package nz.co.activiti.tutorial.ds.execution;

import java.util.List;
import java.util.Map;

import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.Variable;
import nz.co.activiti.tutorial.model.VariableScope;
import nz.co.activiti.tutorial.model.execution.Execution;
import nz.co.activiti.tutorial.model.execution.ExecutionActionRequest;
import nz.co.activiti.tutorial.model.execution.ExecutionQueryParameter;
import nz.co.activiti.tutorial.model.execution.Executions;

public interface ExecutionDS {

	Execution getExecutionById(String executionId) throws Exception;

	Execution actionOnExecution(String executionId,
			ExecutionActionRequest executionActionRequest) throws Exception;

	String[] getActiveActivities(String executionId) throws Exception;

	Executions getExecutions(
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
