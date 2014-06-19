package nz.co.activiti.tutorial.ds.processinstance;

import java.util.List;
import java.util.Map;

import nz.co.activiti.tutorial.ds.IdentityType;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;

public interface ProcessInstanceDS {

	ProcessInstance startProcessByProcessDefinitionId(
			String processDefinitionId, String businessKey,
			Map<String, Object> variables) throws Exception;

	ProcessInstance startProcessByProcessDefinitionKey(
			String processDefinitionKey, String businessKey,
			Map<String, Object> variables) throws Exception;

	ProcessInstance getProcessInstance(String processInstanceId)
			throws Exception;

	ProcessInstance getProcessInstance(String businessKey,
			String processDefinitionId) throws Exception;

	void deleteProcessInstance(String processInstanceId, String deleteReason)
			throws Exception;

	void suspendProcessInstance(String processInstanceId) throws Exception;

	void activeProcessInstance(String processInstanceId) throws Exception;

	List<IdentityLink> getInvolvedPeopleForProcessInstance(
			String processInstanceId) throws Exception;

	void addInvolvedPeopleToProcess(String processInstanceId, String user,
			IdentityType identityType) throws Exception;

}
