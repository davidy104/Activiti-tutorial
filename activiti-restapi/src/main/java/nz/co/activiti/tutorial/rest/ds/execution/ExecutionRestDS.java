package nz.co.activiti.tutorial.rest.ds.execution;

import java.util.List;
import java.util.Map;

import nz.co.activiti.tutorial.ds.VariableScope;
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.rest.model.Variable;
import nz.co.activiti.tutorial.rest.model.execution.Execution;
import nz.co.activiti.tutorial.rest.model.execution.ExecutionActionRequest;
import nz.co.activiti.tutorial.rest.model.execution.ExecutionQueryParameter;

public interface ExecutionRestDS {

	Execution getExecutionById(String executionId) throws Exception;

	Execution actionOnExecution(String executionId,
			ExecutionActionRequest executionActionRequest) throws Exception;

	String[] getActiveActivities(String executionId) throws Exception;

	GenericCollectionModel<Execution> getExecutions(
			Map<ExecutionQueryParameter, String> executionQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception;

	List<Variable> getVariablesOnExecution(String executionId,
			VariableScope scope) throws Exception;

	Variable getVariableOnExecution(String executionId, String variableName,
			VariableScope scope) throws Exception;

	List<Variable> createVariablesOnExecution(String executionId,
			List<Variable> addVariables) throws Exception;

	Variable updateVariableOnExecution(String executionId, String variableName,
			Variable updateVariable) throws Exception;
}
