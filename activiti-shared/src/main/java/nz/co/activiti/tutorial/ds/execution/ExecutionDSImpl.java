package nz.co.activiti.tutorial.ds.execution;

import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.NotFoundException;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ExecutionQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ExecutionDSImpl implements ExecutionDS {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ExecutionDSImpl.class);

	@Resource
	private RuntimeService runtimeService;

	@Override
	public Execution getExecutionById(String executionId,
			String processInstanceBusinessKey) throws Exception {
		LOGGER.info("getExecutionById start:{} ", executionId);
		LOGGER.info("processInstanceBusinessKey:{} ",
				processInstanceBusinessKey);
		Execution execution = null;
		ExecutionQuery executionQuery = runtimeService.createExecutionQuery()
				.executionId(executionId);

		if (!StringUtils.isEmpty(processInstanceBusinessKey)) {
			executionQuery = executionQuery.processInstanceBusinessKey(
					processInstanceBusinessKey, true);
		}
		execution = executionQuery.singleResult();

		if (execution == null) {
			throw new NotFoundException("Execution not found by id["
					+ executionId + "]");
		}
		LOGGER.info("getExecutionById end:{} ");
		return execution;
	}

	@Override
	public void signal(String executionId, Map<String, Object> variables)
			throws Exception {
		try {
			runtimeService.signal(executionId, variables);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException("Execution not found by id["
					+ executionId + "]");
		}
	}

	@Override
	public Map<String, Object> getVariablesOnExecution(String executionId)
			throws Exception {
		try {
			return runtimeService.getVariables(executionId);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException("Execution not found by id["
					+ executionId + "]");
		}
	}

	@Override
	public Object getVariableOnExecution(String executionId, String variableName)
			throws Exception {
		try {
			return runtimeService.getVariable(executionId, variableName);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException("Execution not found by id["
					+ executionId + "]");
		}
	}

	@Override
	public void createVariablesOnExecution(String executionId,
			Map<String, Object> variables) throws Exception {
		try {
			runtimeService.setVariables(executionId, variables);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException("Execution not found by id["
					+ executionId + "]");
		}
	}

	@Override
	public void createOrUpdateVariableOnExecution(String executionId,
			String variableName, Object updateVariable) throws Exception {
		try {
			runtimeService.setVariable(executionId, variableName,
					updateVariable);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException("Execution not found by id["
					+ executionId + "]");
		}
	}

	@Override
	public void removeVariable(String executionId, String variableName)
			throws Exception {
		runtimeService.removeVariable(executionId, variableName);
	}

}
