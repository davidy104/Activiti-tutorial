package nz.co.activiti.tutorial.taskprocess;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.ActivitiFacade;
import nz.co.activiti.tutorial.ds.GenericActivityModel;
import nz.co.activiti.tutorial.taskprocess.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.taskprocess.model.CustomerModel;
import nz.co.activiti.tutorial.taskprocess.model.OrderModel;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
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
public class LaptopOrderProcessTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LaptopOrderProcessTest.class);

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
	public void testToDataEntry() throws Exception {
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
		assertEquals("Order Data Entry", pendingActivity.getName());

	}

	private String startProcess() throws Exception {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("order", order);
		variableMap.put("customer", new CustomerModel());
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

}
