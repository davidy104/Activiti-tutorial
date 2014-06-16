package nz.co.activiti.tutorial.rest.task

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.activiti.tutorial.ConvertException
import nz.co.activiti.tutorial.model.group.Group;
import nz.co.activiti.tutorial.model.task.Task
import nz.co.activiti.tutorial.model.task.TaskActionRequest
import nz.co.activiti.tutorial.model.task.TaskComment
import nz.co.activiti.tutorial.model.task.TaskEvent
import nz.co.activiti.tutorial.model.task.Tasks
import nz.co.activiti.tutorial.rest.ActionType;
import nz.co.activiti.tutorial.rest.GeneralModelJSONConverter

import org.springframework.stereotype.Component
@Component
@Slf4j
class TaskJSONConverter {

	@Resource
	GeneralModelJSONConverter generalModelJSONConverter

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
		if(taskActionRequest.variables){
			variablesParam = generalModelJSONConverter.toVariablesJson(taskActionRequest.variables)
		}

		json{
			action taskActionRequest.action.name()
			assignee taskActionRequest.assignee
			if(variablesParam){
				variables variablesParam
			}
		}
		return json.toString();
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

		String time = (String) jsonResult.get("time");
		String userId = (String) jsonResult.get("userId");
		String[] message = (String[]) jsonResult.get("message");


		TaskEvent taskEvent = new TaskEvent(
				id:id,
				url:url,
				message:message,
				action:action,
				taskUrl:taskUrl,
				time:time,
				userId:userId)

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
					time:it.time,
					userId:it.userId)
			taskEvents.add(taskEvent)
		}
		log.info "toTaskEvents end:{} "
		return taskEvents
	}


	Task toTask(String jsonText)throws ConvertException{
		log.info "toTask start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);

		Map jsonResult = (Map) result;
		String id = (String) jsonResult.get("id");
		String url = (String) jsonResult.get("url");
		String assignee = (String) jsonResult.get("assignee");
		String createTime = (String) jsonResult.get("createTime");
		String delegationState = (String) jsonResult.get("delegationState");
		String description = (String)jsonResult.get("description")

		String dueDate = (String)jsonResult.get("dueDate")
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

		Task task = new Task(
				id:id,
				assignee:assignee,
				delegationState:delegationState,
				createTime:createTime,
				suspended:suspended,
				description:description,
				dueDate:dueDate,
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

		log.info "toTask end:{} $task"
		return task
	}

	Tasks toTasks(String jsonText)throws ConvertException{
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

		Tasks tasks = new Tasks(total:total,
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
					createTime:it.createTime,
					suspended:it.suspended,
					description:it.description,
					dueDate:it.dueDate,
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
			tasks.addTask(task)
		}
		log.info "toTasks end:{}"
		return tasks
	}
}
