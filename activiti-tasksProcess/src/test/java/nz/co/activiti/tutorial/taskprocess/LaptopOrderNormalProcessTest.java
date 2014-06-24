package nz.co.activiti.tutorial.taskprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.ActivitiFacade;
import nz.co.activiti.tutorial.ds.GenericActivityModel;
import nz.co.activiti.tutorial.laptop.data.OrderModel;
import nz.co.activiti.tutorial.taskprocess.config.ApplicationContextConfiguration;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfiguration.class })
public class LaptopOrderNormalProcessTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LaptopOrderNormalProcessTest.class);

	@Resource
	private ActivitiFacade activitiFacade;

	private String processDefinitionId;

	private String deployId;

	private static final String USER1_ID = "gonzo";
	private static final String USER2_ID = "kermit";
	private static final String USER3_ID = "fozzie";
	private static final String GROUP_ID = "orderAdmin";

	// processKey
	private String orderNo;

	private OrderModel order;

	@Resource
	private FormService formService;

	@Resource
	private HistoryService historyService;

	@Before
	public void initialize() throws Exception {
		LOGGER.info("initialize start:{}");

		deployId = activitiFacade.deployProcessesFromClasspath(
				"process/TasksTestProcess.bpmn20.xml").get(0);

		ProcessDefinition processDefinition = activitiFacade
				.getProcessDefinitionByDeploymentId(deployId);
		processDefinitionId = processDefinition.getId();
		LOGGER.info("deployId:{}", deployId);
		LOGGER.info("processDefinitionId:{}", processDefinitionId);

		order = TestUtils.initOrder();
		orderNo = order.getOrderNo();

		initialUsers();
		LOGGER.info("initialize end:{}");
	}

	@After
	public void clean() throws Exception {
		LOGGER.info("undeploy process start:{}");
		removeUsers();
		activitiFacade.undeployment(deployId);
		LOGGER.info("undeploy process end:{}");
	}

	@Test
	public void test() throws Exception {
		String processInstanceId = startProcess();
		assertNotNull(processInstanceId);
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		assertFalse(activitiFacade.ifProcessFinished(orderNo,
				processDefinitionId));
		// after start check active activity
		GenericActivityModel pendingActivity = activitiFacade
				.getActiveActivity(processDefinitionId, orderNo);

		assertNotNull(pendingActivity);
		LOGGER.info("pending activity:{}", pendingActivity);
		assertEquals("userTask", pendingActivity.getType());
		assertEquals("Order Data Entry", pendingActivity.getName());

		// get active Task
		Task pendingTask = activitiFacade.getTask(pendingActivity.getName(),
				orderNo);

		TaskFormData taskFormData = formService.getTaskFormData(pendingTask
				.getId());
		// get form properties from task
		List<FormProperty> formProperties = taskFormData.getFormProperties();
		assertNotNull(formProperties);
		for (FormProperty formProperty : formProperties) {
			LOGGER.info("formName:{} ", formProperty.getName());
			LOGGER.info("formValue:{} ", formProperty.getValue());
			LOGGER.info("formType:{} ", formProperty.getType());
		}

		// get OrderModel object from process variable
		order = (OrderModel) activitiFacade.getVariableOnExecution(
				pendingTask.getExecutionId(), "order");
		assertNotNull(order);
		LOGGER.info("order:{} ", order);

		// submit order info, filling customer info
		TestUtils.submitOrderInfo(order);
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("order", order);
		activitiFacade.completeTask(pendingTask.getId(), variableMap);

		assertFalse(activitiFacade.ifProcessFinished(orderNo,
				processDefinitionId));

		pendingActivity = activitiFacade.getActiveActivity(processDefinitionId,
				orderNo);
		assertNotNull(pendingActivity);
		LOGGER.info("pending activity:{}", pendingActivity);
		assertEquals("Order Approval", pendingActivity.getName());
		assertEquals("userTask", pendingActivity.getType());

		order = (OrderModel) activitiFacade.getVariableOnExecution(
				pendingTask.getExecutionId(), "order");
		LOGGER.info("after calculation order:{} ", order);

		// get order approval task
		pendingTask = activitiFacade
				.getTask(pendingActivity.getName(), orderNo);

		// check gonzo and fozzie's task (both of them belonging to orderAdmin
		// group)
		List<Task> taskList = activitiFacade.getTasksForUser(USER1_ID);
		assertEquals(1, taskList.size());
		taskList = activitiFacade.getTasksForUser(USER2_ID);
		assertEquals(1, taskList.size());

		// gonzo claim task, and check their tasklist again, fozzie's gone
		pendingTask = activitiFacade.claimTask(pendingTask.getId(), USER1_ID);
		assertNotNull(pendingTask);
		assertEquals(pendingTask.getAssignee(), USER1_ID);

		taskList = activitiFacade.getTasksForUser(USER1_ID);
		assertEquals(1, taskList.size());
		taskList = activitiFacade.getTasksForUser(USER2_ID);
		assertEquals(0, taskList.size());

		// gonzo complete Order approval task
		variableMap = new HashMap<String, Object>();
		variableMap.put("order", order);
		variableMap.put("acceptOrder", true);
		activitiFacade.completeTask(pendingTask.getId(), variableMap);

		// check active task, it should be payment task
		pendingActivity = activitiFacade.getActiveActivity(processDefinitionId,
				orderNo);
		assertEquals(pendingActivity.getName(), "Order Payment");
		assertEquals(pendingActivity.getType(), "userTask");

		pendingTask = activitiFacade
				.getTask(pendingActivity.getName(), orderNo);
		LOGGER.info("pendingTask:{}", pendingTask);
		assertEquals(pendingTask.getAssignee(), "david");

		order = (OrderModel) activitiFacade.getVariableOnExecution(
				pendingTask.getExecutionId(), "order");
		assertNotNull(order);

		// david finish payment task
		variableMap = new HashMap<String, Object>();
		variableMap.put("order", order);
		variableMap.put("accountNo", "1234567");
		activitiFacade.completeTask(pendingTask.getId(), variableMap);

		// check active task, it should be receive task
		pendingActivity = activitiFacade.getActiveActivity(processDefinitionId,
				orderNo);
		LOGGER.info("pendingActivity:{} ", pendingActivity);
		assertEquals(pendingActivity.getName(), "Payment Confirm");
		assertEquals(pendingActivity.getType(), "receiveTask");

		ProcessInstance processInstance = activitiFacade.getProcessInstance(
				orderNo, processDefinitionId);

		String accountNo = (String) activitiFacade.getVariableOnExecution(
				processInstance.getId(), "accountNo");
		LOGGER.info("accountNo from process:{}", accountNo);

		order.setStatus("delivered");
		LOGGER.info("before deliver:{}", order);
		variableMap = new HashMap<String, Object>();
		variableMap.put("order", order);
		activitiFacade.signal(processInstance.getId(), variableMap);
		assertTrue(activitiFacade.ifProcessFinished(orderNo,
				processDefinitionId));

		processInstanceId = processInstance.getId();
		order = (OrderModel) activitiFacade.getHistoricVariableOnProcess(
				processInstance.getId(), "order");
		LOGGER.info("get historic order:{}", order);

		this.printHistory(processInstanceId);

	}

	private String startProcess() throws Exception {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("order", order);
		ProcessInstance processInstance = activitiFacade.startProcess(
				processDefinitionId, orderNo, variableMap);
		return processInstance.getId();
	}

	private void initialUsers() throws Exception {
		activitiFacade.createGroup(GROUP_ID, GROUP_ID, null);

		activitiFacade.createUser(USER1_ID, USER1_ID, "", USER1_ID
				+ "@test.com", "123456");

		activitiFacade.createUser(USER2_ID, USER2_ID, "", USER2_ID
				+ "@test.com", "123456");

		activitiFacade.createUser(USER3_ID, USER3_ID, "", USER3_ID
				+ "@test.com", "123456");

		activitiFacade.createMembership(USER1_ID, GROUP_ID);
		activitiFacade.createMembership(USER2_ID, GROUP_ID);
		activitiFacade.createMembership(USER3_ID, GROUP_ID);
	}

	private void removeUsers() throws Exception {
		activitiFacade.deleteMemberFromGroup(GROUP_ID, USER1_ID);
		activitiFacade.deleteMemberFromGroup(GROUP_ID, USER2_ID);
		activitiFacade.deleteMemberFromGroup(GROUP_ID, USER3_ID);
		activitiFacade.deleteUser(USER1_ID);
		activitiFacade.deleteUser(USER2_ID);
		activitiFacade.deleteUser(USER3_ID);
		activitiFacade.deleteGroup(GROUP_ID);
	}

	// private void printTask(Task task) {
	// LOGGER.info("task Name:{}", task.getName());
	// LOGGER.info("deletgationState:{}", task.getDelegationState());
	// LOGGER.info("task Assignee:{}", task.getAssignee());
	// LOGGER.info("task Owner:{}", task.getOwner());
	// LOGGER.info("task defintionKey:{}", task.getTaskDefinitionKey());
	// }

	private void printHistory(String processInstanceId) throws Exception {
		LOGGER.info("print historicIdentity start:{}");
		List<HistoricIdentityLink> historicIdentityList = historyService
				.getHistoricIdentityLinksForProcessInstance(processInstanceId);
		for (HistoricIdentityLink historicIdentityLink : historicIdentityList) {
			String info = "[userId:" + historicIdentityLink.getUserId()
					+ ", groupId:" + historicIdentityLink.getGroupId()
					+ ", type:" + historicIdentityLink.getType() + ", taskId:"
					+ historicIdentityLink.getTaskId();

			LOGGER.info("historicIdentity:{} ", info);
		}
		LOGGER.info("print historicIdentity end:{}");

		LOGGER.info("print HistoricTaskInstance start:{}");
		List<HistoricTaskInstance> taskInstances = historyService
				.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId)
				.orderByHistoricTaskInstanceStartTime().desc().list();

		for (HistoricTaskInstance historicTaskInstance : taskInstances) {
			String info = "[taskName:" + historicTaskInstance.getName()
					+ ", assignee:" + historicTaskInstance.getAssignee()
					+ ", duriation:"
					+ historicTaskInstance.getDurationInMillis()
					+ ", claimTime:" + historicTaskInstance.getClaimTime();

			LOGGER.info("historicTaskInstance:{} ", info);
		}
		LOGGER.info("print HistoricTaskInstance end:{}");

		List<HistoricActivityInstance> historicActivityInstances = historyService
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				.processDefinitionId(processDefinitionId)
				.orderByHistoricActivityInstanceEndTime().asc().list();
		LOGGER.info("print historicActivityInstance start:{}");
		for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
			String info = "[name:" + historicActivityInstance.getActivityName()
					+ ", type:" + historicActivityInstance.getActivityType()
					+ ", CalledProcessInstanceId:"
					+ historicActivityInstance.getCalledProcessInstanceId()
					+ ", executionId:"
					+ historicActivityInstance.getExecutionId()
					+ ", startTime:" + historicActivityInstance.getStartTime()
					+ ", endTime:" + historicActivityInstance.getEndTime();
			LOGGER.info("historicActivityInstance:{} ", info);
		}
		LOGGER.info("print historicActivityInstance end:{}");

		List<HistoricDetail> historicDetails = historyService
				.createHistoricDetailQuery()
				.processInstanceId(processInstanceId).orderByTime().asc()
				.list();
		for (HistoricDetail historicDetail : historicDetails) {
			String info = "[id:" + historicDetail.getId() + ", taskId:"
					+ historicDetail.getTaskId() + ", processInstanceId:"
					+ historicDetail.getProcessInstanceId() + ", time:"
					+ historicDetail.getTime();

			LOGGER.info("historicDetail:{} ", info);
		}
	}
}
