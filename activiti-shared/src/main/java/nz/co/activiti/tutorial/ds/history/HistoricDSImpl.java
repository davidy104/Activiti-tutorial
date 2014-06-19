package nz.co.activiti.tutorial.ds.history;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.DuplicatedException;
import nz.co.activiti.tutorial.NotFoundException;

import org.activiti.engine.HistoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricActivityInstanceQuery;
import org.activiti.engine.history.HistoricDetail;
import org.activiti.engine.history.HistoricIdentityLink;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableUpdate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class HistoricDSImpl implements HistoricDS {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(HistoricDSImpl.class);

	@Resource
	private HistoryService historyService;

	@Override
	public HistoricProcessInstance getHistoricProcessInstanceById(
			String processInstanceId, String processDefinitionId)
			throws Exception {
		LOGGER.info("getHistoricProcessInstanceById start:{}",
				processInstanceId);
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService
				.createHistoricProcessInstanceQuery().processInstanceId(
						processInstanceId);
		if (!StringUtils.isEmpty(processDefinitionId)) {
			historicProcessInstanceQuery = historicProcessInstanceQuery
					.processDefinitionId(processDefinitionId);
		}

		HistoricProcessInstance historicProcessInstance = historicProcessInstanceQuery
				.singleResult();

		if (historicProcessInstance == null) {
			throw new NotFoundException(
					"HistoricProcessInstance not found by id["
							+ processInstanceId + "]");
		}
		LOGGER.info("getHistoricProcessInstanceById end:{}");
		return historicProcessInstance;
	}

	@Override
	public boolean checkIfHistoricProcessInstanceExisted(
			String processInstanceId) {
		long count = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).count();
		if (count == 1) {
			return true;
		}
		return false;
	}

	@Override
	public void deleteHistoricProcessInstance(String processInstanceId)
			throws Exception {
		LOGGER.info("deleteHistoricProcessInstance start:{}", processInstanceId);
		if (!checkIfHistoricProcessInstanceExisted(processInstanceId)) {
			throw new NotFoundException("ProcessInstance not found by id");
		}
		historyService.deleteHistoricProcessInstance(processInstanceId);
		LOGGER.info("deleteHistoricProcessInstance end:{}");
	}

	@Override
	public List<HistoricIdentityLink> getHistoricProcessInstanceIdentities(
			String processInstanceId) throws Exception {
		return historyService
				.getHistoricIdentityLinksForProcessInstance(processInstanceId);
	}

	@Override
	public List<HistoricTaskInstance> getHistoricTaskInstances(
			String processInstanceId) throws Exception {
		historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).list();
		return null;
	}

	@Override
	public HistoricTaskInstance getHistoricTaskInstance(
			String processInstanceId, String taskId) throws Exception {
		return historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).taskId(taskId)
				.singleResult();
	}

	@Override
	public void deleteHistoricTaskInstance(String taskId) throws Exception {
		historyService.deleteHistoricTaskInstance(taskId);

	}

	@Override
	public List<HistoricIdentityLink> getHistoricTaskInstanceIdentities(
			String taskId) throws Exception {
		return historyService.getHistoricIdentityLinksForTask(taskId);
	}

	@Override
	public List<HistoricActivityInstance> getHistoricActivityInstances(
			String processInstanceId, String processDefinitionId)
			throws Exception {
		LOGGER.info("getHistoricActivityInstances start:{} ");
		LOGGER.info("processInstanceId:{} ", processInstanceId);
		LOGGER.info("processDefinitionId:{} ", processDefinitionId);
		HistoricActivityInstanceQuery historicActivityInstanceQuery = historyService
				.createHistoricActivityInstanceQuery();
		if (!StringUtils.isEmpty(processInstanceId)) {
			historicActivityInstanceQuery = historicActivityInstanceQuery
					.processInstanceId(processInstanceId);
		}
		if (!StringUtils.isEmpty(processDefinitionId)) {
			historicActivityInstanceQuery = historicActivityInstanceQuery
					.processDefinitionId(processDefinitionId);
		}
		LOGGER.info("getHistoricActivityInstances end:{} ");
		return historicActivityInstanceQuery
				.orderByHistoricActivityInstanceEndTime().desc().list();

	}

	@Override
	public List<HistoricVariableUpdate> getHistoricVariablesOnProcess(
			String processInstanceId) throws Exception {
		List<HistoricVariableUpdate> historicVariableUpdateList = null;
		List<HistoricDetail> details = historyService
				.createHistoricDetailQuery().variableUpdates()
				.processInstanceId(processInstanceId).orderByVariableName()
				.asc().list();

		if (details != null) {
			historicVariableUpdateList = new ArrayList<HistoricVariableUpdate>();
			for (HistoricDetail detail : details) {
				historicVariableUpdateList.add((HistoricVariableUpdate) detail);
			}
		}
		return historicVariableUpdateList;
	}

	@Override
	public HistoricVariableUpdate getHistoricVariableOnProcess(
			String processInstanceId, String variableName) throws Exception {
		List<HistoricVariableUpdate> historicVariableUpdateList = this
				.getHistoricVariablesOnProcess(processInstanceId);
		if (historicVariableUpdateList != null) {
			for (HistoricVariableUpdate historicVariableUpdate : historicVariableUpdateList) {
				String vName = historicVariableUpdate.getVariableName();
				if (vName.equalsIgnoreCase(variableName)) {
					return historicVariableUpdate;
				}
			}
		}
		return null;
	}

	@Override
	public List<HistoricDetail> getHistoricDetails(String processInstanceId)
			throws Exception {
		return historyService.createHistoricDetailQuery()
				.processInstanceId(processInstanceId).list();
	}

	@Override
	public HistoricProcessInstance getHistoricProcessInstance(String bizKey,
			String processDefinitionId) throws Exception {

		return null;
	}

}
