package nz.co.activiti.tutorial.rest.ds.history

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import java.text.SimpleDateFormat

import javax.annotation.Resource

import nz.co.activiti.tutorial.ConvertException
import nz.co.activiti.tutorial.rest.GeneralModelJSONConverter
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel
import nz.co.activiti.tutorial.rest.model.Variable
import nz.co.activiti.tutorial.rest.model.history.HistoricActivityInstance
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessIdentity
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessInstance
import nz.co.activiti.tutorial.rest.model.history.HistoricProcessInstanceComment
import nz.co.activiti.tutorial.rest.model.history.HistoricTaskInstance
import nz.co.activiti.tutorial.rest.model.history.HistoricVariableInstance;

import org.springframework.stereotype.Component
@Component
@Slf4j
class HistoricJSONConverter {

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	@Resource
	GeneralModelJSONConverter generalModelJSONConverter

	GenericCollectionModel<HistoricActivityInstance> toHistoricActivityInstances(String jsonText)throws ConvertException{
		log.info "toHistoricActivityInstances start:{} $jsonText"

		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;

		Object[] datajson = (Object[])jsonResult.get("data")
		log.info "datajson:{} $datajson"

		Integer total =  (Integer)jsonResult.get("total")
		Integer start =  (Integer)jsonResult.get("start")
		Integer ssize =  (Integer)jsonResult.get("size")
		String ssort =  (String)jsonResult.get("sort")
		String order =  (String)jsonResult.get("order")

		GenericCollectionModel<HistoricActivityInstance> historicActivityInstances = new GenericCollectionModel<HistoricActivityInstance>(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single processInstance:{} "+it
			HistoricActivityInstance historicActivityInstance = new HistoricActivityInstance(
					id:it.id,
					activityId:it.activityId,
					activityName:it.activityName,
					activityType:it.activityType,
					processDefinitionId:it.processDefinitionId,
					processInstanceUrl:it.processInstanceUrl,
					executionId:it.executionId,
					calledProcessInstanceId:it.calledProcessInstanceId,
					taskId:it.taskId,
					assignee:it.assignee,
					tenantId:it.tenantId
					)

			if(it.durationInMillis){
				historicActivityInstance.durationInMillis = (Long)it.durationInMillis
			}

			if(it.startTime){
				historicActivityInstance.startTime = sdf.parse(it.startTime)
			}

			if(it.endTime){
				historicActivityInstance.endTime = sdf.parse(it.endTime)
			}
			historicActivityInstances.addModel(historicActivityInstance)
		}
		log.info "toHistoricActivityInstances end:{}"
		return historicActivityInstances
	}

	HistoricProcessInstance toHistoricProcessInstance(String jsonText)throws ConvertException{
		log.info "toHistoricProcessInstance start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper()
		Object result = jsonSlurper.parseText(jsonText)
		List<Variable> variables

		Map jsonResult = (Map) result
		String id = (String) jsonResult.get("id")
		String businessKey = (String) jsonResult.get("businessKey")
		String processDefinitionUrl = (String) jsonResult.get("processDefinitionUrl")
		String processDefinitionId = (String) jsonResult.get("processDefinitionId")
		String startTimeJson = (String) jsonResult.get("startTime")
		String endTimeJson = (String) jsonResult.get("endTime")
		String startUserId = (String) jsonResult.get("startUserId")
		String startActivityId = (String) jsonResult.get("startActivityId")
		String endActivityId = (String) jsonResult.get("endActivityId")
		String deleteReason = (String) jsonResult.get("deleteReason")
		String superProcessInstanceId = (String) jsonResult.get("superProcessInstanceId")
		String url = (String)jsonResult.get("url")
		String tenantId = (String) jsonResult.get("tenantId")

		String variablesJson = (String)jsonResult.get("variables")

		if(variablesJson){
			variables = generalModelJSONConverter.toVariables(variablesJson)
		}

		HistoricProcessInstance historicProcessInstance = new HistoricProcessInstance(
				id:id,
				processDefinitionUrl:processDefinitionUrl,
				businessKey:businessKey,
				processDefinitionId:processDefinitionId,
				startUserId:startUserId,
				startActivityId:startActivityId,
				endActivityId:endActivityId,
				deleteReason:deleteReason,
				superProcessInstanceId:superProcessInstanceId,
				url:url,
				tenantId:tenantId)

		historicProcessInstance.variables = variables

		if(jsonResult.get("durationInMillis")){
			historicProcessInstance.durationInMillis = (Long)jsonResult.get("durationInMillis")
		}

		if(startTimeJson){
			historicProcessInstance.startTime = sdf.parse(startTimeJson)
		}

		if(endTimeJson){
			historicProcessInstance.endTime = sdf.parse(endTimeJson)
		}

		log.info "toHistoricProcessInstance end:{} $historicProcessInstance"
		return historicProcessInstance
	}

	GenericCollectionModel<HistoricProcessInstance> toHistoricProcessInstances(String jsonText)throws ConvertException{
		log.info "toHistoricProcessInstances start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;

		Object[] datajson = (Object[])jsonResult.get("data")
		log.info "datajson:{} $datajson"

		Integer total =  (Integer)jsonResult.get("total")
		Integer start =  (Integer)jsonResult.get("start")
		Integer ssize =  (Integer)jsonResult.get("size")
		String ssort =  (String)jsonResult.get("sort")
		String order =  (String)jsonResult.get("order")

		GenericCollectionModel<HistoricProcessInstance> historicProcessInstances = new GenericCollectionModel<HistoricProcessInstance>(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single HistoricProcessInstance:{} "+it
			HistoricProcessInstance historicProcessInstance = new HistoricProcessInstance(
					id:it.id,
					processDefinitionUrl:it.processDefinitionUrl,
					businessKey:it.businessKey,
					processDefinitionId:it.processDefinitionId,
					startUserId:it.startUserId,
					startActivityId:it.startActivityId,
					endActivityId:it.endActivityId,
					deleteReason:it.deleteReason,
					superProcessInstanceId:it.superProcessInstanceId,
					url:it.url,
					tenantId:it.tenantId)

			if(it.durationInMillis){
				historicProcessInstance.durationInMillis = (Long)it.durationInMillis
			}

			if(it.startTime){
				historicProcessInstance.startTime = sdf.parse(it.startTime)
			}

			if(it.endTime){
				historicProcessInstance.endTime = sdf.parse(it.endTime)
			}
			if(it.variables){
				historicProcessInstance.variables = generalModelJSONConverter.toVariables(it.variables)
			}

			historicProcessInstances.addModel(historicProcessInstance)
		}
		log.info "toHistoricProcessInstances end:{} $historicProcessInstances"
		return historicProcessInstances
	}

	List<HistoricProcessIdentity> toHistoricProcessIdentities(String jsonText)throws ConvertException{
		log.info "toHistoricProcessIdentities start:{} $jsonText"
		List<HistoricProcessIdentity> historicProcessIdentities = new ArrayList<HistoricProcessIdentity>();
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object[] listResult = (Object[])jsonSlurper.parseText(jsonText);
		listResult.each {
			println "single resource:{} "+it
			HistoricProcessIdentity historicProcessIdentity = new HistoricProcessIdentity(userId:it.userId,
			groupId:it.groupId,
			taskId:it.taskId,
			taskUrl:it.taskUrl,
			processInstanceId:it.processInstanceId,
			processInstanceUrl:it.processInstanceUrl,
			type:it.type
			)

			historicProcessIdentities.add(historicProcessIdentity)
		}
		return historicProcessIdentities
	}

	HistoricProcessInstanceComment toHistoricProcessInstanceComment(String jsonText)throws ConvertException{
		log.info "toHistoricProcessInstanceComment start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String id = (String) jsonResult.get("id");
		String message = (String) jsonResult.get("message");
		String author = (String) jsonResult.get("author");
		String url = (String) jsonResult.get("url");

		HistoricProcessInstanceComment historicProcessInstanceComment = new HistoricProcessInstanceComment(
				id:id,
				url:url,
				message:message,
				author:author
				)

		log.info "toHistoricProcessInstanceComment end:{} $historicProcessInstanceComment"
		return historicProcessInstanceComment
	}

	List<HistoricProcessInstanceComment> toHistoricProcessInstanceComments(String jsonText)throws ConvertException{
		log.info "toHistoricProcessInstanceComments start:{} $jsonText"
		List<HistoricProcessInstanceComment> historicProcessInstanceComments = new ArrayList<HistoricProcessInstanceComment>();
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object[] listResult = (Object[])jsonSlurper.parseText(jsonText);
		listResult.each {
			println "single historicProcessInstanceComment:{} "+it
			HistoricProcessInstanceComment historicProcessInstanceComment = new HistoricProcessInstanceComment(
					id:it.id,
					url:it.url,
					message:it.message,
					author:it.author
					)

			historicProcessInstanceComments.add(historicProcessInstanceComment)
		}
		return historicProcessInstanceComments
	}

	HistoricTaskInstance toHistoricTaskInstance(String jsonText)throws ConvertException{
		log.info "toHistoricTaskInstance start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String id = (String) jsonResult.get("id");
		String processDefinitionId = (String) jsonResult.get("processDefinitionId");
		String processDefinitionUrl = (String) jsonResult.get("processDefinitionUrl");
		String url = (String) jsonResult.get("url");

		String processInstanceId = (String) jsonResult.get("processInstanceId");
		String processInstanceUrl = (String) jsonResult.get("processInstanceUrl");
		String executionId = (String) jsonResult.get("executionId");

		String name = (String) jsonResult.get("name");
		String description = (String) jsonResult.get("description");
		String deleteReason = (String) jsonResult.get("deleteReason");

		String owner = (String) jsonResult.get("owner");
		String assignee = (String) jsonResult.get("assignee");
		String startTimeJson = (String) jsonResult.get("startTime");
		String endTimeJson = (String) jsonResult.get("endTime");

		long durationInMillis = (Long) jsonResult.get("durationInMillis");
		long workTimeInMillis = (Long) jsonResult.get("workTimeInMillis");

		String claimTimeJson = (String) jsonResult.get("claimTime");
		String dueDateJson = (String) jsonResult.get("dueDate");

		String taskDefinitionKey = (String) jsonResult.get("taskDefinitionKey");
		String formKey = (String) jsonResult.get("formKey");
		Integer priority = (Integer) jsonResult.get("priority");

		String parentTaskId = (String) jsonResult.get("parentTaskId");
		String tenantId = (String) jsonResult.get("tenantId");
		String variablesJson = (String) jsonResult.get("variables");

		HistoricTaskInstance historicTaskInstance = new HistoricTaskInstance(
				id:id,
				url:url,
				processDefinitionId:processDefinitionId,
				processDefinitionUrl:processDefinitionUrl,
				processInstanceId:processInstanceId,
				processInstanceUrl:processInstanceUrl,
				executionId:executionId,
				name:name,
				description:description,
				deleteReason:deleteReason,
				owner:owner,
				assignee:assignee,
				durationInMillis:durationInMillis,
				workTimeInMillis:workTimeInMillis,
				taskDefinitionKey:taskDefinitionKey,
				formKey:formKey,
				priority:priority,
				parentTaskId:parentTaskId,
				tenantId:tenantId)

		if(variablesJson){
			historicTaskInstance.variables = generalModelJSONConverter.toVariables(variablesJson)
		}

		if(startTimeJson){
			historicTaskInstance.startTime = sdf.parse(startTimeJson)
		}

		if(endTimeJson){
			historicTaskInstance.endTime = sdf.parse(endTimeJson)
		}

		if(claimTimeJson){
			historicTaskInstance.claimTime = sdf.parse(claimTimeJson)
		}

		if(dueDateJson){
			historicTaskInstance.dueDate = sdf.parse(dueDateJson)
		}

		log.info "toHistoricTaskInstance end:{} $historicTaskInstance"
		return historicTaskInstance
	}

	GenericCollectionModel<HistoricTaskInstance> toHistoricTaskInstances(String jsonText)throws ConvertException{
		log.info "toHistoricProcessInstances start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;

		Object[] datajson = (Object[])jsonResult.get("data")
		log.info "datajson:{} $datajson"

		Integer total =  (Integer)jsonResult.get("total")
		Integer start =  (Integer)jsonResult.get("start")
		Integer ssize =  (Integer)jsonResult.get("size")
		String ssort =  (String)jsonResult.get("sort")
		String order =  (String)jsonResult.get("order")

		GenericCollectionModel<HistoricTaskInstance> historicTaskInstances = new GenericCollectionModel<HistoricTaskInstance>(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single HistoricTaskInstance:{} "+it
			HistoricTaskInstance historicTaskInstance = new HistoricTaskInstance(
					id:it.id,
					url:it.url,
					processDefinitionId:it.processDefinitionId,
					processDefinitionUrl:it.processDefinitionUrl,
					processInstanceId:it.processInstanceId,
					processInstanceUrl:it.processInstanceUrl,
					executionId:it.executionId,
					name:it.name,
					description:it.description,
					deleteReason:it.deleteReason,
					owner:it.owner,
					assignee:it.assignee,
					taskDefinitionKey:it.taskDefinitionKey,
					formKey:it.formKey,
					priority:it.priority,
					parentTaskId:it.parentTaskId,
					tenantId:it.tenantId)

			if(it.durationInMillis){
				historicTaskInstance.durationInMillis = (Long)it.durationInMillis
			}

			if(it.workTimeInMillis){
				historicTaskInstance.workTimeInMillis = (Long)it.workTimeInMillis
			}

			if(it.variables){
				historicTaskInstance.variables = generalModelJSONConverter.toVariables(it.variables)
			}

			if(it.startTime){
				historicTaskInstance.startTime = sdf.parse(it.startTime)
			}

			if(it.endTime){
				historicTaskInstance.endTime = sdf.parse(it.endTime)
			}

			if(it.claimTime){
				historicTaskInstance.claimTime = sdf.parse(it.claimTime)
			}

			if(it.dueDate){
				historicTaskInstance.dueDate = sdf.parse(it.dueDate)
			}

			historicTaskInstances.addModel(historicTaskInstance)
		}
		log.info "toHistoricProcessInstances end:{} $historicTaskInstances"
		return historicTaskInstances
	}

	GenericCollectionModel<HistoricVariableInstance> toHistoricVariableInstances(String jsonText)throws ConvertException{
		log.info "toHistoricVariableInstances start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;

		Object[] datajson = (Object[])jsonResult.get("data")
		log.info "datajson:{} $datajson"

		Integer total =  (Integer)jsonResult.get("total")
		Integer start =  (Integer)jsonResult.get("start")
		Integer ssize =  (Integer)jsonResult.get("size")
		String ssort =  (String)jsonResult.get("sort")
		String order =  (String)jsonResult.get("order")

		GenericCollectionModel<HistoricVariableInstance> historicVariableInstances = new GenericCollectionModel<HistoricVariableInstance>(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single HistoricVariableInstance:{} "+it
			HistoricVariableInstance historicVariableInstance = new HistoricVariableInstance(
					id:it.id,
					processInstanceId:it.processInstanceId,
					processInstanceUrl:it.processInstanceUrl,
					taskId:it.taskId)

			if(it.variable){
				println "variable name json:{} $it.variable.name"
				Variable variable = new Variable(
						name:it.variable.name,
						scope:it.variable.scope,
						value:it.variable.value,
						type:it.variable.type
						)
				historicVariableInstance.addVariable(variable)
			}

			historicVariableInstances.addModel(historicVariableInstance)
		}
		log.info "toHistoricVariableInstances end:{} $historicVariableInstances"
		return historicVariableInstances
	}
}
