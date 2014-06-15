package nz.co.activiti.tutorial.ds.processinstance;

import java.util.List;
import java.util.Map;
import java.util.Set;

import nz.co.activiti.tutorial.model.PagingAndSortingParameters;
import nz.co.activiti.tutorial.model.Party;
import nz.co.activiti.tutorial.model.Variable;
import nz.co.activiti.tutorial.model.processinstance.ProcessInstance;
import nz.co.activiti.tutorial.model.processinstance.ProcessInstanceQueryParameters;
import nz.co.activiti.tutorial.model.processinstance.ProcessInstances;

import org.activiti.engine.task.IdentityLinkType;

public interface ProcessInstanceDS {

	ProcessInstance startProcessByProcessDefinitionId(
			String processDefinitionId, String businessKey,
			Map<String, Object> variables) throws Exception;

	ProcessInstance startProcessByProcessDefinitionKey(
			String processDefinitionKey, String businessKey,
			Map<String, Object> variables) throws Exception;

	ProcessInstance getProcessInstance(String processInstanceId)
			throws Exception;

	void deleteProcessInstance(String processInstanceId) throws Exception;

	ProcessInstance suspendProcessInstance(String processInstanceId)
			throws Exception;

	ProcessInstance activeProcessInstance(String processInstanceId)
			throws Exception;

	ProcessInstances getProcessInstances(
			Map<ProcessInstanceQueryParameters, String> processInstanceQueryParameters,
			Map<PagingAndSortingParameters, String> pagingAndSortingParameters)
			throws Exception;

	Set<Party> getInvolvedPeopleForProcessInstance(String processInstanceId)
			throws Exception;

	/**
	 * 
	 * @param processInstanceId
	 * @param user
	 * @param identityType
	 *            see@IdentityLinkType
	 *            ["assignee","candidate","owner","participant","starter"]
	 * @return
	 * @throws Exception
	 */
	Party addInvolvedPeopleToProcess(String processInstanceId, String user,
			String identityType) throws Exception;

	/**
	 * 
	 * @param processInstanceId
	 * @param user
	 * @param identityType
	 *            see@IdentityLinkType
	 * @throws Exception
	 */
	void removeInvolvedPeopleFromProcess(String processInstanceId, String user,
			String identityType) throws Exception;

	List<Variable> getVariablesFromProcess(String processInstanceId)
			throws Exception;

	Variable getVariableFromProcess(String processInstanceId,
			String variableName) throws Exception;

	List<Variable> createVariablesForProcess(String processInstanceId,
			List<Variable> addVariables) throws Exception;

	Variable updateVariableForProcess(String processInstanceId,
			String variableName, Variable updateVariable) throws Exception;

}
