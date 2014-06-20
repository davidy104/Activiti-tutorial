package nz.co.activiti.tutorial.ds.history;

import java.util.List;

import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;

public interface HistoricDS {

	HistoricProcessInstance getHistoricProcessInstanceById(
			String processInstanceId);

	HistoricProcessInstance getHistoricProcessInstance(String businessKey,
			String processDefinitionId);

	boolean checkIfHistoricProcessInstanceExisted(String processInstanceId);

	void deleteHistoricProcessInstance(String processInstanceId);

	List<HistoricIdentityLink> getHistoricProcessInstanceIdentities(
			String processInstanceId);

	List<HistoricTaskInstance> getHistoricTaskInstances(String processInstanceId);

	HistoricTaskInstance getHistoricTaskInstance(String processInstanceId,
			String taskId);

	void deleteHistoricTaskInstance(String taskId);

	List<HistoricIdentityLink> getHistoricTaskInstanceIdentities(String taskId)
			throws Exception;

	List<HistoricActivityInstance> getHistoricActivityInstances(
			String processInstanceId, String processDefinitionId);

	List<HistoricDetail> getHistoricDetails(String processInstanceId);

	List<Object> getHistoricVariablesOnProcess(String processInstanceId);

	Object getHistoricVariableOnProcess(String processInstanceId,
			String variableName);

}
