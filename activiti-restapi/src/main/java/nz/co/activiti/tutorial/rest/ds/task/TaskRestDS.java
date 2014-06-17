package nz.co.activiti.tutorial.rest.ds.task;

import java.util.List;
import java.util.Map;
import java.util.Set;

import nz.co.activiti.tutorial.rest.model.Family;
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.Identity;
import nz.co.activiti.tutorial.rest.model.IdentityType;
import nz.co.activiti.tutorial.rest.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.rest.model.Variable;
import nz.co.activiti.tutorial.rest.model.VariableScope;
import nz.co.activiti.tutorial.rest.model.task.Task;
import nz.co.activiti.tutorial.rest.model.task.TaskActionRequest;
import nz.co.activiti.tutorial.rest.model.task.TaskComment;
import nz.co.activiti.tutorial.rest.model.task.TaskEvent;
import nz.co.activiti.tutorial.rest.model.task.TaskQueryParameter;

public interface TaskRestDS {

	Task getTaskById(String taskId) throws Exception;

	GenericCollectionModel<Task> getTasks(
			Map<TaskQueryParameter, String> taskQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception;

	Task updateTask(String taskId, Task updateTask) throws Exception;

	Task actionOnTask(String taskId, TaskActionRequest taskActionRequest)
			throws Exception;

	void deleteTask(String taskId, boolean cascadeHistory, boolean deleteReason)
			throws Exception;

	List<Variable> getVariablesOnTask(String taskId, VariableScope scope)
			throws Exception;

	Variable getVariableOnTask(String taskId, String variableName,
			VariableScope scope) throws Exception;

	List<Variable> createVariablesOnTask(String taskId,
			List<Variable> addVariables) throws Exception;

	Variable updateVariableOnTask(String taskId, String variableName,
			Variable updateVariable) throws Exception;

	void deleteVariableOnTask(String taskId, String variableName,
			VariableScope scope) throws Exception;

	Set<Identity> getAllIdentities(String taskId) throws Exception;

	Set<Identity> getIdentities(String taskId, Family family) throws Exception;

	Identity getIdentity(String taskId, Family family, String identityId,
			IdentityType identityType) throws Exception;

	Identity addIdentity(String taskId, Family family, String identityId,
			IdentityType identityType) throws Exception;

	void deleteIdentity(String taskId, Family family, String identityId,
			IdentityType identityType) throws Exception;

	TaskComment createCommentOnTask(String taskId, String message)
			throws Exception;

	List<TaskComment> getAllCommentsOnTask(String taskId) throws Exception;

	TaskComment getCommentOnTask(String taskId, String commentId)
			throws Exception;

	void deleteCommentOnTask(String taskId, String commentId) throws Exception;

	List<TaskEvent> getAllEventsOnTask(String taskId) throws Exception;

	TaskEvent getEventOnTask(String taskId, String eventId) throws Exception;

}
