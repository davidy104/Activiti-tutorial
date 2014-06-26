package nz.co.activiti.tutorial.rest.ds.task;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.Family;
import nz.co.activiti.tutorial.ds.IdentityType;
import nz.co.activiti.tutorial.ds.TaskAction;
import nz.co.activiti.tutorial.ds.VariableScope;
import nz.co.activiti.tutorial.rest.ActionType;
import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;
import nz.co.activiti.tutorial.rest.GeneralModelJSONConverter;
import nz.co.activiti.tutorial.rest.GenericActivitiRestException;
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.Identity;
import nz.co.activiti.tutorial.rest.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.rest.model.Variable;
import nz.co.activiti.tutorial.rest.model.task.Task;
import nz.co.activiti.tutorial.rest.model.task.TaskActionRequest;
import nz.co.activiti.tutorial.rest.model.task.TaskComment;
import nz.co.activiti.tutorial.rest.model.task.TaskEvent;
import nz.co.activiti.tutorial.rest.model.task.TaskQueryParameter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

@Service
public class TaskRestDSImpl extends ActivitiRestClientAccessor implements
		TaskRestDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(TaskRestDSImpl.class);

	@Resource
	private TaskJSONConverter taskJSONConverter;

	@Resource
	private GeneralModelJSONConverter generalModelJSONConverter;

	@Override
	public Task getTaskById(String taskId) throws Exception {
		LOGGER.info("getTaskById start:{}", taskId);
		Task task = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			task = taskJSONConverter.toTask(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getTaskById end:{}", task);
		return task;
	}

	@Override
	public GenericCollectionModel<Task> getTasks(
			Map<TaskQueryParameter, String> taskQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		LOGGER.info("getTasks start:{}");
		GenericCollectionModel<Task> tasks = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks");

		if (taskQueryParameters != null) {
			for (Map.Entry<TaskQueryParameter, String> entry : taskQueryParameters
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
			tasks = this.taskJSONConverter.toTasks(respStr);
		}
		LOGGER.info("getTasks end:{}", tasks);
		return tasks;
	}

	@Override
	public Task updateTask(String taskId, Task updateTask) throws Exception {
		LOGGER.info("updateTask start:{} ", taskId);
		Task udpatedTask = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId);

		String requestJson = this.taskJSONConverter
				.toUpdateTaskRequestJson(updateTask);

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
			udpatedTask = taskJSONConverter.toTask(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else if (statusCode == ClientResponse.Status.CONFLICT) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("updateTask end:{} ", udpatedTask);
		return udpatedTask;
	}

	@Override
	public Task actionOnTask(String taskId, TaskActionRequest taskActionRequest)
			throws Exception {
		LOGGER.info("actionOnTask start:{} ", taskId);
		LOGGER.info("taskActionRequest:{} ", taskActionRequest);
		Task task = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId);

		String requestJson = this.taskJSONConverter
				.toTaskActionRequestJson(taskActionRequest);
		LOGGER.info("requestJson:{} ", requestJson);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, requestJson);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			task = taskJSONConverter.toTask(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else if (statusCode == ClientResponse.Status.CONFLICT
				|| statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("actionOnTask end:{} ", task);
		return task;
	}

	@Override
	public void deleteTask(String taskId, boolean cascadeHistory,
			boolean deleteReason) throws Exception {
		LOGGER.info("deleteTask start:{}", taskId);

		WebResource webResource = client.resource(baseUrl)
				.path("/runtime/tasks/" + taskId)
				.queryParam("cascadeHistory", String.valueOf(cascadeHistory))
				.queryParam("deleteReason", String.valueOf(deleteReason));

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.FORBIDDEN) {
			throw new GenericActivitiRestException(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("deleteTask end:{}");
	}

	@Override
	public List<Variable> getVariablesOnTask(String taskId, VariableScope scope)
			throws Exception {
		LOGGER.info("getVariablesOnTask start:{} ", taskId);
		List<Variable> variables = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/variables");

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
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getVariablesOnTask end:{} ");
		return variables;
	}

	@Override
	public Variable getVariableOnTask(String taskId, String variableName,
			VariableScope scope) throws Exception {
		LOGGER.info("getVariableOnTask start:{} ", taskId);
		Variable variable = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/variables/" + variableName);

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
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else if (statusCode != ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getVariableOnTask end:{} ", variable);
		return variable;
	}

	@Override
	public List<Variable> createVariablesOnTask(String taskId,
			List<Variable> addVariables) throws Exception {
		LOGGER.info("createVariablesOnTask start:{} ", taskId);
		List<Variable> variables = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/variables");

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
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST
				|| statusCode == ClientResponse.Status.CONFLICT) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("createVariablesOnTask end:{} ");
		return variables;
	}

	@Override
	public Variable updateVariableOnTask(String taskId, String variableName,
			Variable updateVariable) throws Exception {
		LOGGER.info("updateVariableOnTask start:{} ", taskId);
		Variable udpatedVariable = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/variables/" + variableName);

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
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("updateVariableOnTask end:{} ", udpatedVariable);
		return udpatedVariable;
	}

	@Override
	public void deleteVariableOnTask(String taskId, String variableName,
			VariableScope scope) throws Exception {
		LOGGER.info("deleteVariableOnTask start:{} ", taskId);

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/variables/" + variableName);

		if (scope != null) {
			webResource = webResource.queryParam("scope", scope.name());
		} else {
			webResource = webResource.queryParam("scope", "local");
		}

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("deleteVariableOnTask end:{} ");

	}

	@Override
	public Set<Identity> getAllIdentities(String taskId) throws Exception {
		LOGGER.info("getAllIdentities start:{}", taskId);
		Set<Identity> identities = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/identitylinks");

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
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getAllIdentities end:{}", identities);
		return identities;
	}

	@Override
	public Set<Identity> getIdentities(String taskId, Family family)
			throws Exception {
		LOGGER.info("getIdentities start:{}", taskId);
		Set<Identity> identities = null;
		WebResource webResource = client.resource(baseUrl)
				.path("/runtime/tasks/" + taskId + "/identitylinks")
				.path(family.name());

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
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getIdentities end:{}", identities);
		return identities;
	}

	@Override
	public Identity getIdentity(String taskId, Family family,
			String identityId, IdentityType identityType) throws Exception {
		LOGGER.info("getIdentity start:{} ", taskId);
		LOGGER.info("family:{} ", family);
		LOGGER.info("identityId:{} ", identityId);
		Identity identity = null;

		WebResource webResource = client
				.resource(baseUrl)
				.path("/runtime/tasks/" + taskId + "/identitylinks/"
						+ String.valueOf(family)).path(identityId)
				.path(identityType.name());

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
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}

		LOGGER.info("getIdentity end:{} ", identity);
		return identity;
	}

	@Override
	public Identity addIdentity(String taskId, Family family,
			String identityId, IdentityType identityType) throws Exception {
		LOGGER.info("addIdentity start:{} ", taskId);
		LOGGER.info("identityId:{} ", identityId);
		LOGGER.info("identityType:{} ", identityType);
		Identity addedIdentity = null;
		String jsonRequest = null;
		if (family == Family.users) {
			jsonRequest = "{\"userId\":\"" + identityId + "\",\"type\":\""
					+ identityType.name() + "\"}";
		} else {
			jsonRequest = "{\"groupId\":\"" + identityId + "\",\"type\":\""
					+ identityType.name() + "\"}";
		}

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/identitylinks");
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
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}

		return addedIdentity;
	}

	@Override
	public void deleteIdentity(String taskId, Family family, String identityId,
			IdentityType identityType) throws Exception {
		LOGGER.info("deleteIdentity start:{} ", taskId);
		WebResource webResource = client.resource(baseUrl)
				.path("/runtime/tasks/" + taskId + "/identitylinks")
				.path(family.name()).path(identityId).path(identityType.name());

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("deleteIdentity end:{} ");
	}

	@Override
	public TaskComment createCommentOnTask(String taskId, String message)
			throws Exception {
		LOGGER.info("createCommentOnTask start:{} ", taskId);
		LOGGER.info("message:{} ", message);
		TaskComment taskComment = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/comments");

		String requestJson = "{\"message\" : \"" + message + "\"}";
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
			taskComment = taskJSONConverter.toTaskComment(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("createCommentOnTask end:{} ", taskComment);
		return taskComment;
	}

	@Override
	public List<TaskComment> getAllCommentsOnTask(String taskId)
			throws Exception {
		LOGGER.info("getAllCommentsOnTask start:{}", taskId);
		List<TaskComment> taskComments = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/comments");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			taskComments = this.taskJSONConverter.toTaskComments(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getAllCommentsOnTask end:{}", taskComments);
		return taskComments;
	}

	@Override
	public TaskComment getCommentOnTask(String taskId, String commentId)
			throws Exception {
		LOGGER.info("getCommentOnTask start:{} ", taskId);
		LOGGER.info("commentId:{} ", commentId);
		TaskComment taskComment = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/comments/" + commentId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			taskComment = this.taskJSONConverter.toTaskComment(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}

		LOGGER.info("getCommentOnTask end:{} ", taskComment);
		return taskComment;
	}

	@Override
	public void deleteCommentOnTask(String taskId, String commentId)
			throws Exception {
		LOGGER.info("deleteCommentOnTask start:{} ", taskId);
		LOGGER.info("commentId:{} ", commentId);

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/comments/" + commentId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}

		LOGGER.info("deleteCommentOnTask end:{} ");
	}

	@Override
	public List<TaskEvent> getAllEventsOnTask(String taskId) throws Exception {
		LOGGER.info("getAllEventsOnTask start:{}", taskId);
		List<TaskEvent> taskEvents = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/events");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			taskEvents = this.taskJSONConverter.toTaskEvents(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getAllEventsOnTask end:{}", taskEvents);
		return taskEvents;
	}

	@Override
	public TaskEvent getEventOnTask(String taskId, String eventId)
			throws Exception {
		LOGGER.info("getCommentOnTask start:{} ", taskId);
		LOGGER.info("eventId:{} ", eventId);
		TaskEvent taskEvent = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/tasks/" + taskId + "/events/" + eventId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			taskEvent = this.taskJSONConverter.toTaskEvent(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("task not found by id[" + taskId + "]");
		} else {
			throw new Exception("Unknown excepiton:{} " + respStr);
		}

		LOGGER.info("getEventOnTask end:{} ", taskEvent);
		return taskEvent;
	}

	@Override
	public void actionOnTask(String taskId, TaskAction taskAction,
			Map<String, Object> variables) throws Exception {
		LOGGER.info("actionOnTask start:{} ", taskId);

		WebResource webResource = client.resource(baseUrl).path(
				"/task/" + taskId + "/" + taskAction.name());
		String requestJson = null;

		if (variables != null) {
			requestJson = this.taskJSONConverter.toTaskVariablesJson(variables);
		}
		LOGGER.info("requestJson:{} ", requestJson);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, requestJson);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

	}

}
