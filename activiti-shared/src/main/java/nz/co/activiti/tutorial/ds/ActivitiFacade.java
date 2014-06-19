package nz.co.activiti.tutorial.ds;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ProcessActivityDto;
import nz.co.activiti.tutorial.ds.processinstance.ProcessInstanceDS;
import nz.co.activiti.tutorial.ds.task.TaskDS;
import nz.co.activiti.tutorial.ds.user.UserDS;

import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ActivitiFacade {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ActivitiFacade.class);

	@Resource
	private TaskDS taskDs;

	@Resource
	private UserDS userDs;

	@Resource
	private ProcessInstanceDS processInstanceDs;

	@Resource
	private RepositoryService repositoryService;

	@Resource
	private TaskService taskService;

	@Resource
	private IdentityService identityService;

	// -----------------tasks-----------------
	public Task updateTask(String businessKey, String taskName,
			TaskEntity updateTask) throws Exception {
		LOGGER.info("updateTask start:{} ");
		LOGGER.info("businessKey:{} ", businessKey);
		LOGGER.info("taskName:{} ", taskName);
		Task task = taskDs.getTask(taskName, businessKey);
		if (task == null) {
			throw new NotFoundException("Task not found by businessKey["
					+ businessKey + "] and taskName[" + taskName + "]");
		}
		String taskId = task.getId();
		taskDs.updateTask(taskId, updateTask);
		task = taskDs.getTaskById(taskId);
		LOGGER.info("updateTask end:{} ");
		return task;
	}

	/**
	 * Delete the given task
	 * 
	 * @param taskId
	 * @throws Exception
	 */
	public void deleteTask(String taskId) throws Exception {
		try {
			taskDs.deleteTask(taskId, true);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException("Task not found with id[" + taskId
					+ "]:{}" + e.getMessage());
		}
	}

	/**
	 * 
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getVariablesOnTask(String taskId)
			throws Exception {
		if (taskDs.checkIfTaskExisted(taskId)) {
			throw new NotFoundException("Task not found by id[" + taskId + "]");
		}
		return taskDs.getVariablesOnTask(taskId, true);
	}

	/**
	 * 
	 * @param taskId
	 * @param variableName
	 * @return
	 * @throws Exception
	 */
	public Object getVariableOnTask(String taskId, String variableName)
			throws Exception {
		if (taskDs.checkIfTaskExisted(taskId)) {
			throw new NotFoundException("Task not found by id[" + taskId + "]");
		}
		return taskDs.getVariableOnTask(taskId, variableName, true);
	}

	/**
	 * 
	 * @param taskId
	 * @param groupId
	 * @throws Exception
	 */
	public void addCandidateGroup(String taskId, String groupId)
			throws Exception {
		try {
			taskDs.addIdentity(taskId, groupId, Family.groups,
					IdentityType.candidate);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException(e);
		}
	}

	/**
	 * 
	 * @param taskId
	 * @param userId
	 * @throws Exception
	 */
	public void addCandidateUser(String taskId, String userId) throws Exception {
		try {
			taskDs.addIdentity(taskId, userId, Family.users,
					IdentityType.candidate);
		} catch (ActivitiObjectNotFoundException e) {
			// the given taskId or userId not existed
			throw new NotFoundException(e);
		}
	}

	public void deleteCandidateUser(String taskId, String userId)
			throws Exception {
		try {
			taskDs.deleteIdentity(taskId, userId, Family.users,
					IdentityType.candidate);
		} catch (ActivitiObjectNotFoundException e) {
			// the given taskId or userId not existed
			throw new NotFoundException(e);
		}
	}

	public void deleteCandidateGroup(String taskId, String userId)
			throws Exception {
		try {
			taskDs.deleteIdentity(taskId, userId, Family.groups,
					IdentityType.candidate);
		} catch (ActivitiObjectNotFoundException e) {
			// the given taskId or userId not existed
			throw new NotFoundException(e);
		}
	}

	/**
	 * 
	 * @param taskId
	 * @return
	 * @throws Exception
	 */
	public List<IdentityLink> getAllCandidatesOnTask(String taskId)
			throws Exception {
		List<IdentityLink> identityList = new ArrayList<IdentityLink>();
		if (taskDs.checkIfTaskExisted(taskId)) {
			throw new NotFoundException("Task not found by id[" + taskId + "]");
		}
		List<IdentityLink> allIdentities = taskDs.getAllIdentities(taskId);
		if (allIdentities != null) {
			for (IdentityLink identityLink : allIdentities) {
				if (identityLink.getType().equalsIgnoreCase(
						IdentityType.candidate.name())) {
					identityList.add(identityLink);
				}
			}
		}
		return identityList;
	}

	/**
	 * Get All tasks for the given User (including candiates tasks)
	 * 
	 * @param userId
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<Task> getTasksForUser(String userId, Integer firstResult,
			Integer maxResults) {
		LOGGER.info("getAllTasksForUser start:{} ", userId);
		List<Task> tasks = null;
		TaskQuery taskQuery = taskService.createTaskQuery()
				.taskCandidateOrAssigned(userId).orderByTaskCreateTime().desc();

		if (firstResult != null && maxResults != null) {
			tasks = taskQuery.listPage(firstResult, maxResults);
		} else {
			tasks = taskQuery.list();
		}
		LOGGER.info("getAllTasksForUser end:{} ");
		return tasks;
	}

	/**
	 * verify if the given user has right to process the task
	 * 
	 * @param businessKey
	 * @param taskName
	 * @param userId
	 * @return
	 */
	public boolean checkIfUserHasRightForGivenTask(String businessKey,
			String taskName, String userId) {
		long count = taskService.createTaskQuery().taskAssignee(userId)
				.processInstanceBusinessKey(businessKey).taskName(taskName)
				.count();
		if (count == 0) {
			return false;
		}
		return true;
	}

	// --------------------------------general
	// methods-----------------------------------

	/**
	 * Get Current Active activity
	 * 
	 * @param processDefinitionId
	 * @param businessKey
	 * @return
	 * @throws Exception
	 */
	public GenericActivityModel getActiveActivity(String processDefinitionId,
			String businessKey) throws Exception {
		LOGGER.info("getActiveActivity start:{} ");
		LOGGER.info("processDefinitionId:{} ", processDefinitionId);
		LOGGER.info("businessKey:{} ", businessKey);
		GenericActivityModel genericActivityModel = null;
		ProcessInstance processInstance = processInstanceDs.getProcessInstance(
				businessKey, processDefinitionId);
		if (processInstance == null) {
			throw new NotFoundException(
					"Processinstance not found by processDefinitionId["
							+ processDefinitionId + "] and businessKey["
							+ businessKey + "]");
		}

		String activityId = processInstance.getActivityId();
		ActivityImpl activityImpl = this.getActivityImpl(processDefinitionId,
				activityId);

		if (activityImpl != null) {
			String activityName = (String) activityImpl.getProperty("name");
			String activityType = (String) activityImpl.getProperty("type");
			genericActivityModel = GenericActivityModel.getBuilder(activityId,
					activityName, activityType).build();

			List<PvmTransition> incomingTransitions = activityImpl
					.getIncomingTransitions();
			for (PvmTransition pvmTransition : incomingTransitions) {
				String transition = pvmTransition.getId();
				PvmActivity incomingActivity = pvmTransition.getDestination();
				String incomingActivityType = (String) incomingActivity
						.getProperty("type");
				String incomingActivityName = (String) incomingActivity
						.getProperty("name");
				ProcessActivityDto incomingActivityDto = ProcessActivityDto
						.getBuilder(incomingActivity.getId(),
								incomingActivityName, incomingActivityType)
						.build();
				genericActivityModel.addIncomingActivity(transition,
						incomingActivityDto);
			}

			List<PvmTransition> outgoingTransitions = activityImpl
					.getOutgoingTransitions();
			for (PvmTransition pvmTransition : outgoingTransitions) {
				String transition = pvmTransition.getId();
				PvmActivity outgoingActivity = pvmTransition.getDestination();
				String outgoingActivityType = (String) outgoingActivity
						.getProperty("type");
				String outgoingActivityName = (String) outgoingActivity
						.getProperty("name");
				ProcessActivityDto outgoingActivityDto = ProcessActivityDto
						.getBuilder(outgoingActivity.getId(),
								outgoingActivityName, outgoingActivityType)
						.build();
				genericActivityModel.addOutgoingActivity(transition,
						outgoingActivityDto);
			}
		}
		LOGGER.info("getActiveActivity end:{} ", genericActivityModel);
		return genericActivityModel;
	}

	private ActivityImpl getActivityImpl(String processDefinitionId,
			String activityId) {
		ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);

		List<ActivityImpl> activityList = processDefinitionEntity
				.getActivities();

		if (activityList != null) {
			for (ActivityImpl activityImpl : activityList) {
				String id = activityImpl.getId();
				if (id.equals(activityId)) {
					return activityImpl;
				}
			}
		}
		return null;
	}

	public List<String> getGroupIdsByUserId(String userId) {
		List<Group> groupList = identityService.createGroupQuery()
				.groupMember(userId).list();
		List<String> groupIds = null;

		if (groupList != null && groupList.size() > 0) {
			groupIds = new ArrayList<String>();
			for (Group group : groupList) {
				groupIds.add(group.getId());
			}
		}
		return groupIds;
	}

}
