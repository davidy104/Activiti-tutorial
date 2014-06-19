package nz.co.activiti.tutorial.ds.history;

import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableUpdate;

public interface HistoricDS {

	HistoricProcessInstance getHistoricProcessInstanceById(
			String processInstanceId) throws Exception;

	HistoricProcessInstance getHistoricProcessInstance(String bizKey,
			String processDefinitionId) throws Exception;

	void deleteHistoricProcessInstance(String processInstanceId)
			throws Exception;

	List<HistoricIdentityLink> getHistoricProcessInstanceIdentities(
			String processInstanceId) throws Exception;

	HistoricTaskInstance getHistoricTaskInstance(String processInstanceId,
			String taskId) throws Exception;

	void deleteHistoricTaskInstance(String taskId) throws Exception;

	List<HistoricIdentityLink> getHistoricTaskInstanceIdentities(String taskId)
			throws Exception;

	List<HistoricActivityInstance> getHistoricActivityInstances(
			String processInstanceId, String processDefinitionId)
			throws Exception;

	List<HistoricDetail> getHistoricDetails(String processInstanceId)
			throws Exception;

	List<HistoricVariableUpdate> getHistoricVariablesOnProcess(
			String processInstanceId) throws Exception;

	HistoricVariableUpdate getHistoricVariableOnProcess(
			String processInstanceId, String variableName) throws Exception;

}
