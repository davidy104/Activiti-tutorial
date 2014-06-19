package nz.co.activiti.tutorial.ds.execution;

import java.util.Map;

import org.activiti.engine.runtime.Execution;

public interface ExecutionDS {

	Execution getExecutionById(String executionId,
			String processInstanceBusinessKey) throws Exception;

	void signal(String executionId, Map<String, Object> variables)
			throws Exception;

	Map<String, Object> getVariablesOnExecution(String executionId)
			throws Exception;

	Object getVariableOnExecution(String executionId, String variableName)
			throws Exception;

	void createVariablesOnExecution(String executionId,
			Map<String, Object> addVariables) throws Exception;

	void createOrUpdateVariableOnExecution(String executionId, String variableName,
			Object updateVariable) throws Exception;

	void removeVariable(String executionId, String variableName)
			throws Exception;
}
