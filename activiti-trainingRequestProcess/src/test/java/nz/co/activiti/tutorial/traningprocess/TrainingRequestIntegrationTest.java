package nz.co.activiti.tutorial.traningprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.ActivitiFacade;
import nz.co.activiti.tutorial.ds.GenericActivityModel;
import nz.co.activiti.tutorial.traningprocess.config.ApplicationContextConfiguration;

import org.activiti.engine.FormService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
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
@Ignore
public class TrainingRequestIntegrationTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrainingRequestIntegrationTest.class);

	@Resource
	private ActivitiFacade activitiFacade;

	private String processDefinitionId;

	private String deployId;

	private static final String USER_ID = "gonzo";

	@Resource
	private FormService formService;

	// processKey
	private String requestNo = UUID.randomUUID().toString();

	@Before
	public void initialize() throws Exception {
		LOGGER.info("initialize start:{}");

		deployId = activitiFacade.deployProcessesFromClasspath(
				"process/trainingRequest.bpmn20.xml").get(0);

		ProcessDefinition processDefinition = activitiFacade
				.getProcessDefinitionByDeploymentId(deployId);
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
		activitiFacade.undeployment(deployId);
		LOGGER.info("undeploy process end:{}");
	}

	@Test
	public void testPendingBusinessDevelopmentTaskStatus() throws Exception {
		String processInstanceId = startProcess();
		assertNotNull(processInstanceId);
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		assertFalse(activitiFacade.ifProcessFinished(requestNo,
				processDefinitionId));

		GenericActivityModel pendingActivity = activitiFacade
				.getActiveActivity(processDefinitionId, requestNo);
		assertNotNull(pendingActivity);
		LOGGER.info("pending activity:{}", pendingActivity);

		assertEquals("userTask", pendingActivity.getType());

		Task pendingTask = activitiFacade.getTask(pendingActivity.getName(),
				requestNo);
		String assignee = pendingTask.getAssignee();
		String taskName = pendingTask.getName();
		assertNotNull(pendingTask);
		assertEquals("Business Development Executive", taskName);
		assertEquals(USER_ID, assignee);
	}

	@Test
	public void testCompleteTask() throws Exception {
		startProcess();
		GenericActivityModel pendingActivity = activitiFacade
				.getActiveActivity(processDefinitionId, requestNo);
		String activityName = pendingActivity.getName();
		assertEquals("Business Development Executive", activityName);

		Task pendingTask = activitiFacade.getTask(activityName, requestNo);

		TaskFormData taskFormData = formService.getTaskFormData(pendingTask
				.getId());

		assertNotNull(taskFormData);

		List<FormProperty> formProperties = taskFormData.getFormProperties();
		// assertEquals(formProperties.size(),4);

		for (FormProperty formProperty : formProperties) {
			LOGGER.info("formName:{} ", formProperty.getName());
			LOGGER.info("formValue:{} ", formProperty.getValue());
			LOGGER.info("formType:{} ", formProperty.getType());
		}

		// we can use submitTaskFormData to complete formtas
		Map<String, String> variableMap = new HashMap<String, String>();
		variableMap.put("trainerName", "Jun Yuan");
		variableMap.put("trainerMailId", "david.yuan124@gmail.com");
		formService.submitTaskFormData(pendingTask.getId(), variableMap);

		assertTrue(activitiFacade.ifProcessFinished(requestNo,
				processDefinitionId));

	}

	private String startProcess() throws Exception {
		Map<String, Object> variableMap = TrainingRequestProcessTestUtils
				.getRequestVariables();
		ProcessInstance processInstance = activitiFacade.startProcess(
				processDefinitionId, requestNo, variableMap);
		return processInstance.getId();
	}

	private void initialUsers() throws Exception {
		activitiFacade.createUser(USER_ID, USER_ID, "", USER_ID + "@test.com",
				"123456");
	}

	private void removeUsers() throws Exception {
		activitiFacade.deleteUser(USER_ID);
	}

}
