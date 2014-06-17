package nz.co.activiti.tutorial.traningprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ProcessActivityDto;
import nz.co.activiti.tutorial.traningprocess.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.utils.ActivitiFacade;

import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
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
public class TrainingRequest01IntegrationTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TrainingRequestIntegrationTest.class);

	@Resource
	private ActivitiFacade activitiFacade;

	private String processDefinitionId;

	private String deployId;

	private static final String USER1_ID = "gonzo";
	private static final String USER2_ID = "kermit";
	private static final String GROUP_ID = "engineering";

	// processKey
	private String requestNo = UUID.randomUUID().toString();

	@Before
	public void initialize() throws Exception {
		LOGGER.info("initialize start:{}");

		deployId = activitiFacade.deployProcessesFromClasspath(
				"process/trainingRequest.bpmn20.xml").get(0);

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
	public void testPendingBusinessDevelopmentTaskStatus() throws Exception {
		String processInstanceId = startProcess();
		assertNotNull(processInstanceId);
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		assertFalse(activitiFacade.ifProcessFinishted(requestNo,
				processDefinitionId));

		ProcessActivityDto pendingActivity = activitiFacade
				.getExecutionActivityBasicInfo(requestNo, processDefinitionId,
						processInstanceId, true, true);
		assertNotNull(pendingActivity);
		LOGGER.info("pending activity:{}", pendingActivity);

		assertEquals("userTask", pendingActivity.getType());

		Task pendingTask = activitiFacade.getActiveTaskByNameAndBizKey(
				pendingActivity.getName(), requestNo);
		String assignee = pendingTask.getAssignee();
		String taskName = pendingTask.getName();
		assertNotNull(pendingTask);
		assertEquals("Business Development Executive", taskName);
		assertEquals(USER1_ID, assignee);

		List<Task> taskList = activitiFacade.getAllTasksForUser(USER1_ID);
		assertEquals(1, taskList.size());

		//with declared assignee, can not find task in his group
		taskList = activitiFacade.getAllTasksForGroup(GROUP_ID);
		assertEquals(0, taskList.size());
	}

	private String startProcess() {
		Map<String, Object> variableMap = TrainingRequestProcessTestUtils
				.getRequestVariables();
		ProcessInstance processInstance = activitiFacade.startProcessInstance(
				requestNo, processDefinitionId, variableMap);
		return processInstance.getId();
	}

	private void initialUsers() {
		Group group = activitiFacade.getIdentityService().newGroup(GROUP_ID);
		activitiFacade.getIdentityService().saveGroup(group);

		User user1 = activitiFacade.getIdentityService().newUser(USER1_ID);
		activitiFacade.getIdentityService().saveUser(user1);

		User user2 = activitiFacade.getIdentityService().newUser(USER2_ID);
		activitiFacade.getIdentityService().saveUser(user2);

		activitiFacade.getIdentityService().createMembership(user1.getId(),
				group.getId());
		activitiFacade.getIdentityService().createMembership(user2.getId(),
				group.getId());
	}

	private void removeUsers() {
		activitiFacade.getIdentityService()
				.deleteMembership(USER1_ID, GROUP_ID);
		activitiFacade.getIdentityService()
				.deleteMembership(USER2_ID, GROUP_ID);
		activitiFacade.getIdentityService().deleteUser(USER1_ID);
		activitiFacade.getIdentityService().deleteUser(USER2_ID);
		activitiFacade.getIdentityService().deleteGroup(GROUP_ID);
	}

}
