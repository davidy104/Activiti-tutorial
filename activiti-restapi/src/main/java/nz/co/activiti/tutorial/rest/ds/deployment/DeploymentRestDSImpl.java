package nz.co.activiti.tutorial.rest.ds.deployment;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.GenericActivitiRestException;
import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.rest.model.deployment.Deployment;
import nz.co.activiti.tutorial.rest.model.deployment.DeploymentQueryParameter;
import nz.co.activiti.tutorial.rest.model.deployment.DeploymentResource;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.uri.UriComponent;
import com.sun.jersey.api.uri.UriComponent.Type;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.FileDataBodyPart;

@Service
public class DeploymentRestDSImpl extends ActivitiRestClientAccessor implements
		DeploymentRestDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeploymentRestDSImpl.class);

	@Resource
	private DeploymentJSONConverter deploymentJSONConverter;

	@Override
	public Deployment deployment(String tenantId, File uploadFile)
			throws Exception {
		LOGGER.info("deployment start:{}", tenantId);
		Deployment deployment = null;

		FormDataMultiPart multiPart = new FormDataMultiPart();
		multiPart.field("tenantId", tenantId);
		multiPart.bodyPart(new FileDataBodyPart("deployment", uploadFile));

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

		if (statusCode == ClientResponse.Status.CREATED) {
			deployment = deploymentJSONConverter.toDeployment(respStr);

		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(
					"there was no content present in the request body or the content mime-type is not supported for deployment");
		} else {
			throw new Exception(
					"unknown exception when deploy process with tenantId["
							+ tenantId + "]:" + respStr);
		}

		LOGGER.info("deployment end:{}", deployment);
		return deployment;
	}

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

		if (statusCode == ClientResponse.Status.OK) {
			deploymentResources = deploymentJSONConverter
					.toDeploymentResources(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("deployment not found by id["
					+ deploymentId + "]");
		} else {
			throw new Exception("Unknown exception:{} " + respStr);
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

		String encodedResourceId = UriComponent.encode(resourceId,
				Type.PATH_SEGMENT);

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
	public GenericCollectionModel<Deployment> getDeployments(
			Map<DeploymentQueryParameter, String> deploymentQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		LOGGER.info("getDeployments start:{}");
		GenericCollectionModel<Deployment> deployments = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/deployments");

		if (deploymentQueryParameters != null) {
			for (Map.Entry<DeploymentQueryParameter, String> entry : deploymentQueryParameters
					.entrySet()) {
				if (!StringUtils.isEmpty(entry.getValue())) {
					webResource = webResource.queryParam(entry.getKey().name(),
							entry.getValue());
				}
			}
		}

		if (pagingAndSortingParameters != null) {
			this.pagingAndSortQueryParametersUrlBuild(webResource,
					pagingAndSortingParameters);
		}

		LOGGER.info("URI:{}", webResource.getURI().toString());

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
			deployments = deploymentJSONConverter.toDeployments(respStr);
		}
		return deployments;
	}

	@Override
	public Deployment getDeploymentByDeploymentId(String deploymentId)
			throws Exception {
		LOGGER.info("getDeploymentByDeploymentId start:{}", deploymentId);
		Deployment deployment = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/repository/deployments/" + deploymentId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			deployment = deploymentJSONConverter.toDeployment(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("deployment not found by id["
					+ deploymentId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		return deployment;
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

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("deployment not found by id["
					+ deploymentId + "]");
		} else if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknown exception:{}" + respStr);
		}
	}

}
