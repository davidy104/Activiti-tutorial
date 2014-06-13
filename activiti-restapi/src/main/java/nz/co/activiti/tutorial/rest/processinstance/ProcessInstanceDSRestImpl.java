package nz.co.activiti.tutorial.rest.processinstance;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.ds.processinstance.ProcessInstanceDS;
import nz.co.activiti.tutorial.model.PagingAndSortingParameters;
import nz.co.activiti.tutorial.model.Party;
import nz.co.activiti.tutorial.model.Variable;
import nz.co.activiti.tutorial.model.processinstance.ProcessInstance;
import nz.co.activiti.tutorial.model.processinstance.ProcessInstanceQueryParameters;
import nz.co.activiti.tutorial.model.processinstance.ProcessInstances;
import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;

import org.activiti.engine.task.IdentityLinkType;
import org.apache.cxf.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

@Service("processInstanceDSRest")
public class ProcessInstanceDSRestImpl extends ActivitiRestClientAccessor
		implements ProcessInstanceDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessInstanceDSRestImpl.class);

	@Resource
	private ProcessInstanceJSONConverter processInstanceJSONConverter;

	@Override
	public ProcessInstance startProcessByProcessDefinitionId(
			String processDefinitionId, String businessKey,
			Map<String, Object> variables) throws Exception {
		LOGGER.info("startProcessByProcessDefinitionId start:{}",
				processDefinitionId);
		LOGGER.info("businessKey:{}", businessKey);
		ProcessInstance processInstance = null;
		String requestBody = null;
		StringBuilder stringBuilder = new StringBuilder(
				"{\"processDefinitionId\":\"" + processDefinitionId + "\"");
		if (!StringUtils.isEmpty(businessKey)) {
			stringBuilder.append(",\"businessKey\":\"" + businessKey + "\"");
		}
		if (variables != null) {
			String variablesJsonText = this.variablesJsonBuild(variables);
			LOGGER.info("variablesJsonText:{} ", variablesJsonText);
			if (!StringUtils.isEmpty(variablesJsonText)) {
				stringBuilder.append("," + variablesJsonText);
			}
		}
		stringBuilder.append("}");
		requestBody = stringBuilder.toString();
		LOGGER.info("requestBody:{} ", requestBody);

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, requestBody);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.CREATED) {
			throw new Exception("startProcessByProcessDefinitionId ["
					+ processDefinitionId + "] failed:{} " + respStr);
		} else {
			processInstance = processInstanceJSONConverter
					.toProcessInstance(respStr);
		}

		LOGGER.info("startProcessByProcessDefinitionId end:{}", processInstance);
		return processInstance;
	}

	@Override
	public ProcessInstance startProcessByProcessDefinitionKey(
			String processDefinitionKey, String businessKey,
			Map<String, Object> variables) throws Exception {
		LOGGER.info("startProcessByProcessDefinitionKey start:{}",
				processDefinitionKey);
		LOGGER.info("businessKey:{}", businessKey);
		ProcessInstance processInstance = null;
		String requestBody = null;
		StringBuilder stringBuilder = new StringBuilder(
				"{\"processDefinitionKey\":\"" + processDefinitionKey + "\"");
		if (!StringUtils.isEmpty(businessKey)) {
			stringBuilder.append(",\"businessKey\":\"" + businessKey + "\"");
		}
		if (variables != null) {
			String variablesJsonText = this.variablesJsonBuild(variables);
			LOGGER.info("variablesJsonText:{} ", variablesJsonText);
			if (!StringUtils.isEmpty(variablesJsonText)) {
				stringBuilder.append("," + variablesJsonText);
			}
		}
		stringBuilder.append("}");
		requestBody = stringBuilder.toString();
		LOGGER.info("requestBody:{} ", requestBody);

		WebResource webResource = client.resource(baseUrl).path(
				"/runtime/process-instances");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, requestBody);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.CREATED) {
			throw new Exception("startProcessByProcessDefinitionKey ["
					+ processDefinitionKey + "] failed:{} " + respStr);
		} else {
			processInstance = processInstanceJSONConverter
					.toProcessInstance(respStr);
		}

		LOGGER.info("startProcessByProcessDefinitionKey end:{}",
				processInstance);
		return processInstance;
	}

	@Override
	public ProcessInstance getProcessInstance(String processInstanceId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteProcessInstance(String processInstanceId)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void suspendProcessInstance(String processInstanceId)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void activeProcessInstance(String processInstanceId)
			throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public ProcessInstances getProcessInstances(
			Map<PagingAndSortingParameters, Object> pagingAndSortingParameters)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ProcessInstances getProcessInstances(
			Map<ProcessInstanceQueryParameters, Object> processInstanceQueryParameters,
			Map<PagingAndSortingParameters, Object> pagingAndSortingParameters)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<Party> getInvolvedPeopleForProcessInstance(
			String processInstanceId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Party addInvolvedPeopleToProcess(String processInstanceId,
			String user, IdentityLinkType identityType) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void removeInvolvedPeopleFromProcess(String processInstanceId,
			String user, IdentityLinkType identityType) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Variable> getVariablesFromProcess(String processInstanceId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Variable getVariableFromProcess(String processInstanceId,
			String variableName) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Variable> createVariablesForProcess(String processInstanceId,
			List<Variable> addVariables) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Variable> updateVariablesForProcess(String processInstanceId,
			List<Variable> updateVariables) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Variable updateVariableForProcess(String processInstanceId,
			Variable updateVariable) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
