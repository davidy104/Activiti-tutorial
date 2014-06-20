package nz.co.activiti.tutorial.ds.execution;

import java.util.Map;

import nz.co.activiti.tutorial.NotFoundException;

import org.activiti.engine.runtime.Execution;

public interface ExecutionDS {

	Execution getExecutionById(String executionId,
			String processInstanceBusinessKey);

	void signal(String executionId, Map<String, Object> variables)
			throws NotFoundException;

	Map<String, Object> getVariablesOnExecution(String executionId)
			throws NotFoundException;

	Object getVariableOnExecution(String executionId, String variableName)
			throws NotFoundException;

	void createVariablesOnExecution(String executionId,
			Map<String, Object> addVariables) throws NotFoundException;

	void createOrUpdateVariableOnExecution(String executionId,
			String variableName, Object updateVariable)
			throws NotFoundException;

	void removeVariable(String executionId, String variableName)
			throws NotFoundException;

	Execution getExecution(String processInstanceId, String businessKey);
}
