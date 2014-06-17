package nz.co.activiti.tutorial.rest.execution;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.GenericActivitiRestException;
import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.execution.ExecutionDS;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.Variable;
import nz.co.activiti.tutorial.model.VariableScope;
import nz.co.activiti.tutorial.model.execution.Execution;
import nz.co.activiti.tutorial.model.execution.ExecutionActionRequest;
import nz.co.activiti.tutorial.model.execution.ExecutionQueryParameter;
import nz.co.activiti.tutorial.model.execution.Executions;
import nz.co.activiti.tutorial.rest.ActionType;
import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;
import nz.co.activiti.tutorial.rest.GeneralModelJSONConverter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

@Service("executionDSRest")
public class ExecutionDSRestImpl extends ActivitiRestClientAccessor implements
		ExecutionDS {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExecutionDSRestImpl.class);

	@Resource
	private ExecutionJSONConverter executionJSONConverter;

	@Resource
	private GeneralModelJSONConverter generalModelJSONConverter;
	
	

	@Override
	public Execution getExecutionById(String executionId) throws Exception {
		LOGGER.info("getExecutionById start:{}", executionId);
		Execution execution = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/executions/" + executionId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			execution = executionJSONConverter.toExecution(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("execution not found by id["
					+ executionId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getExecutionById end:{}", execution);
		return execution;
	}

	@Override
	public Execution actionOnExecution(String executionId,
			ExecutionActionRequest executionActionRequest) throws Exception {
		LOGGER.info("actionOnExecution start:{}", executionId);
		LOGGER.info("executionActionRequest:{}", executionActionRequest);
		Execution execution = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/executions/" + executionId);

		String requestJson = executionJSONConverter
				.toExecutionActionRequestJson(executionActionRequest);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, requestJson);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			execution = executionJSONConverter.toExecution(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("execution not found by id["
					+ executionId + "]");
		} else if (statusCode == ClientResponse.Status.NO_CONTENT
				|| statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getExecutionById end:{}", execution);
		return execution;
	}

	@Override
	public String[] getActiveActivities(String executionId) throws Exception {
		LOGGER.info("getActiveActivities start:{}", executionId);

		String[] activities = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/executions/" + executionId + "/activities");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			activities = executionJSONConverter.toActiveActivities(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("execution not found by id["
					+ executionId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getActiveActivities end:{}", activities);
		return activities;
	}

	@Override
	public Executions getExecutions(
			Map<ExecutionQueryParameter, String> executionQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		LOGGER.info("getExecutions start:{}");
		Executions executions = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/repository/executions");

		if (executionQueryParameters != null) {
			for (Map.Entry<ExecutionQueryParameter, String> entry : executionQueryParameters
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
			throw new Exception(respStr);
		} else {
			executions = this.executionJSONConverter.toExecutions(respStr);
		}
		LOGGER.info("getExecutions end:{}", executions);
		return executions;
	}

	@Override
	public List<Variable> getVariablesOnExecution(String executionId,
			VariableScope scope) throws Exception {
		LOGGER.info("getVariablesOnExecution start:{} ", executionId);
		List<Variable> variables = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/executions/" + executionId + "/variables");

		if (scope != null) {
			webResource = webResource.queryParam("scope", scope.name());
		} else {
			webResource = webResource.queryParam("scope", "local");
		}

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
			throw new NotFoundException("execution not found by id["
					+ executionId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getVariablesFromProcess end:{} ");
		return variables;
	}

	@Override
	public Variable getVariableOnExecution(String executionId,
			String variableName, VariableScope scope) throws Exception {
		LOGGER.info("getVariableOnExecution start:{} ", executionId);
		Variable variable = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/executions/" + executionId + "/variables/"
						+ variableName);

		if (scope != null) {
			webResource = webResource.queryParam("scope", scope.name());
		} else {
			webResource = webResource.queryParam("scope", "local");
		}

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
			throw new NotFoundException("execution not found by id["
					+ executionId + "]");
		} else if (statusCode != ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getVariableOnExecution end:{} ", variable);
		return variable;
	}

	@Override
	public List<Variable> createVariablesOnExecution(String executionId,
			List<Variable> addVariables) throws Exception {
		LOGGER.info("createVariablesOnExecution start:{} ", executionId);
		List<Variable> variables = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/executions/" + executionId + "/variables");

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
			throw new NotFoundException("execution not found by id["
					+ executionId + "]");
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST
				|| statusCode == ClientResponse.Status.CONFLICT) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("createVariablesOnExecution end:{} ");
		return variables;
	}

	@Override
	public Variable updateVariableOnExecution(String executionId,
			String variableName, Variable updateVariable) throws Exception {
		LOGGER.info("updateVariableOnExecution start:{} ", executionId);
		Variable udpatedVariable = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/executions/" + executionId + "/variables/"
						+ variableName);

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
			throw new NotFoundException("execution not found by id["
					+ executionId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("updateVariableOnExecution end:{} ", udpatedVariable);
		return udpatedVariable;
	}

}
