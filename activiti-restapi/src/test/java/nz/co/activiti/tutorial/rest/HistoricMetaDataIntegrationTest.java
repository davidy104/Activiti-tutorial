package nz.co.activiti.tutorial.rest;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.ds.task.TaskDS;
import nz.co.activiti.tutorial.rest.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.rest.ds.deployment.DeploymentRestDS;
import nz.co.activiti.tutorial.rest.ds.processdefinition.ProcessDefinitionRestDS;
import nz.co.activiti.tutorial.rest.ds.processinstance.ProcessInstanceRestDS;
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.deployment.Deployment;
import nz.co.activiti.tutorial.rest.model.processdefinition.ProcessDefinition;
import nz.co.activiti.tutorial.rest.model.processdefinition.ProcessDefinitionQueryParameter;
import nz.co.activiti.tutorial.rest.model.processinstance.ProcessInstance;
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

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class HistoricMetaDataIntegrationTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(HistoricMetaDataIntegrationTest.class);

	private static String REST_URI = "http://localhost:8383/activiti-rest/service";

	@Resource
	private ActivitiRestClientAccessor activitiRestClientAccessor;

	private static final String PROCESS_LOCATION = "process/laptopOrderHumanProcess.bpmn20.xml";

	@Resource
	private DeploymentRestDS deploymentRestDS;

	@Resource
	private ProcessInstanceRestDS processInstanceRestDS;

	@Resource
	private ProcessDefinitionRestDS processDefinitionRestDS;

	@Resource
	private TaskDS taskDSRest;

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

		Deployment deployment = deploymentRestDS.deployment(TENANT_ID,
				processFile);
		deploymentId = deployment.getId();
		LOGGER.info("deploymentId:{} ", deploymentId);
		Map<ProcessDefinitionQueryParameter, String> processDefinitionQueryParameters = new HashMap<ProcessDefinitionQueryParameter, String>();
		processDefinitionQueryParameters.put(
				ProcessDefinitionQueryParameter.deploymentId, deploymentId);
		processDefinitionQueryParameters.put(
				ProcessDefinitionQueryParameter.key, PROCESS_DEFINITION_KEY);
		GenericCollectionModel<ProcessDefinition> processDefinitions = processDefinitionRestDS
				.getProcessDefinitions(processDefinitionQueryParameters, null);
		ProcessDefinition processDefinition = processDefinitions.getModelList()
				.get(0);
		LOGGER.info("processDefinition:{} ", processDefinition);
		processDefinitionId = processDefinition.getId();
	}

	@After
	public void clean() throws Exception {
		if (processInstanceId != null) {
			processInstanceRestDS.deleteProcessInstance(processInstanceId);
		}
		deploymentRestDS.undeployment(deploymentId);
	}

	@Test
	public void testGetHistoricProcessInstanceAfterStart() throws Exception {
		ProcessInstance processInstance = this.startProcess();
		assertNotNull(processInstance);
		processInstanceId = processInstance.getId();

		// printHistoricProcessInstance(processInstanceId);
		// printHistoricProcessInstances();
		this.printIdentities(processInstanceId);
	}

	@Test
	public void testGetHistoricTaskInstance() throws Exception {
		// printHistoricTaskInstances(null);
		printQueryHistoricTaskInstance();
		ProcessInstance processInstance = this.startProcess();
		assertNotNull(processInstance);
		processInstanceId = processInstance.getId();
		printQueryHistoricTaskInstance();
		printHistoricTaskInstances(processInstanceId);

		printHistoryVariableInstances();

	}

	private void printHistoryVariableInstances() throws Exception {
		WebResource webResource = activitiRestClientAccessor.client.resource(
				REST_URI).path("/history/historic-variable-instances");

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = activitiRestClientAccessor
				.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}

	/**
	 * after start process, we can use following method for task instance query
	 * 
	 * @throws Exception
	 */
	private void printQueryHistoricTaskInstance() throws Exception {
		WebResource webResource = activitiRestClientAccessor.client.resource(
				REST_URI).path("/query/historic-task-instances");

		String requstJson = "{\"processDefinitionId\" : \""
				+ processDefinitionId + "\"}";

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, requstJson);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = activitiRestClientAccessor
				.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}

	private void printHistoricTaskInstances(String processInstanceId)
			throws Exception {
		WebResource webResource = activitiRestClientAccessor.client.resource(
				REST_URI).path("/history/historic-task-instances");
		if (processInstanceId != null) {
			webResource = webResource.queryParam("processInstanceId",
					processInstanceId);
		}

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = activitiRestClientAccessor
				.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}

	private void printHistoricProcessInstances() throws Exception {
		WebResource webResource = activitiRestClientAccessor.client
				.resource(REST_URI).path("/history/historic-process-instances")
				.queryParam("businessKey", orderNo);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = activitiRestClientAccessor
				.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}

	private void printHistoricProcessInstance(String processInstanceId)
			throws Exception {
		WebResource webResource = activitiRestClientAccessor.client
				.resource(REST_URI).path("/history/historic-process-instances")
				.path(processInstanceId);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = activitiRestClientAccessor
				.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}

	private void printIdentities(String processInstanceId) throws Exception {
		WebResource webResource = activitiRestClientAccessor.client
				.resource(REST_URI).path("/history/historic-process-instances")
				.path(processInstanceId + "/identitylinks");

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = activitiRestClientAccessor
				.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}

	private ProcessInstance startProcess() throws Exception {
		return processInstanceRestDS.startProcessByProcessDefinitionId(
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
