package nz.co.activiti.tutorial.rest.ds.processinstance;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.GenericActivitiRestException;
import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.IdentityType;
import nz.co.activiti.tutorial.rest.ActionType;
import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;
import nz.co.activiti.tutorial.rest.GeneralModelJSONConverter;
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.Identity;
import nz.co.activiti.tutorial.rest.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.rest.model.Variable;
import nz.co.activiti.tutorial.rest.model.processinstance.ProcessInstance;
import nz.co.activiti.tutorial.rest.model.processinstance.ProcessInstanceQueryParameter;

import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

@Service
public class ProcessInstanceDSRestImpl extends ActivitiRestClientAccessor
		implements ProcessInstanceRestDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessInstanceDSRestImpl.class);

	@Resource
	private ProcessInstanceJSONConverter processInstanceJSONConverter;

	@Resource
	private GeneralModelJSONConverter generalModelJSONConverter;

	@Override
	public ProcessInstance startProcessByProcessDefinitionId(
			String processDefinitionId, String businessKey,
			Map<String, Object> variables) throws Exception {
		LOGGER.info("startProcessByProcessDefinitionId start:{}",
				processDefinitionId);
		LOGGER.info("businessKey:{}", businessKey);
		ProcessInstance processInstance = null;
		String requestBody = this.processInstanceJSONConverter
				.toStartProcessInstanceByIdJson(processDefinitionId,
						businessKey, variables);

		LOGGER.info("requestBody:{} ", requestBody);

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, requestBody);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.CREATED) {
			processInstance = processInstanceJSONConverter
					.toProcessInstance(respStr);
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknown exception:{} " + respStr);
		}

		LOGGER.info("startProcessByProcessDefinitionId end:{}", processInstance);
		return processInstance;
	}

	@Override
	public ProcessInstance startProcessByProcessDefinitionKey(
			String processDefinitionKey, String businessKey,
			Map<String, Object> variables) throws Exception {
		LOGGER.info("startProcessByProcessDefinitionKey start:{}",
				processDefinitionKey);
		LOGGER.info("businessKey:{}", businessKey);
		ProcessInstance processInstance = null;
		String requestBody = this.processInstanceJSONConverter
				.toStartProcessInstanceByKeyJson(processDefinitionKey,
						businessKey, variables);

		LOGGER.info("requestBody:{} ", requestBody);

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, requestBody);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.CREATED) {
			processInstance = processInstanceJSONConverter
					.toProcessInstance(respStr);
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknown exception:{} " + respStr);
		}

		LOGGER.info("startProcessByProcessDefinitionKey end:{}",
				processInstance);
		return processInstance;
	}

	@Override
	public void getLegacyProcessInstance(String processInstanceId)
			throws Exception {
		WebResource webResource = client.resource(baseUrl).path(
				"/process-instance/" + processInstanceId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
	}

	@Override
	public ProcessInstance getProcessInstance(String processInstanceId)
			throws Exception {
		LOGGER.info("getProcessInstance start:{} ", processInstanceId);
		ProcessInstance processInstance = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances/" + processInstanceId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			processInstance = processInstanceJSONConverter
					.toProcessInstance(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("ProcessInstance not found by id["
					+ processInstanceId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getProcessInstance end:{} ", processInstance);
		return processInstance;
	}

	@Override
	public void deleteProcessInstance(String processInstanceId)
			throws Exception {
		LOGGER.info("deleteProcessInstance start:{}", processInstanceId);
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances/" + processInstanceId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("ProcessInstance not found by Id["
					+ processInstanceId + "]");
		} else if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknown exception:{}" + respStr);
		}
	}

	@Override
	public ProcessInstance suspendProcessInstance(String processInstanceId)
			throws Exception {
		LOGGER.info("suspendProcessInstance start:{} ", processInstanceId);
		ProcessInstance processInstance = null;
		String jsonEntity = "{\"action\":\"suspend\"}";
		LOGGER.info("jsonEntity:{} ", jsonEntity);
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances/" + processInstanceId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, jsonEntity);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			processInstance = processInstanceJSONConverter
					.toProcessInstance(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("processInstance not found by id["
					+ processInstanceId + "]");
		} else if (statusCode == ClientResponse.Status.CONFLICT) {
			throw new GenericActivitiRestException(
					"the requested process instance action cannot be executed since the process-instance is already suspended.");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("suspendProcessInstance end:{} ");
		return processInstance;
	}

	@Override
	public ProcessInstance activeProcessInstance(String processInstanceId)
			throws Exception {
		LOGGER.info("activeProcessInstance start:{} ", processInstanceId);
		ProcessInstance processInstance = null;
		String jsonEntity = "{\"action\":\"activate\"}";
		LOGGER.info("jsonEntity:{} ", jsonEntity);
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances/" + processInstanceId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, jsonEntity);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			processInstance = processInstanceJSONConverter
					.toProcessInstance(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("processInstance not found by id["
					+ processInstanceId + "]");
		} else if (statusCode == ClientResponse.Status.CONFLICT) {
			throw new GenericActivitiRestException(
					"the requested process instance action cannot be executed since the process-instance is already active.");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("activeProcessInstance end:{} ");
		return processInstance;
	}

	@Override
	public GenericCollectionModel<ProcessInstance> getProcessInstances(
			Map<ProcessInstanceQueryParameter, String> processInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		LOGGER.info("getProcessInstances start:{}");
		GenericCollectionModel<ProcessInstance> processInstances = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances");

		if (processInstanceQueryParameters != null) {
			for (Map.Entry<ProcessInstanceQueryParameter, String> entry : processInstanceQueryParameters
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
			throw new Exception("getGroups failed:{} " + respStr);
		} else {
			processInstances = processInstanceJSONConverter
					.toProcessInstances(respStr);
		}
		LOGGER.info("getProcessInstances end:{}", processInstances);
		return processInstances;
	}

	@Override
	public Set<Identity> getInvolvedPeopleForProcessInstance(
			String processInstanceId) throws Exception {
		LOGGER.info("getInvolvedPeopleForProcessInstance start:{}",
				processInstanceId);
		Set<Identity> identities = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances/" + processInstanceId
						+ "/identitylinks");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			identities = this.generalModelJSONConverter.toIdentities(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("processInstance not found by id["
					+ processInstanceId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getInvolvedPeopleForProcessInstance end:{}");
		return identities;
	}

	@Override
	public Identity addInvolvedPeopleToProcess(String processInstanceId,
			String userId, IdentityType identityType) throws Exception {
		LOGGER.info("addInvolvedPeopleToProcess start:{} ", processInstanceId);
		LOGGER.info("userId:{} ", userId);
		LOGGER.info("identityType:{} ", identityType);
		Identity addedIdentity = null;
		String jsonRequest = "{\"userId\":\"" + userId + "\",\"type\":\""
				+ identityType.name() + "\"}";
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances/" + processInstanceId
						+ "/identitylinks");
		LOGGER.info("jsonRequest:{} ", jsonRequest);
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonRequest);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			addedIdentity = this.generalModelJSONConverter.toIdentity(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("processInstance not found by id["
					+ processInstanceId + "]");
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(
					"the requested body did not contain a userId or a type");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}

		return addedIdentity;
	}

	@Override
	public void removeInvolvedPeopleFromProcess(String processInstanceId,
			String userId, IdentityType identityType) throws Exception {
		LOGGER.info("removeInvolvedPeopleFromProcess start:{} ",
				processInstanceId);
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances/" + processInstanceId
						+ "/identitylinks/users/" + userId + "/"
						+ identityType.name());

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("processInstance not found by id["
					+ processInstanceId + "]");
		} else if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("removeInvolvedPeopleFromProcess end:{} ");
	}

	@Override
	public List<Variable> getVariablesFromProcess(String processInstanceId)
			throws Exception {
		LOGGER.info("getVariablesFromProcess start:{} ", processInstanceId);
		List<Variable> variables = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances/" + processInstanceId
						+ "/variables");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			variables = this.generalModelJSONConverter.toVariables(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("processInstance not found by id["
					+ processInstanceId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getVariablesFromProcess end:{} ");
		return variables;
	}

	@Override
	public Variable getVariableFromProcess(String processInstanceId,
			String variableName) throws Exception {
		LOGGER.info("getVariableFromProcess start:{} ", processInstanceId);
		Variable variable = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances/" + processInstanceId
						+ "/variables/" + variableName);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			variable = this.generalModelJSONConverter.toVariable(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("processInstance not found by id["
					+ processInstanceId + "]");
		} else if (statusCode != ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(
					"the request body is incomplete or contains illegal value");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getVariableFromProcess end:{} ", variable);
		return variable;
	}

	@Override
	public List<Variable> createVariablesForProcess(String processInstanceId,
			List<Variable> addVariables) throws Exception {
		LOGGER.info("createVariablesForProcess start:{} ", processInstanceId);
		List<Variable> variables = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances/" + processInstanceId
						+ "/variables");

		String requestJson = this.generalModelJSONConverter
				.toVariablesJson(addVariables);
		LOGGER.info("requestJson:{} ", requestJson);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, requestJson);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.CREATED) {
			variables = this.generalModelJSONConverter.toVariables(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("processInstance not found by id["
					+ processInstanceId + "]");
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST
				|| statusCode == ClientResponse.Status.CONFLICT) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("createVariablesForProcess end:{} ");
		return variables;
	}

	@Override
	public Variable updateVariableForProcess(String processInstanceId,
			String variableName, Variable updateVariable) throws Exception {
		LOGGER.info("updateVariableForProcess start:{} ", processInstanceId);
		Variable udpatedVariable = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances/" + processInstanceId
						+ "/variables/" + variableName);

		String requestJson = this.generalModelJSONConverter.toVariableJson(
				updateVariable, ActionType.update);
		LOGGER.info("requestJson:{} ", requestJson);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, requestJson);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			udpatedVariable = this.generalModelJSONConverter
					.toVariable(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("processInstance not found by id["
					+ processInstanceId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("updateVariableForProcess end:{} ", udpatedVariable);
		return udpatedVariable;
	}

}
