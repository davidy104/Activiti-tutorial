package nz.co.activiti.tutorial.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.deployment.DeploymentDS;
import nz.co.activiti.tutorial.ds.processdefinition.ProcessDefinitionDS;
import nz.co.activiti.tutorial.ds.processinstance.ProcessInstanceDS;
import nz.co.activiti.tutorial.model.GenericCollectionModel;
import nz.co.activiti.tutorial.model.Identity;
import nz.co.activiti.tutorial.model.Variable;
import nz.co.activiti.tutorial.model.deployment.Deployment;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinition;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinitionQueryParameter;
import nz.co.activiti.tutorial.model.processinstance.ProcessInstance;
import nz.co.activiti.tutorial.rest.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.utils.GeneralUtils;

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
public class ProcessInstanceIntegrationTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessInstanceIntegrationTest.class);

	@Resource
	private DeploymentDS deploymentDSRest;

	@Resource
	private ProcessInstanceDS processInstanceDSRest;

	@Resource
	private ProcessDefinitionDS processDefinitionDSRest;

	private static final String PROCESS_LOCATION = "process/laptopOrderHumanProcess.bpmn20.xml";
	private static final String TENANT_ID = "tenantId7890";
	private static final String PROCESS_DEFINITION_KEY = "laptopHumanProcess";

	private String deploymentId;
	private String processDefinitionId;
	private String orderNo = UUID.randomUUID().toString();

	private static final String USER_ID = "kermit";
	private static final String CUSTOMER_ID = "Attune";

	private String processInstanceId;

	@Before
	public void initialize() throws Exception {
		InputStream processStream = ProcessDeploymentMetaDataTest.class
				.getClassLoader().getResourceAsStream(PROCESS_LOCATION);

		File processFile = File.createTempFile("laptopOrderHumanProcess",
				".bpmn20.xml");
		GeneralUtils.inputStreamToFile(processStream, processFile);

		Deployment deployment = deploymentDSRest.deployment(TENANT_ID,
				processFile);
		deploymentId = deployment.getId();
		LOGGER.info("deploymentId:{} ", deploymentId);
		Map<ProcessDefinitionQueryParameter, String> processDefinitionQueryParameters = new HashMap<ProcessDefinitionQueryParameter, String>();
		processDefinitionQueryParameters.put(
				ProcessDefinitionQueryParameter.deploymentId, deploymentId);
		processDefinitionQueryParameters.put(
				ProcessDefinitionQueryParameter.key, PROCESS_DEFINITION_KEY);
		GenericCollectionModel<ProcessDefinition> processDefinitions = processDefinitionDSRest
				.getProcessDefinitions(processDefinitionQueryParameters, null);
		ProcessDefinition processDefinition = processDefinitions.getModelList()
				.get(0);
		LOGGER.info("processDefinition:{} ", processDefinition);
		processDefinitionId = processDefinition.getId();
	}

	@After
	public void clean() throws Exception {
		if (processInstanceId != null) {
			processInstanceDSRest.deleteProcessInstance(processInstanceId);
		}
		deploymentDSRest.undeployment(deploymentId);
	}

	@Test
	public void testStartProcessInstanceAndCheckProcessInstance()
			throws Exception {
		ProcessInstance processInstance = this.startProcess();
		assertNotNull(processInstance);
		LOGGER.info("after start:{}", processInstance);
		processInstanceId = processInstance.getId();
		ProcessInstance foundProcessInstance = processInstanceDSRest
				.getProcessInstance(processInstanceId);
		assertNotNull(foundProcessInstance);
		assertEquals(foundProcessInstance, processInstance);
	}

	@Test
	public void testSuspendAndActiveProcessInstance() throws Exception {
		ProcessInstance processInstance = this.startProcess();
		assertNotNull(processInstance);
		processInstanceId = processInstance.getId();

		processInstance = processInstanceDSRest
				.suspendProcessInstance(processInstanceId);
		assertNotNull(processInstance);
		assertTrue(processInstance.getSuspended());

		processInstance = processInstanceDSRest
				.activeProcessInstance(processInstanceId);
		assertNotNull(processInstance);
		assertFalse(processInstance.getSuspended());
	}

	@Test
	public void testInvolvedPeople() throws Exception {
		ProcessInstance processInstance = this.startProcess();
		assertNotNull(processInstance);
		processInstanceId = processInstance.getId();

		Set<Identity> parties = processInstanceDSRest
				.getInvolvedPeopleForProcessInstance(processInstanceId);
		assertEquals(parties.size(), 1);
		Identity party = parties.iterator().next();
		assertEquals(party.getUser(), USER_ID);
		LOGGER.info("party:{} ", party);

		// need further check add people function
		// Party addedParty = processInstanceDSRest.addInvolvedPeopleToProcess(
		// processInstanceId, "fozzie", "participant");
		// assertEquals(addedParty.getUser(), "fozzie");
		// LOGGER.info("addedParty:{} ", addedParty);
	}

	@Test
	public void testVariables() throws Exception {
		ProcessInstance processInstance = this.startProcess();
		assertNotNull(processInstance);
		processInstanceId = processInstance.getId();

		List<Variable> addVariables = new ArrayList<Variable>();
		Variable variable = new Variable();
		variable.setName("testVariable");
		variable.setType("string");
		variable.setValue("123");
		variable.setScope("local");
		addVariables.add(variable);
		processInstanceDSRest.createVariablesForProcess(processInstanceId,
				addVariables);

		List<Variable> variables = processInstanceDSRest
				.getVariablesFromProcess(processInstanceId);
		assertNotNull(variables);

		for (Variable tmpVariable : variables) {
			LOGGER.info("variable:{} ", tmpVariable);
		}
	}

	private ProcessInstance startProcess() throws Exception {
		return processInstanceDSRest.startProcessByProcessDefinitionId(
				processDefinitionId, orderNo, mockVariables());
	}

	private Map<String, Object> mockVariables() {
		Map<String, Object> variableMap = new HashMap<String, Object>();
		variableMap.put("customerName", CUSTOMER_ID);
		variableMap.put("laptopName", "Del");
		variableMap.put("laptopQuantity", 1);
		variableMap.put("laptopModelNo", 3420);
		variableMap.put("orderDetails", mockOrderDetails());
		return variableMap;
	}

	private OrderDetails mockOrderDetails() {
		OrderDetails orderDetails = new OrderDetails();
		orderDetails.setOrderId(1L);
		orderDetails.setOrderTime(new Date());
		orderDetails.setShipAddress("20 Opal Ave");
		orderDetails.setOrderNo(orderNo);
		return orderDetails;
	}

}
