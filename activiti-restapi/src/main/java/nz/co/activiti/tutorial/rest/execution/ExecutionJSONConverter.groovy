package nz.co.activiti.tutorial.rest.execution

import java.util.List;

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import javax.annotation.Resource

import nz.co.activiti.tutorial.ConvertException
import nz.co.activiti.tutorial.model.execution.Execution
import nz.co.activiti.tutorial.model.execution.ExecutionActionRequest
import nz.co.activiti.tutorial.model.execution.Executions
import nz.co.activiti.tutorial.rest.GeneralModelJSONConverter

import org.springframework.stereotype.Component

@Component
@Slf4j
class ExecutionJSONConverter {

	@Resource
	GeneralModelJSONConverter generalModelJSONConverter

	String toExecutionActionRequestJson(ExecutionActionRequest executionActionRequest) throws ConvertException{
		def json = new JsonBuilder()
		String variablesParam
		if(executionActionRequest.variables){
			variablesParam = generalModelJSONConverter.toVariablesJson(executionActionRequest.variables)
		}

		json{
			action executionActionRequest.action.name()
			signalName executionActionRequest.signalName
			messageName executionActionRequest.messageName
			if(variablesParam){
				variables variablesParam
			}
		}
		return json.toString();
	}

	String[] toActiveActivities(String jsonText) throws ConvertException{
		String[] activities = (String[])new JsonSlurper().parseText(jsonText);
		return activities
	}

	Execution toExecution(String jsonText)throws ConvertException{
		log.info "toExecution start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String id = (String) jsonResult.get("id");
		String processInstanceId = (String) jsonResult.get("processInstanceId");
		String processInstanceUrl = (String) jsonResult.get("processInstanceUrl");
		String parentId = (String) jsonResult.get("parentId");
		String parentUrl = (String) jsonResult.get("parentUrl");
		String url = (String) jsonResult.get("url");

		String activityId = (String) jsonResult.get("activityId");
		String tenantId = (String) jsonResult.get("tenantId");
		String suspended = (Boolean) jsonResult.get("suspended");

		Execution execution = new Execution(id:id,
		processInstanceId:processInstanceId,
		processInstanceUrl:processInstanceUrl,
		parentId:parentId,
		parentUrl:parentUrl,
		url:url,
		activityId:activityId,
		suspended:suspended,
		tenantId:tenantId)

		log.info "toExecution end:{} $execution"
		return execution
	}

	Executions toExecutions(String jsonText) throws ConvertException{
		log.info "toExecutions start:{} $jsonText"
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

		Executions executions = new Executions(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single execution:{} "+it
			Execution execution = new Execution(
					id:it.id,
					processInstanceId:it.processInstanceId,
					processInstanceUrl:it.processInstanceUrl,
					parentId:it.parentId,
					parentUrl:it.parentUrl,
					url:it.url,
					activityId:it.activityId,
					suspended:it.suspended,
					tenantId:it.tenantId)
			executions.addExecution(execution)
		}
		log.info "toExecutions end:{} $executions"
		return executions
	}
}
