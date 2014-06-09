package nz.co.activiti.tutorial.rest.deployment;

import java.io.File;
import java.io.InputStream;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;
import nz.co.activiti.tutorial.rest.ProcessDeploymentMetaDataTest;
import nz.co.activiti.tutorial.utils.GeneralUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

@Service
public class DeploymentDSImpl extends ActivitiRestClientAccessor implements
		DeploymentDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeploymentDSImpl.class);

	@Value("${activiti.api.baseurl}")
	private String baseUrl;

	@Resource
	private DeploymentJSONConverter deploymentJSONConverter;

	@Override
	public DeploymentsResponse getAllDeployments() throws Exception {
		LOGGER.info("getAllDeployments start:{}");
		DeploymentsResponse deploymentsResponse = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/repository/deployments");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("getAllDeployments failed:{} " + respStr);
		} else {
			deploymentsResponse = deploymentJSONConverter
					.toDeploymentsResponse(respStr);
		}

		return deploymentsResponse;
	}

	@Override
	public DeploymentResponse deployment(String tenantId, String classpathBpmn,
			String fileName, String fileExtension) throws Exception {
		LOGGER.info("deploymentSingleBpmn start:{}", classpathBpmn);
		DeploymentResponse deploymentResponse = null;

		InputStream processStream = ProcessDeploymentMetaDataTest.class
				.getClassLoader().getResourceAsStream(classpathBpmn);

		File processFile = File.createTempFile(fileName, fileExtension);
		GeneralUtils.inputStreamToFile(processStream, processFile);

		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.field("tenantId", tenantId);
		multiPart.bodyPart(new FileDataBodyPart("deployment", processFile));

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/deployments");

		ClientResponse response = webResource
				.type(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, multiPart);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.CREATED) {
			throw new Exception("deploymentSingleBpmn failed:{} " + respStr);
		} else {
			deploymentResponse = deploymentJSONConverter
					.toDeploymentResponse(respStr);
		}

		LOGGER.info("deploymentSingleBpmn end:{}", deploymentResponse);
		return deploymentResponse;
	}

	@Override
	public void undeployment(String deploymentId) throws Exception {
		LOGGER.info("undeployment start:{}", deploymentId);
		WebResource webResource = client.resource(baseUrl).path(
				"/repository/deployments/" + deploymentId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("undeployment failed:{} " + respStr);
		}
	}

}
