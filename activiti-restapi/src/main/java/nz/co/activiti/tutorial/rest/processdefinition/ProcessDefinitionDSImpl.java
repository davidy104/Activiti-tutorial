package nz.co.activiti.tutorial.rest.processdefinition;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

@Service
public class ProcessDefinitionDSImpl extends ActivitiRestClientAccessor
		implements ProcessDefinitionDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessDefinitionDSImpl.class);

	@Value("${activiti.api.baseurl}")
	private String baseUrl;

	private DateFormat dateFormat = new SimpleDateFormat(
			"yyyy-MM-dd'T'HH:mm:ss'Z'");

	@Resource
	private ProcessDefinitionJSONConverter processDefinitionJSONConverter;

	@Override
	public ProcessDefinitionsResponse getAllProcessDefinitions()
			throws Exception {
		LOGGER.info("getAllProcessDefinitions start:{}");

		ProcessDefinitionsResponse processDefinitionsResponse = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("getAllProcessDefinitions failed:{} " + respStr);
		} else {
			processDefinitionsResponse = processDefinitionJSONConverter
					.toProcessDefinitionsResponse(respStr);
		}

		LOGGER.info("getAllProcessDefinitions end:{}");
		return processDefinitionsResponse;
	}

	@Override
	public ProcessDefinitionResponse getProcessDefinitionByProcessDefinitionId(
			String processDefinitionId) throws Exception {
		LOGGER.info("getProcessDefinitionByProcessDefinitionId start:{}",
				processDefinitionId);
		ProcessDefinitionResponse processDefinitionResponse = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("getProcessDefinitionByProcessDefinitionId ["
					+ processDefinitionId + "] failed:{} " + respStr);
		} else {
			processDefinitionResponse = processDefinitionJSONConverter
					.toProcessDefinitionResponse(respStr);
		}

		LOGGER.info("getProcessDefinitionByProcessDefinitionId end:{}",
				processDefinitionResponse);
		return processDefinitionResponse;
	}

	@Override
	public ProcessDefinitionResponse updateCategory(String processDefinitionId,
			String updateCategory) throws Exception {
		LOGGER.info("updateCategory start:{}");
		LOGGER.info("processDefinitionId:{}", processDefinitionId);
		LOGGER.info("updateCategory:{}", updateCategory);
		ProcessDefinitionResponse processDefinitionResponse = null;

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

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("getProcessDefinitionByProcessDefinitionId ["
					+ processDefinitionId + "] failed:{} " + respStr);
		} else {
			processDefinitionResponse = processDefinitionJSONConverter
					.toProcessDefinitionResponse(respStr);
		}

		LOGGER.info("updateCategory end:{}");
		return processDefinitionResponse;
	}

	@Override
	public ProcessDefinitionResponse suspendProcess(String processDefinitionId,
			Date effectiveDate) throws Exception {
		LOGGER.info("suspendProcess start:{}", processDefinitionId);
		ProcessDefinitionResponse processDefinitionResponse = null;

		if (effectiveDate == null) {
			effectiveDate = new Date();
		}

		String effectiveDateStr = dateFormat.format(effectiveDate);

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId);

		String requestEntity = "{\"action\" : \"suspend\",\"includeProcessInstances\" : \"false\",\"date\" : \""
				+ effectiveDateStr + "\"}";

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, requestEntity);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("suspendProcess [" + processDefinitionId
					+ "] failed:{} " + respStr);
		} else {
			processDefinitionResponse = processDefinitionJSONConverter
					.toProcessDefinitionResponse(respStr);
		}

		LOGGER.info("suspendProcess end:{}", processDefinitionResponse);
		return processDefinitionResponse;
	}

	@Override
	public ProcessDefinitionResponse activeProcess(String processDefinitionId,
			Date effectiveDate) throws Exception {
		LOGGER.info("activeProcess start:{}", processDefinitionId);
		ProcessDefinitionResponse processDefinitionResponse = null;
		if (effectiveDate == null) {
			effectiveDate = new Date();
		}
		String effectiveDateStr = dateFormat.format(effectiveDate);

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId);

		String requestEntity = "{\"action\" : \"activate\",\"includeProcessInstances\" : \"true\",\"date\" : \""
				+ effectiveDateStr + "\"}";

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, requestEntity);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("activeProcess [" + processDefinitionId
					+ "] failed:{} " + respStr);
		} else {
			processDefinitionResponse = processDefinitionJSONConverter
					.toProcessDefinitionResponse(respStr);
		}

		return processDefinitionResponse;
	}

	@Override
	public Set<Candidate> getAllCandidates(String processDefinitionId)
			throws Exception {
		LOGGER.info("getAllCandidates start:{}", processDefinitionId);
		Set<Candidate> candidates = null;
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

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("getAllCandidates [" + processDefinitionId
					+ "] failed:{} " + respStr);
		} else {
			candidates = processDefinitionJSONConverter.toCandidates(respStr);
		}

		LOGGER.info("getAllCandidates end:{}");
		return candidates;
	}

	@Override
	public Candidate addCandidate(String processDefinitionId, String family,
			String name) throws Exception {
		LOGGER.info("addCandidate start:{} ");
		LOGGER.info("family:{} ", family);
		Candidate candidate = null;
		String requestBody = null;
		if (family.equals("users")) {
			requestBody = "{\"user\" : \"" + name + "\"}";
		} else if (family.equals("groups")) {
			requestBody = "{\"group\" : \"" + name + "\"}";
		} else {
			throw new Exception("family can not be identified.");
		}

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

		if (statusCode != ClientResponse.Status.CREATED) {
			throw new Exception("addCandidate [" + processDefinitionId
					+ "] failed:{} " + respStr);
		} else {
			candidate = processDefinitionJSONConverter.toCandidate(respStr);
		}
		LOGGER.info("addCandidate end:{} ", candidate);
		return candidate;
	}

	@Override
	public void deleteCandidate(String processDefinitionId, String family,
			String identityId) throws Exception {
		LOGGER.info("deleteCandidate start:{} ");
		LOGGER.info("family:{} ", family);
		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId
						+ "/identitylinks/" + family + "/" + identityId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("deleteCandidate [" + processDefinitionId
					+ "] failed:{} " + respStr);
		}
		LOGGER.info("deleteCandidate end:{} ");
	}

	@Override
	public Candidate getCandidate(String processDefinitionId, String family,
			String identityId) throws Exception {
		LOGGER.info("getCandidate start:{} ", processDefinitionId);
		LOGGER.info("family:{} ", family);
		LOGGER.info("identityId:{} ", identityId);
		Candidate candidate = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/process-definitions/" + processDefinitionId
						+ "/identitylinks/" + family + "/" + identityId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("getCandidate [" + processDefinitionId
					+ "] failed:{} " + respStr);
		} else {
			candidate = processDefinitionJSONConverter.toCandidate(respStr);
		}

		LOGGER.info("getCandidate end:{} ", candidate);
		return candidate;
	}

}
