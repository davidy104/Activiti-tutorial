package nz.co.activiti.tutorial.ds.processinstance;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.DuplicatedException;
import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.IdentityType;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
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
			Map<String, Object> variables) throws NotFoundException {
		try {
			return runtimeService.startProcessInstanceById(processDefinitionId,
					businessKey, variables);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException(e);
		}
	}

	@Override
	public ProcessInstance startProcessByProcessDefinitionKey(
			String processDefinitionKey, String businessKey,
			Map<String, Object> variables) throws NotFoundException {
		try {
			return runtimeService.startProcessInstanceByKey(
					processDefinitionKey, businessKey, variables);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException(e);
		}
	}

	@Override
	public ProcessInstance getProcessInstance(String processInstanceId) {
		LOGGER.info("getProcessInstance start:{} ", processInstanceId);
		ProcessInstance processInstance = null;
		processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		LOGGER.info("getProcessInstance end:{} ");
		return processInstance;
	}

	@Override
	public ProcessInstance getProcessInstance(String businessKey,
			String processDefinitionId) {
		ProcessInstance processInstance = null;
		processInstance = runtimeService.createProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey)
				.processDefinitionId(processDefinitionId).singleResult();
		return processInstance;
	}

	@Override
	public void deleteProcessInstance(String processInstanceId,
			String deleteReason) throws NotFoundException {
		try {
			runtimeService.deleteProcessInstance(processInstanceId,
					deleteReason);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException(e);
		}
	}

	@Override
	public void suspendProcessInstance(String processInstanceId)
			throws Exception {
		try {
			runtimeService.suspendProcessInstanceById(processInstanceId);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException(e);
		} catch (ActivitiException e1) {
			throw new DuplicatedException(e1);
		}
	}

	@Override
	public void activeProcessInstance(String processInstanceId)
			throws Exception {
		try {
			runtimeService.activateProcessInstanceById(processInstanceId);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException(e);
		} catch (ActivitiException e1) {
			throw new DuplicatedException(e1);
		}
	}

	@Override
	public List<IdentityLink> getInvolvedPeopleForProcessInstance(
			String processInstanceId) throws Exception {
		return runtimeService
				.getIdentityLinksForProcessInstance(processInstanceId);
	}

	@Override
	public void addInvolvedPeopleToProcess(String processInstanceId,
			String userId, IdentityType identityType) throws NotFoundException {
		try {
			runtimeService.addUserIdentityLink(processInstanceId, userId,
					identityType.name());
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException(e);
		}
	}

}
