package nz.co.activiti.tutorial.ds.task;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.Family;
import nz.co.activiti.tutorial.ds.IdentityType;
import nz.co.activiti.tutorial.ds.TaskAction;

import org.activiti.engine.TaskService;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Event;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TaskDSImpl implements TaskDS {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(TaskDSImpl.class);

	@Resource
	private TaskService taskService;

	@Override
	public Task getTaskById(String taskId) throws Exception {
		return taskService.createTaskQuery().taskId(taskId).singleResult();
	}

	@Override
	public Task getTask(String taskName, String businessKey) throws Exception {
		return taskService.createTaskQuery().taskName(taskName)
				.processInstanceBusinessKey(businessKey).singleResult();
	}

	@Override
	public void updateTask(String taskId, TaskEntity updateTask)
			throws Exception {
		LOGGER.info("updateTask start:{}", taskId);
		Task existedTask = this.getTaskById(taskId);
		if (updateTask.getParentTaskId() != null) {
			if (!this.checkIfTaskExisted(updateTask.getParentTaskId())) {
				throw new NotFoundException("Task not found by id["
						+ updateTask.getParentTaskId() + "]");
			}
			existedTask.setParentTaskId(updateTask.getParentTaskId());
		}

		if (!StringUtils.isEmpty(updateTask.getAssignee())) {
			existedTask.setAssignee(updateTask.getAssignee());
		}
		if (updateTask.getDelegationState() != null) {
			existedTask.setDelegationState(updateTask.getDelegationState());
		}

		if (!StringUtils.isEmpty(updateTask.getDescription())) {
			existedTask.setDescription(updateTask.getDescription());
		}

		if (updateTask.getDueDate() != null) {
			existedTask.setDueDate(updateTask.getDueDate());
		}

		if (!StringUtils.isEmpty(updateTask.getOwner())) {
			existedTask.setOwner(updateTask.getOwner());
		}

		if (!StringUtils.isEmpty(updateTask.getName())) {
			existedTask.setName(updateTask.getName());
		}
		taskService.saveTask(existedTask);
		LOGGER.info("updateTask end:{}");
	}

	@Override
	public void actionOnTask(String taskId, TaskAction action, String userId,
			Map<String, Object> variables) throws Exception {

		switch (action) {
		case complete:
			taskService.complete(taskId, variables);
			break;
		case claim:
			if (StringUtils.isEmpty(userId)) {
				throw new IllegalArgumentException("User id is madatory");
			}
			taskService.claim(taskId, userId);
			break;
		case delegate:
			if (StringUtils.isEmpty(userId)) {
				throw new IllegalArgumentException("User id is madatory");
			}
			taskService.delegateTask(taskId, userId);
			break;
		case resolve:
			taskService.resolveTask(taskId, variables);
			break;
		}
	}

	@Override
	public void deleteTask(String taskId, boolean cascadeHistory)
			throws Exception {
		taskService.deleteTask(taskId, cascadeHistory);
	}

	@Override
	public Map<String, Object> getVariablesOnTask(String taskId,
			boolean localScope) throws Exception {
		LOGGER.info("getVariablesOnTask start:{}", taskId);
		LOGGER.info("localScope:{}", localScope);
		Map<String, Object> foundVariables = null;
		if (localScope) {
			foundVariables = taskService.getVariablesLocal(taskId);
		} else {
			foundVariables = taskService.getVariables(taskId);
		}
		LOGGER.info("getVariablesOnTask end:{}");
		return foundVariables;
	}

	@Override
	public Object getVariableOnTask(String taskId, String variableName,
			boolean localScope) throws Exception {
		if (localScope) {
			return taskService.getVariableLocal(taskId, variableName);
		} else {
			return taskService.getVariable(taskId, variableName);
		}
	}

	@Override
	public void createVariablesOnTask(String taskId,
			Map<String, Object> addVariables, boolean localScope)
			throws Exception {
		if (localScope) {
			taskService.setVariablesLocal(taskId, addVariables);
		} else {
			taskService.setVariables(taskId, addVariables);
		}
	}

	@Override
	public void updateVariableOnTask(String taskId, String variableName,
			Object updateVariable, boolean localScope) throws Exception {
		if (localScope) {
			taskService.setVariableLocal(taskId, variableName, updateVariable);
		} else {
			taskService.setVariable(taskId, variableName, updateVariable);
		}
	}

	@Override
	public void deleteVariableOnTask(String taskId, String variableName,
			boolean localScope) throws Exception {
		if (localScope) {
			taskService.removeVariableLocal(taskId, variableName);
		} else {
			taskService.removeVariable(taskId, variableName);
		}
	}

	@Override
	public List<IdentityLink> getAllIdentities(String taskId) throws Exception {
		return taskService.getIdentityLinksForTask(taskId);
	}

	@Override
	public IdentityLink getIdentity(String taskId, String identityId,
			Family family, IdentityType identityType) throws Exception {
		List<IdentityLink> identities = this.getAllIdentities(taskId);
		if (identities != null) {
			for (IdentityLink identity : identities) {
				if (family != null) {
					if (family == Family.groups) {
						if (StringUtils.isEmpty(identity.getGroupId())) {
							continue;
						}
					} else if (family == Family.users) {
						if (StringUtils.isEmpty(identity.getUserId())) {
							continue;
						}
					} else {
						throw new IllegalArgumentException(
								"family type can not be identified");
					}
				}

				if (identityType != null
						&& !identity.getType().equalsIgnoreCase(
								identityType.name())) {
					continue;
				}
				return identity;
			}
		}
		return null;
	}

	@Override
	public void addIdentity(String taskId, String identityId, Family family,
			IdentityType identityType) throws Exception {
		if (family == Family.groups) {
			taskService.addGroupIdentityLink(taskId, identityId,
					identityType.name());
		} else if (family == Family.users) {
			taskService.addUserIdentityLink(taskId, identityId,
					identityType.name());
		} else {
			throw new IllegalArgumentException(
					"family type can not be identified");
		}
	}

	@Override
	public void deleteIdentity(String taskId, String identityId, Family family,
			IdentityType identityType) throws Exception {
		if (family == Family.groups) {
			taskService.deleteGroupIdentityLink(taskId, identityId,
					identityType.name());
		} else if (family == Family.users) {
			taskService.deleteUserIdentityLink(taskId, identityId,
					identityType.name());
		} else {
			throw new IllegalArgumentException(
					"family type can not be identified");
		}

	}

	@Override
	public Comment createCommentOnTask(String taskId, String processInstanceId,
			String message) throws Exception {

		return null;
	}

	@Override
	public List<Comment> getAllCommentsOnTask(String taskId) throws Exception {

		return null;
	}

	@Override
	public Comment getCommentOnTask(String taskId, String commentId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteCommentOnTask(String taskId, String commentId)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Event> getAllEventsOnTask(String taskId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Event getEventOnTask(String taskId, String eventId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean checkIfTaskExisted(String taskId) {
		long count = taskService.createTaskQuery().taskId(taskId).count();
		if (count == 0) {
			return false;
		}
		return true;
	}

}
