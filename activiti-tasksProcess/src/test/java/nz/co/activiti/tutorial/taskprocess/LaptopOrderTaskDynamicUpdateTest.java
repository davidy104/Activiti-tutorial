package nz.co.activiti.tutorial.taskprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.ActivitiFacade;
import nz.co.activiti.tutorial.ds.GenericActivityModel;
import nz.co.activiti.tutorial.taskprocess.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.taskprocess.model.OrderModel;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.DelegationState;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class LaptopOrderTaskDynamicUpdateTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LaptopOrderTaskDynamicUpdateTest.class);

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
	public void testDelegateTask() throws Exception {
		String processInstanceId = startProcess();
		assertNotNull(processInstanceId);
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		assertFalse(activitiFacade.ifProcessFinished(orderNo,
				processDefinitionId));

		// get pending task
		Task pendingTask = activitiFacade.getTask("Order Data Entry", orderNo);
		this.printTask(pendingTask);

		// check original assignee
		List<Task> taskList = activitiFacade.getTasksForUser(USER2_ID);
		assertEquals(taskList.size(), 1);

		// get order from process
		order = (OrderModel) activitiFacade.getVariableOnExecution(
				pendingTask.getExecutionId(), "order");
		LOGGER.info("order:{} ", order);

		// update task with new assignee, state, and owner
		pendingTask.setAssignee(USER1_ID);
		pendingTask.setDelegationState(DelegationState.PENDING);
		pendingTask.setOwner(USER3_ID);
		activitiFacade.updateTask(orderNo, "Order Data Entry", pendingTask);
		this.printTask(pendingTask);

		// check task list for new assignee
		taskList = activitiFacade.getTasksForUser(USER1_ID);
		assertEquals(taskList.size(), 1);

		// check task owner's task
		taskList = activitiFacade.getTasksForUser(USER3_ID);
		assertEquals(taskList.size(), 1);

		// complete task
		TestUtils.submitOrderInfo(order);
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("order", order);
		// if task is delegate (we updated task with delegation state as PENDING
		// before),
		// we should use resolve task first and then complete it

		activitiFacade.resolveTask(pendingTask.getId(), null);

		// after resolve task, process is still pending on this task, so we need
		// to complete it
		GenericActivityModel pendingActivity = activitiFacade
				.getActiveActivity(processDefinitionId, orderNo);
		assertEquals("userTask", pendingActivity.getType());
		assertEquals("Order Data Entry", pendingActivity.getName());

		activitiFacade.completeTask(pendingTask.getId(), variableMap);

		// get order from process after data entry
		order = (OrderModel) activitiFacade.getVariableOnExecution(
				pendingTask.getExecutionId(), "order");
		LOGGER.info("before final decision order:{} ", order);

		pendingActivity = activitiFacade.getActiveActivity(processDefinitionId,
				orderNo);
		assertEquals("Order Approval", pendingActivity.getName());

		this.printHistory(processInstanceId);
	}

	@Test
	public void testTaskPeopleUpdate() throws Exception {
		String processInstanceId = startProcess();
		assertNotNull(processInstanceId);
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		assertFalse(activitiFacade.ifProcessFinished(orderNo,
				processDefinitionId));

		// get pending task
		Task pendingTask = activitiFacade.getTask("Order Data Entry", orderNo);
		this.printTask(pendingTask);

		// check original assignee
		List<Task> taskList = activitiFacade.getTasksForUser(USER2_ID);
		assertEquals(taskList.size(), 1);

		// get order from process
		order = (OrderModel) activitiFacade.getVariableOnExecution(
				pendingTask.getExecutionId(), "order");
		LOGGER.info("order:{} ", order);

		// update task with new assignee, state, and owner
		pendingTask.setAssignee(USER1_ID);
		pendingTask.setOwner(USER3_ID);
		activitiFacade.updateTask(orderNo, "Order Data Entry", pendingTask);
		this.printTask(pendingTask);

		// check task list for new assignee
		taskList = activitiFacade.getTasksForUser(USER1_ID);
		assertEquals(taskList.size(), 1);

		// complete task
		TestUtils.submitOrderInfo(order);
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("order", order);

		activitiFacade.completeTask(pendingTask.getId(), variableMap);

		// get order from process after data entry
		order = (OrderModel) activitiFacade.getVariableOnExecution(
				pendingTask.getExecutionId(), "order");
		LOGGER.info("order:{} ", order);
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

	private void printTask(Task task) {
		LOGGER.info("task Name:{}", task.getName());
		LOGGER.info("deletgationState:{}", task.getDelegationState());
		LOGGER.info("task Assignee:{}", task.getAssignee());
		LOGGER.info("task Owner:{}", task.getOwner());
		LOGGER.info("task defintionKey:{}", task.getTaskDefinitionKey());

	}

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
