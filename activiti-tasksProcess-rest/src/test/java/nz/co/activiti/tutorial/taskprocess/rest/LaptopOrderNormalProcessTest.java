package nz.co.activiti.tutorial.taskprocess.rest;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.laptop.data.OrderModel;
import nz.co.activiti.tutorial.rest.ds.deployment.DeploymentRestDS;
import nz.co.activiti.tutorial.rest.ds.group.GroupRestDS;
import nz.co.activiti.tutorial.rest.ds.processdefinition.ProcessDefinitionRestDS;
import nz.co.activiti.tutorial.rest.ds.processinstance.ProcessInstanceRestDS;
import nz.co.activiti.tutorial.rest.ds.user.UserRestDS;
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.deployment.Deployment;
import nz.co.activiti.tutorial.rest.model.group.Group;
import nz.co.activiti.tutorial.rest.model.processdefinition.ProcessDefinition;
import nz.co.activiti.tutorial.rest.model.processdefinition.ProcessDefinitionQueryParameter;
import nz.co.activiti.tutorial.rest.model.processinstance.ProcessInstance;
import nz.co.activiti.tutorial.rest.model.user.User;
import nz.co.activiti.tutorial.taskprocess.rest.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.utils.GeneralUtils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.gson.Gson;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfiguration.class })
public class LaptopOrderNormalProcessTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LaptopOrderNormalProcessTest.class);

	private String processDefinitionId;

	private String processInstanceId;

	private String deployId;

	private static final String USER1_ID = "jordan";
	private static final String USER2_ID = "mike";
	private static final String USER3_ID = "john";
	private static final String GROUP_ID = "orderAdmin";

	private static final String TENANT_ID = "laptopOrderProcess999000";

	// processKey
	private String orderNo;

	private OrderModel order;

	@Resource
	private DeploymentRestDS deploymentRestDs;

	@Resource
	private ProcessDefinitionRestDS processDefinitionRestDs;

	@Resource
	private UserRestDS userRestDs;

	@Resource
	private GroupRestDS groupRestDs;

	@Resource
	private ProcessInstanceRestDS processInstanceRestDs;

	@Before
	public void initialize() throws Exception {
		LOGGER.info("initialize start:{}");

		InputStream processStream = LaptopOrderNormalProcessTest.class
				.getClassLoader().getResourceAsStream(
						"process/TasksTestProcess02.bpmn20.xml");

		File processFile = File.createTempFile("TasksTestProcess",
				".bpmn20.xml");
		GeneralUtils.inputStreamToFile(processStream, processFile);

		Deployment deployment = deploymentRestDs.deployment(TENANT_ID,
				processFile);
		assertNotNull(deployment);
		LOGGER.info("deployment:{} ", deployment);

		deployId = deployment.getId();

		Map<ProcessDefinitionQueryParameter, String> processDefinitionQueryParameters = new HashMap<ProcessDefinitionQueryParameter, String>();
		processDefinitionQueryParameters.put(
				ProcessDefinitionQueryParameter.deploymentId, deployId);

		GenericCollectionModel<ProcessDefinition> processDefinitionsResponse = processDefinitionRestDs
				.getProcessDefinitions(processDefinitionQueryParameters, null);
		ProcessDefinition processDefinition = processDefinitionsResponse
				.getModelList().get(0);
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
		if (processInstanceId != null) {
			processInstanceRestDs.deleteProcessInstance(processInstanceId);
		}
		deploymentRestDs.undeployment(deployId);
		LOGGER.info("undeploy process end:{}");
	}

	@Test
	public void test() throws Exception {
		ProcessInstance processInstance = this.startProcess();
		LOGGER.info("get processInstance afterstart:{}", processInstance);
		processInstanceId = processInstance.getId();
		processInstanceRestDs.getLegacyProcessInstance(processInstanceId);

	}

	private ProcessInstance startProcess() throws Exception {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String orderJson = gson.toJson(order);
		LOGGER.info("orderJson:{}", orderJson);
		variableMap.put("order", orderJson);

		ProcessInstance processInstance = processInstanceRestDs
				.startProcessByProcessDefinitionId(processDefinitionId,
						orderNo, variableMap);
		assertNotNull(processInstance);
		LOGGER.info("processInstance:{} ", processInstance);

		return processInstance;
	}

	private void initialUsers() throws Exception {
		Group group = new Group();
		group.setId(GROUP_ID);
		group.setName(GROUP_ID);
		groupRestDs.createGroup(group);

		User addUser = new User();
		addUser.setId(USER1_ID);
		addUser.setFirstName(USER1_ID);
		addUser.setEmail(USER1_ID + "@test.com");
		addUser.setPassword("123456");
		userRestDs.createUser(addUser);

		addUser = new User();
		addUser.setId(USER2_ID);
		addUser.setFirstName(USER2_ID);
		addUser.setEmail(USER2_ID + "@test.com");
		addUser.setPassword("123456");
		userRestDs.createUser(addUser);

		addUser = new User();
		addUser.setId(USER3_ID);
		addUser.setFirstName(USER3_ID);
		addUser.setEmail(USER3_ID + "@test.com");
		addUser.setPassword("123456");
		userRestDs.createUser(addUser);

		groupRestDs.createMemberToGroup(GROUP_ID, USER1_ID);
		groupRestDs.createMemberToGroup(GROUP_ID, USER2_ID);
		groupRestDs.createMemberToGroup(GROUP_ID, USER3_ID);
	}

	private void removeUsers() throws Exception {
		groupRestDs.deleteMemberFromGroup(GROUP_ID, USER1_ID);
		groupRestDs.deleteMemberFromGroup(GROUP_ID, USER2_ID);
		groupRestDs.deleteMemberFromGroup(GROUP_ID, USER3_ID);

		userRestDs.deleteUser(USER1_ID);
		userRestDs.deleteUser(USER2_ID);
		userRestDs.deleteUser(USER3_ID);

		groupRestDs.deleteGroup(GROUP_ID);
	}

}
