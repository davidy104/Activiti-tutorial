package nz.co.activiti.tutorial.rest.processdefinition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.GenericActivitiRestException;
import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.processdefinition.ProcessDefinitionDS;
import nz.co.activiti.tutorial.model.Family;
import nz.co.activiti.tutorial.model.GenericCollectionModel;
import nz.co.activiti.tutorial.model.Identity;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinition;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinitionQueryParameter;
import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;
import nz.co.activiti.tutorial.rest.GeneralModelJSONConverter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

@Service("processDefinitionDSRest")
public class ProcessDefinitionDSRestImpl extends ActivitiRestClientAccessor
		implements ProcessDefinitionDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessDefinitionDSRestImpl.class);

	private DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

	@Resource
	private ProcessDefinitionJSONConverter processDefinitionJSONConverter;

	@Resource
	private GeneralModelJSONConverter generalModelJSONConverter;

	@Override
	public GenericCollectionModel<ProcessDefinition> getProcessDefinitions(
			Map<ProcessDefinitionQueryParameter, String> processDefinitionQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		LOGGER.info("getAllProcessDefinitions start:{}");

		GenericCollectionModel<ProcessDefinition> processDefinitions = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions");

		if (processDefinitionQueryParameters != null) {
			for (Map.Entry<ProcessDefinitionQueryParameter, String> entry : processDefinitionQueryParameters
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

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			processDefinitions = processDefinitionJSONConverter
					.toProcessDefinitions(respStr);

		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(
					"Parameters format is wrong:{}" + respStr);
		} else {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}

		LOGGER.info("getAllProcessDefinitions end:{}");
		return processDefinitions;
	}

	@Override
	public ProcessDefinition getProcessDefinitionByProcessDefinitionId(
			String processDefinitionId) throws Exception {
		LOGGER.info("getProcessDefinitionByProcessDefinitionId start:{}",
				processDefinitionId);
		ProcessDefinition processDefinition = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			processDefinition = processDefinitionJSONConverter
					.toProcessDefinition(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("ProcessDefinition not found by id["
					+ processDefinitionId + "]");
		} else {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}

		LOGGER.info("getProcessDefinitionByProcessDefinitionId end:{}",
				processDefinition);
		return processDefinition;
	}

	@Override
	public ProcessDefinition updateCategory(String processDefinitionId,
			String updateCategory) throws Exception {
		LOGGER.info("updateCategory start:{}");
		LOGGER.info("processDefinitionId:{}", processDefinitionId);
		LOGGER.info("updateCategory:{}", updateCategory);
		ProcessDefinition processDefinition = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId);

		String requestEntity = "{\"category\" : \"" + updateCategory + "\"}";

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, requestEntity);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			processDefinition = processDefinitionJSONConverter
					.toProcessDefinition(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("ProcessDefinition not found by id["
					+ processDefinitionId + "]");
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(
					"Request Body format is wrong:{}" + respStr);
		} else {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}

		LOGGER.info("updateCategory end:{}");
		return processDefinition;
	}

	@Override
	public ProcessDefinition suspendProcessDefinition(
			String processDefinitionId, boolean includeProcessInstances,
			Date effectiveDate) throws Exception {
		LOGGER.info("suspendProcess start:{}", processDefinitionId);
		ProcessDefinition processDefinition = null;

		if (effectiveDate == null) {
			effectiveDate = new Date();
		}

		String effectiveDateStr = dateFormat.format(effectiveDate);

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId);

		String requestEntity = "{\"action\" : \"suspend\",\"includeProcessInstances\" : \""
				+ includeProcessInstances
				+ "\",\"date\" : \""
				+ effectiveDateStr + "\"}";

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, requestEntity);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			processDefinition = processDefinitionJSONConverter
					.toProcessDefinition(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("ProcessDefinition not found by id["
					+ processDefinitionId + "]");
		} else if (statusCode == ClientResponse.Status.CONFLICT) {
			throw new GenericActivitiRestException(
					"process definition is already suspended:{}" + respStr);
		} else {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}

		LOGGER.info("suspendProcess end:{}", processDefinition);
		return processDefinition;
	}

	@Override
	public ProcessDefinition activeProcessDefinition(
			String processDefinitionId, boolean includeProcessInstances,
			Date effectiveDate) throws Exception {
		LOGGER.info("activeProcess start:{}", processDefinitionId);
		ProcessDefinition processDefinition = null;
		if (effectiveDate == null) {
			effectiveDate = new Date();
		}
		String effectiveDateStr = dateFormat.format(effectiveDate);

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId);

		String requestEntity = "{\"action\" : \"activate\",\"includeProcessInstances\" : \""
				+ includeProcessInstances
				+ "\",\"date\" : \""
				+ effectiveDateStr + "\"}";

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, requestEntity);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			processDefinition = processDefinitionJSONConverter
					.toProcessDefinition(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("ProcessDefinition not found by id["
					+ processDefinitionId + "]");
		} else if (statusCode == ClientResponse.Status.CONFLICT) {
			throw new GenericActivitiRestException(
					"process definition is already suspended:{}" + respStr);
		} else {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}

		return processDefinition;
	}

	@Override
	public Set<Identity> getAllIdentities(String processDefinitionId)
			throws Exception {
		LOGGER.info("getAllCandidates start:{}", processDefinitionId);
		Set<Identity> candidates = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId
						+ "/identitylinks");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			candidates = generalModelJSONConverter.toIdentities(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("ProcessDefinition not found by id["
					+ processDefinitionId + "]");
		} else {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}

		LOGGER.info("getAllCandidates end:{}");
		return candidates;
	}

	@Override
	public Identity addIdentity(String processDefinitionId, Family family,
			String name) throws Exception {
		LOGGER.info("addIdentity start:{} ");
		LOGGER.info("family:{} ", family);
		Identity identity = null;
		String requestBody = "{\"" + String.valueOf(family) + "\" : \"" + name
				+ "\"}";

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId
						+ "/identitylinks");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, requestBody);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.CREATED) {
			identity = generalModelJSONConverter.toIdentity(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("ProcessDefinition not found by id["
					+ processDefinitionId + "]");
		} else {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}
		LOGGER.info("addIdentity end:{} ", identity);
		return identity;
	}

	@Override
	public void deleteIdentity(String processDefinitionId, Family family,
			String identityId) throws Exception {
		LOGGER.info("deleteIdentity start:{} ");
		LOGGER.info("family:{} ", family);
		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId
						+ "/identitylinks/" + String.valueOf(family) + "/"
						+ identityId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}
		LOGGER.info("deleteIdentity end:{} ");
	}

	@Override
	public Identity getIdentity(String processDefinitionId, Family family,
			String identityId) throws Exception {
		LOGGER.info("getIdentity start:{} ", processDefinitionId);
		LOGGER.info("family:{} ", family);
		LOGGER.info("identityId:{} ", identityId);
		Identity identity = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId
						+ "/identitylinks/" + String.valueOf(family) + "/"
						+ identityId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			identity = generalModelJSONConverter.toIdentity(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("ProcessDefinition not found by id["
					+ processDefinitionId + "]");
		} else {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}

		LOGGER.info("getIdentity end:{} ", identity);
		return identity;
	}

}
