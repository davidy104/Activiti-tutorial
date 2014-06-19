package nz.co.activiti.tutorial.ds.processinstance;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.IdentityType;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessInstanceDSImpl implements ProcessInstanceDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessInstanceDSImpl.class);
	@Resource
	private RuntimeService runtimeService;

	@Override
	public ProcessInstance startProcessByProcessDefinitionId(
			String processDefinitionId, String businessKey,
			Map<String, Object> variables) throws Exception {
		return runtimeService.startProcessInstanceById(processDefinitionId,
				businessKey, variables);
	}

	@Override
	public ProcessInstance startProcessByProcessDefinitionKey(
			String processDefinitionKey, String businessKey,
			Map<String, Object> variables) throws Exception {
		return runtimeService.startProcessInstanceByKey(processDefinitionKey,
				businessKey, variables);
	}

	@Override
	public ProcessInstance getProcessInstance(String processInstanceId)
			throws Exception {
		return runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
	}

	@Override
	public ProcessInstance getProcessInstance(String businessKey,
			String processDefinitionId) throws Exception {
		return runtimeService.createProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey)
				.processDefinitionId(processDefinitionId).singleResult();
	}

	@Override
	public void deleteProcessInstance(String processInstanceId,
			String deleteReason) throws Exception {
		runtimeService.deleteProcessInstance(processInstanceId, deleteReason);
	}

	@Override
	public void suspendProcessInstance(String processInstanceId)
			throws Exception {
		runtimeService.suspendProcessInstanceById(processInstanceId);
	}

	@Override
	public void activeProcessInstance(String processInstanceId)
			throws Exception {
		runtimeService.activateProcessInstanceById(processInstanceId);
	}

	@Override
	public List<IdentityLink> getInvolvedPeopleForProcessInstance(
			String processInstanceId) throws Exception {
		return runtimeService
				.getIdentityLinksForProcessInstance(processInstanceId);
	}

	@Override
	public void addInvolvedPeopleToProcess(String processInstanceId,
			String userId, IdentityType identityType) throws Exception {
		runtimeService.addUserIdentityLink(processInstanceId, userId,
				identityType.name());
	}

}
