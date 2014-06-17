package nz.co.activiti.tutorial.ds.history;

import java.util.List;
import java.util.Set;

import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;

public interface HistoricDS {

	HistoricProcessInstance getHistoricProcessInstanceById(
			String processInstanceId) throws Exception;

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

	

	void deleteHistoricTaskInstance(String taskId) throws Exception;

	Set<HistoricProcessIdentity> getHistoricTaskInstanceIdentities(String taskId)
			throws Exception;

	
}
