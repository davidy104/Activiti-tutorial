package nz.co.activiti.tutorial.ds.history;

import java.util.Set;

import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;

public interface HistoricDS {

	HistoricProcessInstance getHistoricProcessInstanceById(
			String processInstanceId) throws Exception;

	void deleteHistoricProcessInstance(String processInstanceId)
			throws Exception;

	Set<HistoricIdentityLink> getHistoricProcessInstanceIdentities(
			String processInstanceId) throws Exception;

	void deleteHistoricProcessInstanceComment(String processInstanceId,
			String commentId) throws Exception;

	HistoricTaskInstance getHistoricTaskInstance(String processInstanceId,
			String taskId) throws Exception;

	void deleteHistoricTaskInstance(String taskId) throws Exception;

	Set<HistoricIdentityLink> getHistoricTaskInstanceIdentities(String taskId)
			throws Exception;

}
