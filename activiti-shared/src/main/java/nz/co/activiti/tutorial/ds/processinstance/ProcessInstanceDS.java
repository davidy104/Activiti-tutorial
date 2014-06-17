package nz.co.activiti.tutorial.ds.processinstance;

import java.util.List;
import java.util.Map;
import java.util.Set;

import nz.co.activiti.tutorial.model.GenericCollectionModel;
import nz.co.activiti.tutorial.model.Identity;
import nz.co.activiti.tutorial.model.IdentityType;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.Variable;
import nz.co.activiti.tutorial.model.processinstance.ProcessInstance;
import nz.co.activiti.tutorial.model.processinstance.ProcessInstanceQueryParameter;

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

	GenericCollectionModel<ProcessInstance> getProcessInstances(
			Map<ProcessInstanceQueryParameter, String> processInstanceQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception;

	Set<Identity> getInvolvedPeopleForProcessInstance(String processInstanceId)
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
	Identity addInvolvedPeopleToProcess(String processInstanceId, String user,
			IdentityType identityType) throws Exception;

	/**
	 * 
	 * @param processInstanceId
	 * @param user
	 * @param identityType
	 *            see@IdentityLinkType
	 * @throws Exception
	 */
	void removeInvolvedPeopleFromProcess(String processInstanceId, String user,
			IdentityType identityType) throws Exception;

	List<Variable> getVariablesFromProcess(String processInstanceId)
			throws Exception;

	Variable getVariableFromProcess(String processInstanceId,
			String variableName) throws Exception;

	List<Variable> createVariablesForProcess(String processInstanceId,
			List<Variable> addVariables) throws Exception;

	Variable updateVariableForProcess(String processInstanceId,
			String variableName, Variable updateVariable) throws Exception;

}
