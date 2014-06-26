package nz.co.activiti.tutorial.rest.ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.TaskAction;
import nz.co.activiti.tutorial.rest.ds.deployment.DeploymentRestDS;
import nz.co.activiti.tutorial.rest.ds.group.GroupRestDS;
import nz.co.activiti.tutorial.rest.ds.history.HistoricRestDS;
import nz.co.activiti.tutorial.rest.ds.processdefinition.ProcessDefinitionRestDS;
import nz.co.activiti.tutorial.rest.ds.processinstance.ProcessInstanceRestDS;
import nz.co.activiti.tutorial.rest.ds.task.TaskRestDS;
import nz.co.activiti.tutorial.rest.ds.user.UserRestDS;
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.rest.model.Variable;
import nz.co.activiti.tutorial.rest.model.history.HistoricActivityInstance;
import nz.co.activiti.tutorial.rest.model.history.HistoricActivityInstanceQueryParameter;
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessInstance;
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessInstanceQueryParameter;
import nz.co.activiti.tutorial.rest.model.history.HistoricVariableInstance;
import nz.co.activiti.tutorial.rest.model.history.HistoricVariableInstanceQueryParameter;
import nz.co.activiti.tutorial.rest.model.task.Task;
import nz.co.activiti.tutorial.rest.model.task.TaskActionRequest;
import nz.co.activiti.tutorial.rest.model.task.TaskQueryParameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ActivitiRestFacade {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ActivitiRestFacade.class);

	@Resource
	private DeploymentRestDS deploymentRestDs;

	@Resource
	private ProcessDefinitionRestDS processDefinitionRestDs;

	@Resource
	private UserRestDS userRestDs;

	@Resource
	private GroupRestDS groupRestDs;

	@Resource
	private ProcessInstanceRestDS processInstanceRestDs;

	@Resource
	private HistoricRestDS historicRestDs;

	@Resource
	private TaskRestDS taskRestDs;

	// -------------------History--------------------

	/**
	 * Get active activity from history
	 * 
	 * @param activityId
	 * @param processInstanceId
	 * @return {see@HistoricActivityInstance}
	 * @throws Exception
	 */
	public HistoricActivityInstance getHistoricActivityInstance(
			String activityId, String processInstanceId) throws Exception {
		LOGGER.info("getHistoricActivityInstance start:{} ");
		LOGGER.info("activityId:{} ", activityId);
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		Map<HistoricActivityInstanceQueryParameter, String> historicProcessInstanceQueryParameters = new HashMap<HistoricActivityInstanceQueryParameter, String>();
		historicProcessInstanceQueryParameters.put(
				HistoricActivityInstanceQueryParameter.activityId, activityId);
		historicProcessInstanceQueryParameters.put(
				HistoricActivityInstanceQueryParameter.processInstanceId,
				processInstanceId);

		GenericCollectionModel<HistoricActivityInstance> historicActivityInstances = historicRestDs
				.getHistoricActivityInstances(
						historicProcessInstanceQueryParameters, null);

		if (historicActivityInstances.getModelList() == null
				|| historicActivityInstances.getModelList().size() != 1) {
			throw new NotFoundException("HistoricActivityInstance Not found");
		}
		LOGGER.info("getHistoricActivityInstance end:{} ");
		return historicActivityInstances.getModelList().get(0);
	}

	/**
	 * verify if the given process finished or not
	 * 
	 * @param businessKey
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	public boolean isProcessFinished(String businessKey,
			String processInstanceId) throws Exception {
		HistoricProcessInstance historicProcessInstance = this
				.getHistoricProcessInstance(businessKey, processInstanceId);
		if (historicProcessInstance.getEndTime() != null) {
			return true;
		}
		return false;
	}

	/**
	 * Get HistoricProcessInstance by bizKey and processInstanceId
	 * 
	 * @param businessKey
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	public HistoricProcessInstance getHistoricProcessInstance(
			String businessKey, String processInstanceId) throws Exception {
		LOGGER.info("getHistoricProcessInstance start:{} ");
		LOGGER.info("businessKey:{} ", businessKey);
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		Map<HistoricProcessInstanceQueryParameter, String> historicProcessInstanceQueryParameters = new HashMap<HistoricProcessInstanceQueryParameter, String>();
		historicProcessInstanceQueryParameters.put(
				HistoricProcessInstanceQueryParameter.businessKey, businessKey);
		historicProcessInstanceQueryParameters.put(
				HistoricProcessInstanceQueryParameter.processInstanceId,
				processInstanceId);
		GenericCollectionModel<HistoricProcessInstance> historicProcessInstances = historicRestDs
				.getHistoricProcessInstances(
						historicProcessInstanceQueryParameters, null);

		if (historicProcessInstances != null
				&& historicProcessInstances.getModelList() != null
				&& historicProcessInstances.getModelList().size() == 1) {
			LOGGER.info("getHistoricProcessInstance end:{} ");
			return historicProcessInstances.getModelList().get(0);
		}
		return null;
	}

	/**
	 * Get latest updated variables from history
	 * 
	 * @param processInstanceId
	 * @param variableName
	 * @return json format
	 * @throws Exception
	 */
	public String getLatestUpdatedHistoricVariable(String processInstanceId,
			String variableName) throws Exception {
		LOGGER.info("getLatestUpdatedHistoricVariable start:{} ");
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		LOGGER.info("variableName:{} ", variableName);
		Map<HistoricVariableInstanceQueryParameter, String> historicTaskInstanceQueryParameters = new HashMap<HistoricVariableInstanceQueryParameter, String>();
		historicTaskInstanceQueryParameters.put(
				HistoricVariableInstanceQueryParameter.variableName,
				variableName);
		historicTaskInstanceQueryParameters.put(
				HistoricVariableInstanceQueryParameter.processInstanceId,
				processInstanceId);

		Map<PagingAndSortingParameter, String> pagingAndSortingParameters = new HashMap<PagingAndSortingParameter, String>();
		pagingAndSortingParameters.put(PagingAndSortingParameter.order, "desc");

		GenericCollectionModel<HistoricVariableInstance> historicVariableInstances = historicRestDs
				.getHistoricVariableInstances(
						historicTaskInstanceQueryParameters,
						pagingAndSortingParameters);

		LOGGER.info("historicVariableInstances:{} ", historicVariableInstances);

		HistoricVariableInstance historicVariableInstance = historicVariableInstances
				.getModelList().get(0);

		Variable variable = historicVariableInstance.getVariables().get(0);
		return variable.getValue();
	}

	// -----------------------Task-------------------------
	public Set<Task> getTasksForUser(String userId) throws Exception {
		LOGGER.info("getTasksForUser start:{}", userId);
		Set<Task> taskSet = null;
		Map<PagingAndSortingParameter, String> pagingAndSortingParameters = new HashMap<PagingAndSortingParameter, String>();
		pagingAndSortingParameters.put(PagingAndSortingParameter.order, "asc");
		pagingAndSortingParameters.put(PagingAndSortingParameter.sort,
				"createTime");

		Map<TaskQueryParameter, String> taskQueryParameters = new HashMap<TaskQueryParameter, String>();
		taskQueryParameters.put(TaskQueryParameter.assignee, userId);
		GenericCollectionModel<Task> tasks = taskRestDs.getTasks(
				taskQueryParameters, pagingAndSortingParameters);

		if (tasks.getSize() > 0) {
			taskSet = new HashSet<Task>();
			taskSet.addAll(tasks.getModelList());
		}

		taskQueryParameters.clear();
		taskQueryParameters.put(TaskQueryParameter.owner, userId);

		tasks = taskRestDs.getTasks(taskQueryParameters,
				pagingAndSortingParameters);

		if (tasks.getSize() > 0) {
			if (taskSet == null) {
				taskSet = new HashSet<Task>();
			}
			taskSet.addAll(tasks.getModelList());
		}

		taskQueryParameters.clear();
		taskQueryParameters.put(TaskQueryParameter.candidateUser, userId);

		tasks = taskRestDs.getTasks(taskQueryParameters,
				pagingAndSortingParameters);

		if (tasks.getSize() > 0) {
			if (taskSet == null) {
				taskSet = new HashSet<Task>();
			}
			taskSet.addAll(tasks.getModelList());
		}

		LOGGER.info("getTasksForUser end:{}");
		return taskSet;
	}

	/**
	 * Complete task
	 * 
	 * @param taskId
	 * @param variables
	 * @return
	 * @throws Exception
	 */
	public Task completeTask(String taskId, Map<String, Object> variables)
			throws Exception {
		LOGGER.info("completeTask start:{} ", taskId);
		Task completedTask = null;
		TaskActionRequest taskActionRequest = new TaskActionRequest();

		if (variables != null) {
			for (Map.Entry<String, Object> entry : variables.entrySet()) {
				Variable variable = new Variable();
				variable.setName(entry.getKey());
				variable.setValue(String.valueOf(entry.getValue()));
				variable.setScope("global");
				taskActionRequest.addVariable(variable);
			}
		}

		taskActionRequest.setAction(TaskAction.complete);
		completedTask = this.taskRestDs.actionOnTask(taskId, taskActionRequest);
		LOGGER.info("completeTask end:{} ", completedTask);
		return completedTask;
	}

	public Task claimTask(String taskId, String assignee) throws Exception {
		LOGGER.info("claimTask start:{} ", taskId);
		Task claimedTask = null;
		TaskActionRequest taskActionRequest = new TaskActionRequest();
		taskActionRequest.setAssignee(assignee);
		taskActionRequest.setAction(TaskAction.claim);
		claimedTask = this.taskRestDs.actionOnTask(taskId, taskActionRequest);
		LOGGER.info("claimTask end:{} ", claimedTask);
		return claimedTask;
	}

}
