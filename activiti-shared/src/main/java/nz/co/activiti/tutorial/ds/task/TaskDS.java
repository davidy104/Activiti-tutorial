package nz.co.activiti.tutorial.ds.task;

import java.util.List;
import java.util.Map;

import nz.co.activiti.tutorial.DuplicatedException;
import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.Family;
import nz.co.activiti.tutorial.ds.IdentityType;
import nz.co.activiti.tutorial.ds.TaskAction;

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

	Task getTaskById(String taskId);

	Task getTask(String taskName, String businessKey);

	void updateTask(String taskId, Task updateTask) throws Exception;

	void actionOnTask(String taskId, TaskAction action, String userId,
			Map<String, Object> variables) throws NotFoundException;

	void deleteTask(String taskId, boolean cascadeHistory) throws Exception;

	Map<String, Object> getVariablesOnTask(String taskId, boolean localScope);

	Object getVariableOnTask(String taskId, String variableName,
			boolean localScope);

	void createVariablesOnTask(String taskId, Map<String, Object> addVariables,
			boolean localScope);

	void updateVariableOnTask(String taskId, String variableName,
			Object updateVariable, boolean localScope);

	void deleteVariableOnTask(String taskId, String variableName,
			boolean localScope);

	List<IdentityLink> getAllIdentities(String taskId);

	IdentityLink getIdentity(String taskId, String identityId, Family family,
			IdentityType identityType);

	void addIdentity(String taskId, String identityId, Family family,
			IdentityType identityType) throws NotFoundException;

	void deleteIdentity(String taskId, String identityId, Family family,
			IdentityType identityType) throws NotFoundException;

	Comment createCommentOnTask(String taskId, String processInstanceId,
			String message);

	List<Comment> getAllCommentsOnTask(String taskId);

	Comment getCommentOnTask(String taskId, String commentId);

	void deleteCommentOnTask(String taskId, String commentId)
			throws NotFoundException;

	List<Event> getAllEventsOnTask(String taskId);

	Event getEventOnTask(String taskId, String eventId);

	boolean checkIfTaskExisted(String taskId) throws DuplicatedException;

}
