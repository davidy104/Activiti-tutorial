package nz.co.activiti.tutorial.ds.task;

import java.util.List;
import java.util.Map;

import nz.co.activiti.tutorial.ds.Family;
import nz.co.activiti.tutorial.ds.IdentityType;
import nz.co.activiti.tutorial.ds.TaskAction;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Event;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;

/**
 * Basic operation on Task service
 * 
 * @author dyuan
 * 
 */
public interface TaskDS {

	Task getTaskById(String taskId) throws Exception;

	Task getTask(String taskName, String businessKey) throws Exception;

	void updateTask(String taskId, TaskEntity updateTask) throws Exception;

	void actionOnTask(String taskId, TaskAction action, String userId,
			Map<String, Object> variables) throws Exception;

	void deleteTask(String taskId, boolean cascadeHistory) throws Exception;

	Map<String, Object> getVariablesOnTask(String taskId, boolean localScope)
			throws Exception;

	Object getVariableOnTask(String taskId, String variableName,
			boolean localScope) throws Exception;

	void createVariablesOnTask(String taskId, Map<String, Object> addVariables,
			boolean localScope) throws Exception;

	void updateVariableOnTask(String taskId, String variableName,
			Object updateVariable, boolean localScope) throws Exception;

	void deleteVariableOnTask(String taskId, String variableName,
			boolean localScope) throws Exception;

	List<IdentityLink> getAllIdentities(String taskId) throws Exception;

	IdentityLink getIdentity(String taskId, String identityId, Family family,
			IdentityType identityType) throws Exception;

	void addIdentity(String taskId, String identityId, Family family,
			IdentityType identityType) throws Exception;

	void deleteIdentity(String taskId, String identityId, Family family,
			IdentityType identityType) throws Exception;

	Comment createCommentOnTask(String taskId, String processInstanceId,
			String message) throws Exception;

	List<Comment> getAllCommentsOnTask(String taskId) throws Exception;

	Comment getCommentOnTask(String taskId, String commentId) throws Exception;

	void deleteCommentOnTask(String taskId, String commentId) throws Exception;

	List<Event> getAllEventsOnTask(String taskId) throws Exception;

	Event getEventOnTask(String taskId, String eventId) throws Exception;

	boolean checkIfTaskExisted(String taskId);

}
