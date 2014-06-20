package nz.co.activiti.tutorial.simpleprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.ActivitiFacade;
import nz.co.activiti.tutorial.ds.GenericActivityModel;
import nz.co.activiti.tutorial.simpleprocess.config.ApplicationContextConfiguration;

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

	private static final String PROCESS_LOCATION = "process/laptopOrderHumanProcess.bpmn20.xml";
	private static final String PROCESS_NAME = "laptopOrderProcess";
	private static final String PROCESS_CATEGORY = "order";

	@Before
	public void initialize() throws Exception {
		LOGGER.info("initialize start:{}");
		InputStream processStream = LaptopHumanProcessTest.class
				.getClassLoader().getResourceAsStream(PROCESS_LOCATION);

		deployId = activitiFacade.deployment(PROCESS_NAME, PROCESS_CATEGORY,
				processStream).getId();

		// deployId = activitiFacade
		// .deployProcessesFromClasspath(PROCESS_LOCATION).get(0);

		LOGGER.info("deployId:{} ", deployId);

		ProcessDefinition processDefinition = activitiFacade
				.getProcessDefinition(PROCESS_NAME, PROCESS_CATEGORY, deployId);

		processDefinitionId = processDefinition.getId();

		LOGGER.info("processDefinitionId:{}", processDefinitionId);
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
	public void testPendingOrderApprovalTaskStatus() throws Exception {

		String processInstanceId = startProcess();
		assertNotNull(processInstanceId);
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		assertFalse(activitiFacade.ifProcessFinished(orderNo,
				processDefinitionId));

		GenericActivityModel pendingActivity = activitiFacade
				.getActiveActivity(processDefinitionId, orderNo);

		assertNotNull(pendingActivity);
		LOGGER.info("pending activity:{}", pendingActivity);

		assertEquals("userTask", pendingActivity.getType());

		Task pendingTask = activitiFacade.getTask(pendingActivity.getName(),
				orderNo);
		String assignee = pendingTask.getAssignee();
		String taskName = pendingTask.getName();
		assertNotNull(pendingTask);
		assertEquals("Order Approval", taskName);
		assertEquals(USER1_ID, assignee);
	}

	@Test
	public void testOrderRejectStatus() throws Exception {
		startProcess();

		GenericActivityModel pendingActivity = activitiFacade
				.getActiveActivity(processDefinitionId, orderNo);

		String activityName = pendingActivity.getName();
		assertEquals("Order Approval", activityName);

		List<Task> taskList = activitiFacade.getTasksForUser(USER1_ID);
		assertEquals(1, taskList.size());

		Task pendingTask = activitiFacade.getTask(activityName, orderNo);

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
		activitiFacade.completeTask(pendingTask.getId(), variableMap);
		assertTrue(activitiFacade.ifProcessFinished(orderNo,
				processDefinitionId));

	}

	@Test
	public void testOrderApprovalStatus() throws Exception {
		String processInstanceId = startProcess();

		GenericActivityModel pendingActivity = activitiFacade
				.getActiveActivity(processDefinitionId, orderNo);

		String activityName = pendingActivity.getName();
		assertEquals("userTask", pendingActivity.getType());
		assertEquals("Order Approval", activityName);

		Task pendingTask = activitiFacade.getTask(activityName, orderNo);

		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("acceptOrder", true);
		activitiFacade.completeTask(pendingTask.getId(), variableMap);
		assertFalse(activitiFacade.ifProcessFinished(orderNo,
				processDefinitionId));

		// check next pending activity
		pendingActivity = activitiFacade.getActiveActivity(orderNo,
				processDefinitionId);
		assertNotNull(pendingActivity);
		LOGGER.info("pending activity:{}", pendingActivity);
		assertEquals("userTask", pendingActivity.getType());

		pendingTask = activitiFacade
				.getTask(pendingActivity.getName(), orderNo);
		String assignee = pendingTask.getAssignee();
		String taskName = pendingTask.getName();
		assertNotNull(pendingTask);
		assertEquals("Payment Task", taskName);
		assertEquals(USER2_ID, assignee);
	}

	private String startProcess() throws Exception {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("customerName", USER2_ID);
		variableMap.put("laptopName", "Del");
		variableMap.put("laptopQuantity", 1);
		variableMap.put("laptopModelNo", 3420);

		ProcessInstance processInstance = activitiFacade.startProcess(
				processDefinitionId, orderNo, variableMap);
		return processInstance.getId();
	}

	private void initialUsers() throws Exception {
		activitiFacade.createUser(USER1_ID, "kermit", "", "kermit@test.com",
				"123456");
		activitiFacade.createUser(USER2_ID, "Attune", "", "Attune@test.com",
				"123456");
	}

	private void removeUsers() throws Exception {
		activitiFacade.deleteUser(USER1_ID);
		activitiFacade.deleteUser(USER2_ID);
	}
}
