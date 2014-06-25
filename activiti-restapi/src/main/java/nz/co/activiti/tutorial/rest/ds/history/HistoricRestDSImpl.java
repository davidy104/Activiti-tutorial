package nz.co.activiti.tutorial.rest.ds.history;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

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
		return null;
	}

	@Override
	public HistoricProcessInstance getHistoricProcessInstanceById(
			String processInstanceId) throws Exception {

		return null;
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

	}

	@Override
	public List<HistoricProcessIdentity> getHistoricProcessInstanceIdentities(
			String processInstanceId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<HistoricProcessInstanceComment> getHistoricProcessInstanceComments(
			String processInstanceId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HistoricProcessInstanceComment createHistoricProcessInstanceComment(
			String processInstanceId, String message) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HistoricProcessInstanceComment getHistoricProcessInstanceComment(
			String processInstanceId, String commentId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteHistoricProcessInstanceComment(String processInstanceId,
			String commentId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public HistoricTaskInstance getHistoricTaskInstance(
			String processInstanceId, String taskId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericCollectionModel<HistoricTaskInstance> getHistoricTaskInstances(
			Map<HistoricTaskInstanceQueryParameter, String> historicTaskInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteHistoricTaskInstance(String taskId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<HistoricProcessIdentity> getHistoricTaskInstanceIdentities(
			String taskId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public GenericCollectionModel<HistoricVariableInstance> getHistoricVariableInstances(
			Map<HistoricVariableInstanceQueryParameter, String> historicTaskInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
