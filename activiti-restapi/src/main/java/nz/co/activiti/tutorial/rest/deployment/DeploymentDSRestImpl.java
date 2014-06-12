package nz.co.activiti.tutorial.rest.deployment;

import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.ds.deployment.DeploymentDS;
import nz.co.activiti.tutorial.model.PagingAndSortingParameters;
import nz.co.activiti.tutorial.model.deployment.Deployment;
import nz.co.activiti.tutorial.model.deployment.DeploymentQueryParameters;
import nz.co.activiti.tutorial.model.deployment.DeploymentResource;
import nz.co.activiti.tutorial.model.deployment.Deployments;
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

@Service("deploymentDSRest")
public class DeploymentDSRestImpl extends ActivitiRestClientAccessor implements
		DeploymentDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeploymentDSRestImpl.class);

	@Value("${activiti.api.baseurl}")
	private String baseUrl;

	@Resource
	private DeploymentJSONConverter deploymentJSONConverter;

	@Override
	public List<DeploymentResource> getDeploymentResourcesByDeployId(
			String deploymentId) throws Exception {
		LOGGER.info("getDeploymentResourcesByDeployId start:{}", deploymentId);
		List<DeploymentResource> deploymentResources = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/repository/deployments/" + deploymentId + "/resources");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("getDeploymentResourcesByDeployId failed:{} "
					+ respStr);
		} else {
			deploymentResources = deploymentJSONConverter
					.toDeploymentResources(respStr);
		}
		LOGGER.info("getDeploymentResourcesByDeployId end:{}");
		return deploymentResources;
	}

	@Override
	public DeploymentResource getDeploymentResource(String deploymentId,
			String resourceId) throws Exception {
		LOGGER.info("getDeploymentResource start:{}");
		LOGGER.info("deploymentId:{}", deploymentId);
		LOGGER.info("resourceId:{}", resourceId);
		DeploymentResource deploymentResource = null;

		String encodedResourceId = URLEncoder.encode(resourceId, "UTF-8");

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/deployments/" + deploymentId + "/resources/"
						+ encodedResourceId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("getDeploymentResource failed:{} " + respStr);
		} else {
			deploymentResource = deploymentJSONConverter
					.toDeploymentResource(respStr);
		}
		LOGGER.info("getDeploymentResource end:{}", deploymentResource);
		return deploymentResource;
	}

	@Override
	public Deployments getDeployments(
			Map<DeploymentQueryParameters, String> deploymentQueryParameters,
			Map<PagingAndSortingParameters, String> pagingAndSortingParameters)
			throws Exception {
		LOGGER.info("getDeployments start:{}");
		Deployments deploymentsResponse = null;
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
	public Deployment getDeploymentByDeploymentId(String deploymentId)
			throws Exception {
		LOGGER.info("getDeploymentByDeploymentId start:{}", deploymentId);
		Deployment deploymentResponse = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/repository/deployments/" + deploymentId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("getDeploymentByDeploymentId failed:{} "
					+ respStr);
		} else {
			deploymentResponse = deploymentJSONConverter
					.toDeploymentResponse(respStr);
		}
		return deploymentResponse;
	}

	@Override
	public Deployment deployment(String tenantId, String classpathBpmn,
			String fileName, String fileExtension) throws Exception {
		LOGGER.info("deploymentSingleBpmn start:{}", classpathBpmn);
		Deployment deploymentResponse = null;

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
			throw new Exception("deployment failed:{} " + respStr);
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
