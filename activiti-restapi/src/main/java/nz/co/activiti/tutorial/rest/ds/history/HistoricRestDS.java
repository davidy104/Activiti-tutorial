package nz.co.activiti.tutorial.rest.ds.history;

import java.util.List;
import java.util.Map;
import java.util.Set;

import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.rest.model.history.HistoricDetail;
import nz.co.activiti.tutorial.rest.model.history.HistoricDetailQueryParameter;
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessIdentity;
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessInstance;
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessInstanceComment;
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessInstanceQueryParameter;
import nz.co.activiti.tutorial.rest.model.history.HistoricTaskInstance;
import nz.co.activiti.tutorial.rest.model.history.HistoricTaskInstanceQueryParameter;
import nz.co.activiti.tutorial.rest.model.history.HistoricVariableInstance;
import nz.co.activiti.tutorial.rest.model.history.HistoricVariableInstanceQueryParameter;

public interface HistoricRestDS {

	HistoricProcessInstance getHistoricProcessInstanceById(
			String processInstanceId) throws Exception;

	GenericCollectionModel<HistoricProcessInstance> getHistoricProcessInstances(
			Map<HistoricProcessInstanceQueryParameter, String> historicProcessInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception;

	void deleteHistoricProcessInstance(String processInstanceId)
			throws Exception;

	Set<HistoricProcessIdentity> getHistoricProcessInstanceIdentities(
			String processInstanceId) throws Exception;

	List<HistoricProcessInstanceComment> getHistoricProcessInstanceComments(
			String processInstanceId) throws Exception;

	HistoricProcessInstanceComment createHistoricProcessInstanceComment(
			String processInstanceId, String message) throws Exception;

	HistoricProcessInstanceComment getHistoricProcessInstanceComment(
			String processInstanceId, String commentId) throws Exception;

	void deleteHistoricProcessInstanceComment(String processInstanceId,
			String commentId) throws Exception;

	HistoricTaskInstance getHistoricTaskInstance(String processInstanceId,
			String taskId) throws Exception;

	GenericCollectionModel<HistoricTaskInstance> getHistoricTaskInstances(
			Map<HistoricTaskInstanceQueryParameter, String> historicTaskInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception;

	void deleteHistoricTaskInstance(String taskId) throws Exception;

	Set<HistoricProcessIdentity> getHistoricTaskInstanceIdentities(String taskId)
			throws Exception;

	GenericCollectionModel<HistoricVariableInstance> getHistoricVariableInstances(
			Map<HistoricVariableInstanceQueryParameter, String> historicTaskInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception;

	GenericCollectionModel<HistoricDetail> getHistoricDetails(
			Map<HistoricDetailQueryParameter, String> historicTaskInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception;
}
