package nz.co.activiti.tutorial.ds;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.DuplicatedException;
import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.deployment.DeploymentDS;
import nz.co.activiti.tutorial.ds.execution.ExecutionDS;
import nz.co.activiti.tutorial.ds.group.GroupDS;
import nz.co.activiti.tutorial.ds.history.HistoricDS;
import nz.co.activiti.tutorial.ds.processdefinition.ProcessDefinitionDS;
import nz.co.activiti.tutorial.ds.processinstance.ProcessInstanceDS;
import nz.co.activiti.tutorial.ds.task.TaskDS;
import nz.co.activiti.tutorial.ds.user.UserDS;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.commons.lang3.StringUtils;
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
	private GroupDS groupDs;

	@Resource
	private DeploymentDS deploymentDs;

	@Resource
	private ProcessInstanceDS processInstanceDs;

	@Resource
	private ProcessDefinitionDS processDefinitionDs;

	@Resource
	private ExecutionDS executionDs;

	@Resource
	private HistoricDS historicDs;

	@Resource
	private RepositoryService repositoryService;

	@Resource
	private TaskService taskService;

	@Resource
	private IdentityService identityService;

	// --------------deployment-----------
	public Deployment deployment(String name, String category,
			InputStream resourceStream) throws DuplicatedException {
		if (deploymentDs.checkIfDeploymentExisted(name, category)) {
			throw new DuplicatedException(
					"Duplicated Deployment with same name[" + name
							+ "] and category[" + category + "]");
		}
		return deploymentDs.deployment(name, category, resourceStream);
	}

	public void undeployment(String deploymentId) throws NotFoundException {
		if (deploymentDs.getDeploymentByDeploymentId(deploymentId) == null) {
			throw new NotFoundException("Deployment not found by id["
					+ deploymentId + "]");
		}
		deploymentDs.undeployment(deploymentId);
	}

	public List<String> deployProcessesFromClasspath(String... resources) {
		List<String> deployIds = new ArrayList<String>();
		for (String resource : resources) {
			deployIds.add(repositoryService.createDeployment()
					.addClasspathResource(resource).deploy().getId());
		}
		return deployIds;
	}

	// --------------execution-----------
	public Object getVariableOnExecution(String executionId, String variableName)
			throws NotFoundException {
		return executionDs.getVariableOnExecution(executionId, variableName);
	}

	public void signal(String executionId, Map<String, Object> variables)
			throws NotFoundException {
		executionDs.signal(executionId, variables);
	}

	// --------------processInstance-----------
	public ProcessInstance startProcess(String processDefinitionId,
			String businessKey, Map<String, Object> variables)
			throws NotFoundException {
		if (StringUtils.isEmpty(processDefinitionId)
				|| StringUtils.isEmpty(businessKey)) {
			throw new IllegalArgumentException(
					"processDefinitionId and businessKey must be provided");
		}
		return processInstanceDs.startProcessByProcessDefinitionId(
				processDefinitionId, businessKey, variables);
	}

	public ProcessInstance getProcessInstance(String businessKey,
			String processDefinitionId) {
		return processInstanceDs.getProcessInstance(businessKey,
				processDefinitionId);
	}

	public void addInvolvedPeopleToProcess(String processInstanceId,
			String userId, IdentityType identityType) throws Exception {
		if (!userDs.checkIfUserExisted(userId)) {
			throw new NotFoundException("User not found by id[" + userId + "]");
		}
		processInstanceDs.addInvolvedPeopleToProcess(processInstanceId, userId,
				identityType);
	}

	// ----------processDefinition---------
	public ProcessDefinition getProcessDefinitionByDeploymentId(
			String deploymentId) {
		LOGGER.info("getProcessDefinition start:{} ");
		LOGGER.info("deploymentId:{} ", deploymentId);

		ProcessDefinition processDefinition = processDefinitionDs
				.getProcessDefinitionByDeploymentId(deploymentId);
		LOGGER.info("getProcessDefinition end:{} ", processDefinition);
		return processDefinition;
	}

	// -----------------tasks-----------------
	public Task getTask(String taskName, String businessKey) throws Exception {
		LOGGER.info("getTask start:{}");
		LOGGER.info("taskName:{}", taskName);
		LOGGER.info("businessKey:{}", businessKey);
		if (StringUtils.isEmpty(taskName) || StringUtils.isEmpty(businessKey)) {
			throw new IllegalArgumentException(
					"taskName and businessKey must be provided");
		}
		Task task = taskDs.getTask(taskName, businessKey);
		LOGGER.info("getTask end:{}", task);
		return task;
	}

	public Task updateTask(String businessKey, String taskName, Task updateTask)
			throws Exception {
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

	public Task completeTask(String taskId, Map<String, Object> variables)
			throws Exception {
		taskDs.actionOnTask(taskId, TaskAction.complete, null, variables);
		return taskDs.getTaskById(taskId);
	}

	public Task claimTask(String taskId, String userId) throws Exception {
		taskDs.actionOnTask(taskId, TaskAction.claim, userId, null);
		return taskDs.getTaskById(taskId);
	}

	public Task delegateTask(String taskId, String userId) throws Exception {
		taskDs.actionOnTask(taskId, TaskAction.delegate, userId, null);
		return taskDs.getTaskById(taskId);
	}

	public Task resolveTask(String taskId, Map<String, Object> variables)
			throws Exception {
		taskDs.actionOnTask(taskId, TaskAction.resolve, null, variables);
		return taskDs.getTaskById(taskId);
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
	 * Get All tasks for the given User (including candiates tasks) activiti
	 * needs workaround for taskCandidateOrAssigned
	 * 
	 * @param userId
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<Task> getPaginatedTasksForUser(String userId,
			Integer firstResult, Integer maxResults) {
		LOGGER.info("getAllTasksForUser start:{} ", userId);
		List<Task> tasks = null;
		TaskQuery taskQuery = taskService.createTaskQuery()
				.taskCandidateOrAssigned(userId).orderByTaskCreateTime().asc();

		if (firstResult != null && maxResults != null) {
			tasks = taskQuery.listPage(firstResult, maxResults);
		} else {
			tasks = taskQuery.list();
		}
		LOGGER.info("getAllTasksForUser end:{} ");
		return tasks;
	}

	/**
	 * get all tasks for the given user (including candidates task, assignees
	 * task and owners task)
	 * 
	 * @param userId
	 * @return
	 */
	public List<Task> getTasksForUser(String userId) {
		LOGGER.info("getTasksForUser start:{}", userId);
		List<Task> tasks = new ArrayList<Task>();

		List<Task> tempTasks = taskService.createTaskQuery()
				.taskAssignee(userId).orderByTaskCreateTime().asc().list();
		LOGGER.info("assigneeTask:{}", tempTasks.size());

		tasks.addAll(tempTasks);

		tempTasks = taskService.createTaskQuery().taskCandidateUser(userId)
				.orderByTaskCreateTime().asc().list();
		LOGGER.info("candidateTask:{}", tempTasks.size());
		tasks.addAll(tempTasks);

		tempTasks = taskService.createTaskQuery().taskOwner(userId)
				.orderByTaskCreateTime().asc().list();
		LOGGER.info("ownerTask:{}", tempTasks.size());
		tasks.addAll(tempTasks);

		return tasks;
	}

	public List<Task> getTasksForGroup(String groupId) {
		return taskService.createTaskQuery().taskCandidateGroup(groupId)
				.orderByTaskCreateTime().asc().list();
	}

	/**
	 * verify if the given user has right to process the task (including
	 * assignee and candidates)
	 * 
	 * @param businessKey
	 * @param taskName
	 * @param userId
	 * @return
	 */
	public boolean checkIfUserHasRightForGivenTask(String businessKey,
			String taskName, String userId) {
		if (taskService.createTaskQuery().taskAssignee(userId)
				.processInstanceBusinessKey(businessKey).taskName(taskName)
				.count() == 1) {
			return true;
		} else if (taskService.createTaskQuery().taskCandidateUser(userId)
				.processInstanceBusinessKey(businessKey).taskName(taskName)
				.count() == 1) {
			return true;
		}
		return false;
	}

	// --------------identity-----------
	public User updateUser(String userId, String firstName, String lastName,
			String email, String password) throws Exception {
		if (!userDs.checkIfUserExisted(userId)) {
			throw new NotFoundException("User not found by id[" + userId + "]");
		}
		userDs.updateUser(userId, firstName, lastName, email, password);
		return userDs.getUserById(userId);
	}

	public User createUser(String userId, String firstName, String lastName,
			String email, String password) throws Exception {
		if (userDs.checkIfUserExisted(userId)) {
			throw new DuplicatedException("User[" + userId
					+ "] already existed");
		}
		userDs.createUser(userId, firstName, lastName, email, password);
		return userDs.getUserById(userId);
	}

	public void deleteUser(String userId) throws Exception {
		if (!userDs.checkIfUserExisted(userId)) {
			throw new NotFoundException("User not found by id[" + userId + "]");
		}
		userDs.deleteUser(userId);
	}

	public Group createGroup(String groupId, String name, String type)
			throws Exception {
		if (groupDs.checkIfGroupExisted(groupId)) {
			throw new DuplicatedException("Group[" + groupId
					+ "] already existed");
		}
		groupDs.createGroup(groupId, name, type);
		return groupDs.getGroupById(groupId);
	}

	public Group updateGroup(String groupId, String name, String type)
			throws Exception {
		if (!groupDs.checkIfGroupExisted(groupId)) {
			throw new NotFoundException("Group not found by id[" + groupId
					+ "]");
		}
		groupDs.updateGroup(groupId, name, type);
		return groupDs.getGroupById(groupId);
	}

	public void deleteGroup(String groupId) throws Exception {
		if (!groupDs.checkIfGroupExisted(groupId)) {
			throw new NotFoundException("Group not found by id[" + groupId
					+ "]");
		}
		groupDs.deleteGroup(groupId);
	}

	public void createMembership(String userId, String groupId)
			throws Exception {
		if (!userDs.checkIfUserExisted(userId)) {
			throw new NotFoundException("User not found by id[" + userId + "]");
		}
		if (!groupDs.checkIfGroupExisted(groupId)) {
			throw new NotFoundException("Group not found by id[" + groupId
					+ "]");
		}

		try {
			groupDs.createMemberToGroup(groupId, userId);
		} catch (RuntimeException e) {
			throw new DuplicatedException(e);
		}
	}

	public void deleteMemberFromGroup(String groupId, String userId)
			throws Exception {
		if (!userDs.checkIfUserExisted(userId)) {
			throw new NotFoundException("User not found by id[" + userId + "]");
		}
		if (!groupDs.checkIfGroupExisted(groupId)) {
			throw new NotFoundException("Group not found by id[" + groupId
					+ "]");
		}
		groupDs.deleteMemberFromGroup(groupId, userId);
	}

	// ----------------historic-------------------
	public Object getHistoricVariableOnProcess(String processInstanceId,
			String variableName) {
		return this.historicDs.getHistoricVariableOnProcess(processInstanceId,
				variableName);
	}

	// ----------------generalmethods-------------------

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
				GenericActivityModel incomingActivityModel = GenericActivityModel
						.getBuilder(incomingActivity.getId(),
								incomingActivityName, incomingActivityType)
						.build();
				genericActivityModel.addIncomingActivity(transition,
						incomingActivityModel);
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
				GenericActivityModel outgoingActivityModel = GenericActivityModel
						.getBuilder(outgoingActivity.getId(),
								outgoingActivityName, outgoingActivityType)
						.build();
				genericActivityModel.addOutgoingActivity(transition,
						outgoingActivityModel);
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

	public boolean ifProcessFinished(String businessKey,
			String processDefinitionId) throws NotFoundException {
		HistoricProcessInstance historicProcessInstance = this.historicDs
				.getHistoricProcessInstance(businessKey, processDefinitionId);
		if (historicProcessInstance == null) {
			throw new NotFoundException(
					"HistoricProcessInstance not found with businessKey["
							+ businessKey + "] and processDefinitionId["
							+ processDefinitionId + "]");
		}
		if (historicProcessInstance.getEndTime() != null) {
			return true;
		}
		return false;
	}

}
