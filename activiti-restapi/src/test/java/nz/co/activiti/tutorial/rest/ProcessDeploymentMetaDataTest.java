package nz.co.activiti.tutorial.rest;

import java.io.File;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.rest.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.utils.GeneralUtils;

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
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class ProcessDeploymentMetaDataTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessDeploymentMetaDataTest.class);

	private static String REST_URI = "http://localhost:8383/activiti-rest/service";

	@Resource
	private ActivitiRestClientAccessor activitiRestClientAccessor;

	private static final String PROCESS_LOCATION = "process/laptopOrderHumanProcess.bpmn20.xml";

	@Test
	public void testDeployment() throws Exception {
		InputStream processStream = ProcessDeploymentMetaDataTest.class
				.getClassLoader().getResourceAsStream(PROCESS_LOCATION);

		File processFile = File.createTempFile(
				"laptopOrderHumanProcess", ".bpmn20.xml");

		GeneralUtils.inputStreamToFile(processStream, processFile);

		// File fileToUpload = new File(
		// "/home/david/study/activiti5/code/activiti-tutorial/activiti-restapi/src/main/resources/process/laptopOrderHumanProcess.bpmn20.xml");

		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.field("name", "laptopProcess");
		multiPart.bodyPart(new FileDataBodyPart("deployment", processFile));

		WebResource webResource = activitiRestClientAccessor.client.resource(
				REST_URI).path("/repository/deployments");

		ClientResponse response = webResource
				.type(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, multiPart);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = activitiRestClientAccessor
				.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}

	@Test
	public void testUnDeployment() throws Exception {
		String deploymentId = "44";
		WebResource webResource = activitiRestClientAccessor.client.resource(
				REST_URI).path("/repository/deployments/" + deploymentId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = activitiRestClientAccessor
				.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}
	
	
	
	

	@Test
	public void testGetProcessDefinitions() throws Exception {

		WebResource webResource = activitiRestClientAccessor.client.resource(
				REST_URI).path("/repository/process-definitions");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = activitiRestClientAccessor
				.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}

	@Test
	public void testGetProcessDefinitionById() throws Exception {
		String processDefinitionId = "createTimersProcess:1:31";

		LOGGER.info("userId:{} ", activitiRestClientAccessor.getUserId());

		WebResource webResource = activitiRestClientAccessor.client.resource(
				REST_URI).path(
				"/repository/process-definitions/" + processDefinitionId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = activitiRestClientAccessor
				.getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}
}
