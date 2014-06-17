package nz.co.activiti.tutorial.simpleprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ProcessActivityDto;
import nz.co.activiti.tutorial.simpleprocess.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.utils.ActivitiFacade;

import org.activiti.engine.identity.User;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
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
public class LaptopHumanProcessTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(LaptopHumanProcessTest.class);

	@Resource
	private ActivitiFacade activitiFacade;

	private String processDefinitionId;

	private String deployId;

	private static final String USER1_ID = "kermit";
	private static final String USER2_ID = "Attune";

	// processKey
	private String orderNo = UUID.randomUUID().toString();

	@Before
	public void initialize() throws Exception {
		LOGGER.info("initialize start:{}");

		deployId = activitiFacade.deployProcessesFromClasspath(
				"process/laptopOrderHumanProcess.bpmn20.xml").get(0);

		ProcessDefinition processDefinition = activitiFacade
				.getProcessDefinitionByDeployId(deployId);
		processDefinitionId = processDefinition.getId();
		LOGGER.info("deployId:{}", deployId);
		LOGGER.info("processDefinitionId:{}", processDefinitionId);
		initialUsers();
		LOGGER.info("initialize end:{}");
	}

	@After
	public void clean() throws Exception {
		LOGGER.info("undeploy process start:{}");
		removeUsers();
		activitiFacade.unDeploy(deployId, true);
		LOGGER.info("undeploy process end:{}");
	}

	@Test
	public void testPendingOrderApprovalTaskStatus() throws Exception {

		String processInstanceId = startProcess();
		assertNotNull(processInstanceId);
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		assertFalse(activitiFacade.ifProcessFinishted(orderNo,
				processDefinitionId));

		ProcessActivityDto pendingActivity = activitiFacade
				.getExecutionActivityBasicInfo(orderNo, processDefinitionId,
						processInstanceId, true, true);
		assertNotNull(pendingActivity);
		LOGGER.info("pending activity:{}", pendingActivity);

		assertEquals("userTask", pendingActivity.getType());

		Task pendingTask = activitiFacade.getActiveTaskByNameAndBizKey(
				pendingActivity.getName(), orderNo);
		String assignee = pendingTask.getAssignee();
		String taskName = pendingTask.getName();
		assertNotNull(pendingTask);
		assertEquals("Order Approval", taskName);
		assertEquals(USER1_ID, assignee);
	}

	@Test
	public void testOrderRejectStatus() throws Exception {

		String processInstanceId = startProcess();
		ActivityImpl activity = activitiFacade.getExecutionActivity(
				processDefinitionId, orderNo, processInstanceId);
		String activityName = (String) activity.getProperty("name");
		assertEquals("Order Approval", activityName);

		List<Task> taskList = activitiFacade.getAllTasksForUser(String
				.valueOf(USER1_ID));
		assertEquals(1, taskList.size());

		Task pendingTask = activitiFacade.getActiveTaskByNameAndBizKey(
				activityName, orderNo);

		boolean ifHasRight = activitiFacade.checkIfUserHasRightForGivenTask(
				orderNo, pendingTask.getName(), pendingTask.getAssignee());
		assertTrue(ifHasRight);

		assertEquals("Order Approval", pendingTask.getName());
		assertEquals(USER1_ID, pendingTask.getAssignee());

		// using assignee, no need to claim task, compare with group
		// activitiFacade.getTaskService().claim(pendingTask.getId(), USER1_ID);

		// TaskFormData taskFormData = activitiFacade.getFormService()
		// .getTaskFormData(pendingTask.getId());
		//
		// List<FormProperty> formProperties = taskFormData.getFormProperties();
		// for(FormProperty formProperty : formProperties){
		// LOGGER.info("formName:{} ", formProperty.getName());
		// LOGGER.info("formValue:{} ", formProperty.getValue());
		// LOGGER.info("formType:{} ", formProperty.getType());
		// }

		// we can use submitTaskFormData to complete formtas
		// Map<String, String> variableMap = new HashMap<String, String>();
		// variableMap.put("acceptOrder", "false");
		// activitiFacade.getFormService().submitTaskFormData(pendingTask.getId(),
		// variableMap);

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("acceptOrder", false);
		activitiFacade.getTaskService().complete(pendingTask.getId(),
				variableMap);
		assertTrue(activitiFacade.ifProcessFinishted(orderNo,
				processDefinitionId));

	}

	@Test
	public void testOrderApprovalStatus() throws Exception {
		String processInstanceId = startProcess();
		ActivityImpl activity = activitiFacade.getExecutionActivity(
				processDefinitionId, orderNo, processInstanceId);
		String activityName = (String) activity.getProperty("name");
		assertEquals("Order Approval", activityName);

		Task pendingTask = activitiFacade.getActiveTaskByNameAndBizKey(
				activityName, orderNo);

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("acceptOrder", true);
		activitiFacade.getTaskService().complete(pendingTask.getId(),
				variableMap);
		assertFalse(activitiFacade.ifProcessFinishted(orderNo,
				processDefinitionId));

		// check next pending activity
		ProcessActivityDto pendingActivity = activitiFacade
				.getExecutionActivityBasicInfo(orderNo, processDefinitionId,
						processInstanceId, true, true);
		assertNotNull(pendingActivity);
		LOGGER.info("pending activity:{}", pendingActivity);

		assertEquals("userTask", pendingActivity.getType());

		pendingTask = activitiFacade.getActiveTaskByNameAndBizKey(
				pendingActivity.getName(), orderNo);
		String assignee = pendingTask.getAssignee();
		String taskName = pendingTask.getName();
		assertNotNull(pendingTask);
		assertEquals("Payment Task", taskName);
		assertEquals(USER2_ID, assignee);
	}

	private String startProcess() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("customerName", USER2_ID);
		variableMap.put("laptopName", "Del");
		variableMap.put("laptopQuantity", 1);
		variableMap.put("laptopModelNo", 3420);

		ProcessInstance processInstance = activitiFacade.startProcessInstance(
				orderNo, processDefinitionId, variableMap);
		return processInstance.getId();
	}

	private void initialUsers() {
		User user1 = activitiFacade.getIdentityService().newUser(USER1_ID);
		User user2 = activitiFacade.getIdentityService().newUser(USER2_ID);
		activitiFacade.getIdentityService().saveUser(user1);
		activitiFacade.getIdentityService().saveUser(user2);
	}

	private void removeUsers() {
		activitiFacade.getIdentityService().deleteUser(USER1_ID);
		activitiFacade.getIdentityService().deleteUser(USER2_ID);
	}
}
