package nz.co.activiti.tutorial.ds.history;

import java.util.Map;

import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.history.HistoricProcessInstance;
import nz.co.activiti.tutorial.model.history.HistoricProcessInstanceQueryParameter;
import nz.co.activiti.tutorial.model.history.HistoricProcessInstances;

public interface HistoricDS {

	HistoricProcessInstance getHistoricProcessInstanceById(
			String processInstanceId) throws Exception;

	HistoricProcessInstances getHistoricProcessInstances(
			Map<HistoricProcessInstanceQueryParameter, String> historicProcessInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception;

	void deleteHistoricProcessInstance(String processInstanceId)
			throws Exception;
}
