package nz.co.activiti.tutorial.rest.ds.history;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;
import nz.co.activiti.tutorial.rest.GenericActivitiRestException;
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.rest.model.history.HistoricActivityInstance;
import nz.co.activiti.tutorial.rest.model.history.HistoricActivityInstanceQueryParameter;
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessIdentity;
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessInstance;
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessInstanceComment;
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessInstanceQueryParameter;
import nz.co.activiti.tutorial.rest.model.history.HistoricTaskInstance;
import nz.co.activiti.tutorial.rest.model.history.HistoricTaskInstanceQueryParameter;
import nz.co.activiti.tutorial.rest.model.history.HistoricVariableInstance;
import nz.co.activiti.tutorial.rest.model.history.HistoricVariableInstanceQueryParameter;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

@Service
public class HistoricRestDSImpl extends ActivitiRestClientAccessor implements
		HistoricRestDS {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HistoricRestDSImpl.class);

	@Resource
	private HistoricJSONConverter historicJSONConverter;

	@Override
	public GenericCollectionModel<HistoricActivityInstance> getHistoricActivityInstances(
			Map<HistoricActivityInstanceQueryParameter, String> historicProcessInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		LOGGER.info("getHistoricActivityInstances start:{}");
		GenericCollectionModel<HistoricActivityInstance> historicActivityInstances = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-activity-instances");

		if (historicProcessInstanceQueryParameters != null) {
			for (Map.Entry<HistoricActivityInstanceQueryParameter, String> entry : historicProcessInstanceQueryParameters
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

		if (statusCode == ClientResponse.Status.OK) {
			historicActivityInstances = this.historicJSONConverter
					.toHistoricActivityInstances(respStr);
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknown exception:{}" + respStr);
		}
		LOGGER.info("getHistoricActivityInstances end:{}",
				historicActivityInstances);
		return historicActivityInstances;
	}

	@Override
	public HistoricProcessInstance getHistoricProcessInstanceById(
			String processInstanceId) throws Exception {
		LOGGER.info("getHistoricProcessInstanceById start:{}",
				processInstanceId);
		HistoricProcessInstance historicProcessInstance = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-process-instances/" + processInstanceId);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			historicProcessInstance = historicJSONConverter
					.toHistoricProcessInstance(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException(respStr);
		} else {
			throw new GenericActivitiRestException("Unknown exception:{}"
					+ respStr);
		}

		LOGGER.info("getHistoricProcessInstanceById end:{}",
				historicProcessInstance);
		return historicProcessInstance;
	}

	@Override
	public GenericCollectionModel<HistoricProcessInstance> getHistoricProcessInstances(
			Map<HistoricProcessInstanceQueryParameter, String> historicProcessInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		LOGGER.info("getHistoricProcessInstances start:{}");
		GenericCollectionModel<HistoricProcessInstance> historicProcessInstances = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-process-instances");

		if (historicProcessInstanceQueryParameters != null) {
			for (Map.Entry<HistoricProcessInstanceQueryParameter, String> entry : historicProcessInstanceQueryParameters
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

		if (statusCode == ClientResponse.Status.OK) {
			historicProcessInstances = historicJSONConverter
					.toHistoricProcessInstances(respStr);
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknown exception:{}" + respStr);
		}
		LOGGER.info("getHistoricProcessInstances end:{}",
				historicProcessInstances);
		return historicProcessInstances;
	}

	@Override
	public void deleteHistoricProcessInstance(String processInstanceId)
			throws Exception {
		LOGGER.info("deleteHistoricProcessInstance start:{}", processInstanceId);
		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-process-instances/" + processInstanceId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException(respStr);
		} else if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("Unknown exception:{}" + respStr);
		}
	}

	@Override
	public List<HistoricProcessIdentity> getHistoricProcessInstanceIdentities(
			String processInstanceId) throws Exception {
		LOGGER.info("getHistoricProcessInstanceIdentities start:{} ",
				processInstanceId);
		List<HistoricProcessIdentity> HistoricProcessIdentities = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-process-instances/" + processInstanceId
						+ "/identitylinks");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
		if (statusCode == ClientResponse.Status.OK) {
			HistoricProcessIdentities = this.historicJSONConverter
					.toHistoricProcessIdentities(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException(respStr);
		} else {
			throw new Exception("Unknown exception:{}" + respStr);
		}
		LOGGER.info("getHistoricProcessInstanceIdentities end:{} ");
		return HistoricProcessIdentities;
	}

	@Override
	public List<HistoricProcessInstanceComment> getHistoricProcessInstanceComments(
			String processInstanceId) throws Exception {
		LOGGER.info("getHistoricProcessInstanceComments start:{} ",
				processInstanceId);
		List<HistoricProcessInstanceComment> historicProcessInstanceComments = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-process-instances/" + processInstanceId
						+ "/comments");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);
		if (statusCode == ClientResponse.Status.OK) {
			historicProcessInstanceComments = this.historicJSONConverter
					.toHistoricProcessInstanceComments(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException(respStr);
		} else {
			throw new Exception("Unknown exception:{}" + respStr);
		}
		LOGGER.info("getHistoricProcessInstanceComments end:{} ");
		return historicProcessInstanceComments;
	}

	@Override
	public HistoricProcessInstanceComment createHistoricProcessInstanceComment(
			String processInstanceId, String message) throws Exception {
		HistoricProcessInstanceComment historicProcessInstanceComment = null;
		LOGGER.info("createHistoricProcessInstanceComment start:{} ", message);
		String jsonRequest = "{\"message\" : \"" + message + "\"}";

		LOGGER.info("jsonRequest:{} ", jsonRequest);
		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-process-instances/" + processInstanceId
						+ "/comments");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonRequest);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.CREATED) {
			historicProcessInstanceComment = this.historicJSONConverter
					.toHistoricProcessInstanceComment(respStr);
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException(respStr);
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("createHistoricProcessInstanceComment end:{} ",
				historicProcessInstanceComment);
		return historicProcessInstanceComment;
	}

	@Override
	public HistoricProcessInstanceComment getHistoricProcessInstanceComment(
			String processInstanceId, String commentId) throws Exception {
		LOGGER.info("getHistoricProcessInstanceComment start:{}",
				processInstanceId);
		LOGGER.info("commentId:{}", commentId);
		HistoricProcessInstanceComment historicProcessInstanceComment = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-process-instances/" + processInstanceId
						+ "/comments/" + commentId);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			historicProcessInstanceComment = historicJSONConverter
					.toHistoricProcessInstanceComment(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException(respStr);
		} else {
			throw new Exception("Unknown exception:{}" + respStr);
		}

		LOGGER.info("getHistoricProcessInstanceComment end:{}",
				historicProcessInstanceComment);
		return historicProcessInstanceComment;
	}

	@Override
	public void deleteHistoricProcessInstanceComment(String processInstanceId,
			String commentId) throws Exception {
		LOGGER.info("deleteHistoricProcessInstanceComment start:{}",
				processInstanceId);
		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-process-instances/" + processInstanceId
						+ "/comments/" + commentId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException(respStr);
		} else if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknown exception:{}" + respStr);
		}
	}

	@Override
	public HistoricTaskInstance getHistoricTaskInstance(String taskId)
			throws Exception {
		LOGGER.info("getHistoricTaskInstance start:{}", taskId);
		HistoricTaskInstance historicTaskInstance = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-task-instances/" + taskId);

		ClientResponse response = webResource.type(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON).get(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			historicTaskInstance = historicJSONConverter
					.toHistoricTaskInstance(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException(respStr);
		} else {
			throw new Exception("Unknown exception:{}" + respStr);
		}

		LOGGER.info("getHistoricTaskInstance end:{}", historicTaskInstance);
		return historicTaskInstance;
	}

	@Override
	public GenericCollectionModel<HistoricTaskInstance> getHistoricTaskInstances(
			Map<HistoricTaskInstanceQueryParameter, String> historicTaskInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		LOGGER.info("getHistoricTaskInstances start:{}");
		GenericCollectionModel<HistoricTaskInstance> historicTaskInstances = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-task-instances");

		if (historicTaskInstanceQueryParameters != null) {
			for (Map.Entry<HistoricTaskInstanceQueryParameter, String> entry : historicTaskInstanceQueryParameters
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

		if (statusCode == ClientResponse.Status.OK) {
			historicTaskInstances = this.historicJSONConverter
					.toHistoricTaskInstances(respStr);
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknown exception:{}" + respStr);
		}
		LOGGER.info("getHistoricTaskInstances end:{}", historicTaskInstances);
		return historicTaskInstances;
	}

	@Override
	public void deleteHistoricTaskInstance(String taskId) throws Exception {
		LOGGER.info("deleteHistoricTaskInstance start:{}", taskId);
		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-task-instances/" + taskId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.delete(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException(respStr);
		} else if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("Unknown exception:{}" + respStr);
		}
	}

	@Override
	public List<HistoricProcessIdentity> getHistoricTaskInstanceIdentities(
			String taskId) throws Exception {

		return null;
	}

	@Override
	public GenericCollectionModel<HistoricVariableInstance> getHistoricVariableInstances(
			Map<HistoricVariableInstanceQueryParameter, String> historicTaskInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		LOGGER.info("getHistoricVariableInstances start:{}");
		GenericCollectionModel<HistoricVariableInstance> historicVariableInstances = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/history/historic-variable-instances");

		if (historicTaskInstanceQueryParameters != null) {
			for (Map.Entry<HistoricVariableInstanceQueryParameter, String> entry : historicTaskInstanceQueryParameters
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

		if (statusCode == ClientResponse.Status.OK) {
			historicVariableInstances = this.historicJSONConverter
					.toHistoricVariableInstances(respStr);
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else {
			throw new Exception("Unknown exception:{}" + respStr);
		}
		LOGGER.info("getHistoricVariableInstances end:{}",
				historicVariableInstances);
		return historicVariableInstances;
	}

}
