package nz.co.activiti.tutorial.rest.ds.task

import java.text.SimpleDateFormat;

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.activiti.tutorial.ConvertException
import nz.co.activiti.tutorial.rest.GeneralModelJSONConverter
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel
import nz.co.activiti.tutorial.rest.model.task.Task
import nz.co.activiti.tutorial.rest.model.task.TaskActionRequest
import nz.co.activiti.tutorial.rest.model.task.TaskComment
import nz.co.activiti.tutorial.rest.model.task.TaskEvent

import org.springframework.stereotype.Component
@Component
@Slf4j
class TaskJSONConverter {

	@Resource
	GeneralModelJSONConverter generalModelJSONConverter

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

	String toUpdateTaskRequestJson(Task task)throws ConvertException{
		def json = new JsonBuilder()
		json{
			assignee task.assignee
			delegationState task.delegationState
			description  task.description
			dueDate  task.dueDate
			name  task.name
			owner task.owner
			parentTaskId  task.parentTask
			priority  task.priority
		}
		return json.toString()
	}

	String toTaskActionRequestJson(TaskActionRequest taskActionRequest)throws ConvertException{
		def json = new JsonBuilder()
		String variablesParam

		json{
			action taskActionRequest.action.name()
			if(taskActionRequest.assignee){
				assignee taskActionRequest.assignee
			}
			if(taskActionRequest.variables){
				variables(
						taskActionRequest.variables.collect { variable->
							[
								name:variable.name,
								value:variable.value,
							]
						}
						)
			}
		}
		return json.toString();
	}

	String toTaskVariablesJson(Map variableMap)throws ConvertException{
		def json = new JsonBuilder()
		json{
			variableMap.each{ k, v ->
				println "${k}:${v}"
				k v
			}
		}
	}




	TaskComment toTaskComment(String jsonText)throws ConvertException{
		log.info "toTaskComment start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);

		Map jsonResult = (Map) result;
		String id = (String) jsonResult.get("id");
		String url = (String) jsonResult.get("url");
		String message = (String) jsonResult.get("message");
		String author = (String) jsonResult.get("author");

		TaskComment taskComment = new TaskComment(
				id:id,
				url:url,
				message:message,
				author:author)

		log.info "toTaskComment end:{} $taskComment"
		return taskComment
	}

	List<TaskComment> toTaskComments(String jsonText) throws ConvertException{
		log.info "toTaskComments start:{} $jsonText"
		List<TaskComment> taskComments = new ArrayList<TaskComment>()
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object[] listResult = (Object[])jsonSlurper.parseText(jsonText);
		listResult.each {
			println "single taskComment:{} "+it
			TaskComment taskComment = new TaskComment(
					id:it.id,
					url:it.url,
					message:it.message,
					author:it.author)
			taskComments.add(taskComment)
		}
		log.info "toTaskComments end:{} "
		return taskComments
	}


	TaskEvent toTaskEvent(String jsonText)throws ConvertException{
		log.info "toTaskEvent start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);

		Map jsonResult = (Map) result;
		String action = (String) jsonResult.get("action");
		String url = (String) jsonResult.get("url");
		String id = (String) jsonResult.get("id");
		String taskUrl = (String) jsonResult.get("taskUrl");

		def timeStr = jsonResult.get("time");
		String userId = (String) jsonResult.get("userId");
		String[] message = (String[]) jsonResult.get("message");


		TaskEvent taskEvent = new TaskEvent(
				id:id,
				url:url,
				message:message,
				action:action,
				taskUrl:taskUrl,
				userId:userId)

		if(timeStr){
			taskEvent.time = sdf.parse(timeStr)
		}

		log.info "toTaskEvent end:{} $taskEvent"
		return taskEvent
	}

	List<TaskEvent> toTaskEvents(String jsonText) throws ConvertException{
		log.info "toTaskEvents start:{} $jsonText"
		List<TaskEvent> taskEvents = new ArrayList<TaskEvent>()
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object[] listResult = (Object[])jsonSlurper.parseText(jsonText);
		listResult.each {
			println "single taskEvent:{} "+it
			TaskEvent taskEvent = new TaskEvent(
					id:it.id,
					url:it.url,
					message:it.message,
					action:it.action,
					taskUrl:it.taskUrl,
					userId:it.userId)

			if(it.time){
				taskEvent.time = sdf.parse(it.time)
			}

			taskEvents.add(taskEvent)
		}
		log.info "toTaskEvents end:{} "
		return taskEvents
	}


	Task toTask(String jsonText)throws ConvertException{
		log.info "toTask start:{} $jsonText"
		Task task = null
		if(jsonText){
			JsonSlurper jsonSlurper = new JsonSlurper();
			Object result = jsonSlurper.parseText(jsonText);

			Map jsonResult = (Map) result;
			String id = (String) jsonResult.get("id");
			String url = (String) jsonResult.get("url");
			String assignee = (String) jsonResult.get("assignee");
			def createTimeStr = jsonResult.get("createTime");
			String delegationState = (String) jsonResult.get("delegationState");
			String description = (String)jsonResult.get("description")

			def dueDateStr = jsonResult.get("dueDate")
			String execution = (String)jsonResult.get("execution")
			String name = (String)jsonResult.get("name")
			String owner = (String)jsonResult.get("owner")

			String parentTask = (String)jsonResult.get("parentTask")
			Integer priority = (Integer)jsonResult.get("priority")
			String processDefinition = (String)jsonResult.get("processDefinition")
			String processInstance = (String)jsonResult.get("processInstance")

			boolean suspended = (Boolean)jsonResult.get("suspended")
			String taskDefinitionKey = (String)jsonResult.get("taskDefinitionKey")
			String tenantId = (String) jsonResult.get("tenantId");

			task = new Task(
					id:id,
					assignee:assignee,
					delegationState:delegationState,
					suspended:suspended,
					description:description,
					execution:execution,
					name:name,
					owner:owner,
					parentTask:parentTask,
					priority:priority,
					processDefinition:processDefinition,
					processInstance:processInstance,
					taskDefinitionKey:taskDefinitionKey,
					url:url,
					tenantId:tenantId)

			if(createTimeStr){
				task.createTime = sdf.parse(createTimeStr)
			}

			if(dueDateStr){
				task.dueDate = sdf.parse(dueDateStr)
			}
		}

		log.info "toTask end:{} $task"
		return task
	}

	GenericCollectionModel<Task> toTasks(String jsonText)throws ConvertException{
		log.info "toTasks start:{} $jsonText"

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

		GenericCollectionModel<Task> tasks = new GenericCollectionModel<Task>(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single task:{} "+it
			Task task = new Task(
					id:it.id,
					assignee:it.assignee,
					delegationState:it.delegationState,
					suspended:it.suspended,
					description:it.description,
					execution:it.execution,
					name:it.name,
					owner:it.owner,
					parentTask:it.parentTask,
					priority:it.priority,
					processDefinition:it.processDefinition,
					processInstance:it.processInstance,
					taskDefinitionKey:it.taskDefinitionKey,
					url:it.url,
					tenantId:it.tenantId)

			if(it.createTime){
				task.createTime = sdf.parse(it.createTime)
			}

			if(it.dueDate){
				task.dueDate = sdf.parse(it.dueDate)
			}

			tasks.addModel(task)
		}
		log.info "toTasks end:{}"
		return tasks
	}
}
