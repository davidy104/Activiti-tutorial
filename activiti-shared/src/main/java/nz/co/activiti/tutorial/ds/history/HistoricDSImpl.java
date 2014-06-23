package nz.co.activiti.tutorial.ds.history;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

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
			String processInstanceId) {
		LOGGER.info("getHistoricProcessInstanceById start:{}",
				processInstanceId);
		HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService
				.createHistoricProcessInstanceQuery().processInstanceId(
						processInstanceId);

		HistoricProcessInstance historicProcessInstance = historicProcessInstanceQuery
				.singleResult();

		LOGGER.info("getHistoricProcessInstanceById end:{}");
		return historicProcessInstance;
	}

	@Override
	public HistoricProcessInstance getHistoricProcessInstance(
			String businessKey, String processDefinitionId) {
		return historyService.createHistoricProcessInstanceQuery()
				.processInstanceBusinessKey(businessKey)
				.processDefinitionId(processDefinitionId).singleResult();
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
	public void deleteHistoricProcessInstance(String processInstanceId) {
		LOGGER.info("deleteHistoricProcessInstance start:{}", processInstanceId);
		historyService.deleteHistoricProcessInstance(processInstanceId);
		LOGGER.info("deleteHistoricProcessInstance end:{}");
	}

	@Override
	public List<HistoricIdentityLink> getHistoricProcessInstanceIdentities(
			String processInstanceId) {
		return historyService
				.getHistoricIdentityLinksForProcessInstance(processInstanceId);
	}

	@Override
	public List<HistoricTaskInstance> getHistoricTaskInstances(
			String processInstanceId) {
		return historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).list();
	}

	@Override
	public HistoricTaskInstance getHistoricTaskInstance(
			String processInstanceId, String taskId) {
		return historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(processInstanceId).taskId(taskId)
				.singleResult();
	}

	@Override
	public void deleteHistoricTaskInstance(String taskId) {
		historyService.deleteHistoricTaskInstance(taskId);

	}

	@Override
	public List<HistoricIdentityLink> getHistoricTaskInstanceIdentities(
			String taskId) {
		return historyService.getHistoricIdentityLinksForTask(taskId);
	}

	@Override
	public List<HistoricActivityInstance> getHistoricActivityInstances(
			String processInstanceId, String processDefinitionId) {
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
				.orderByHistoricActivityInstanceEndTime().asc().list();

	}

	@Override
	public List<Object> getHistoricVariablesOnProcess(String processInstanceId) {
		LOGGER.info("getHistoricVariablesOnProcess start:{}", processInstanceId);
		List<Object> objectList = null;
		List<HistoricDetail> details = historyService
				.createHistoricDetailQuery().variableUpdates()
				.processInstanceId(processInstanceId).orderByTime().list();

		if (details != null) {
			objectList = new ArrayList<Object>();
			for (HistoricDetail detail : details) {
				HistoricVariableUpdate historicVariableUpdate = (HistoricVariableUpdate) detail;
				objectList.add(historicVariableUpdate.getValue());
			}
		}
		LOGGER.info("getHistoricVariablesOnProcess end:{}");
		return objectList;
	}

	/**
	 * get the lastest object by name
	 */
	@Override
	public Object getHistoricVariableOnProcess(String processInstanceId,
			String variableName) {
		List<HistoricDetail> details = historyService
				.createHistoricDetailQuery().variableUpdates()
				.processInstanceId(processInstanceId).orderByTime().desc()
				.list();

		if (details != null) {
			for (HistoricDetail detail : details) {
				HistoricVariableUpdate historicVariableUpdate = (HistoricVariableUpdate) detail;
				String vName = historicVariableUpdate.getVariableName();
				if (vName.equalsIgnoreCase(variableName)) {
					return historicVariableUpdate.getValue();
				}
			}
		}
		return null;
	}

	@Override
	public List<HistoricDetail> getHistoricDetails(String processInstanceId) {
		return historyService.createHistoricDetailQuery()
				.processInstanceId(processInstanceId).list();
	}

}
