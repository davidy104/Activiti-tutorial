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

	void suspendProcessInstance(String processInstanceId) throws Exception;

	void activeProcessInstance(String processInstanceId) throws Exception;

	ProcessInstances getProcessInstances(
			Map<PagingAndSortingParameters, Object> pagingAndSortingParameters)
			throws Exception;

	ProcessInstances getProcessInstances(
			Map<ProcessInstanceQueryParameters, Object> processInstanceQueryParameters,
			Map<PagingAndSortingParameters, Object> pagingAndSortingParameters)
			throws Exception;

	Set<Party> getInvolvedPeopleForProcessInstance(String processInstanceId)
			throws Exception;

	Party addInvolvedPeopleToProcess(String processInstanceId, String user,
			IdentityLinkType identityType) throws Exception;

	void removeInvolvedPeopleFromProcess(String processInstanceId, String user,
			IdentityLinkType identityType) throws Exception;

	List<Variable> getVariablesFromProcess(String processInstanceId)
			throws Exception;

	Variable getVariableFromProcess(String processInstanceId,
			String variableName) throws Exception;

	List<Variable> createVariablesForProcess(String processInstanceId,
			List<Variable> addVariables) throws Exception;

	List<Variable> updateVariablesForProcess(String processInstanceId,
			List<Variable> updateVariables) throws Exception;

	Variable updateVariableForProcess(String processInstanceId,
			Variable updateVariable) throws Exception;

}
