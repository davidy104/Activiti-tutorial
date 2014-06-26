package nz.co.activiti.tutorial.rest.ds.processinstance

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel
import nz.co.activiti.tutorial.rest.model.processinstance.ProcessInstance

import org.springframework.stereotype.Component

@Component
@Slf4j
class ProcessInstanceJSONConverter {

	String toStartProcessInstanceByIdJson(
			String processDefinitionIdParam, String businessKeyParam,
			Map<String, Object> variablesParam){
		log.info "toStartProcessInstanceByIdJson start:{}"
		def builder = new JsonBuilder()

		builder{
			processDefinitionId "${processDefinitionIdParam}"
			if(businessKeyParam){
				businessKey "${businessKeyParam}"
			}
			if(variablesParam){
				variables(
						variablesParam.collect {key, val ->
							[name:"${key}",value:"${val}"]
						}
						)
			}
		}
		return builder.toString()
	}

	String toStartProcessInstanceByKeyJson(
			String processDefinitionKeyParam, String businessKeyParam,
			Map<String, Object> variablesParam){
		log.info "toStartProcessInstanceByIdJson start:{}"
		def builder = new JsonBuilder()

		builder{
			processDefinitionKey "${processDefinitionKeyParam}"
			if(businessKeyParam){
				businessKey "${businessKeyParam}"
			}
			if(variablesParam){

				variables(
						variablesParam.collect {key, val ->
							[name:"${key}",value:"${val}"]
						}
						)
			}
		}
		return builder.toString()
	}


	ProcessInstance toProcessInstance(String jsonText){
		log.info "toProcessInstance start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);

		Map jsonResult = (Map) result;
		String id = (String) jsonResult.get("id");
		String url = (String) jsonResult.get("url");
		String processDefinitionUrl = (String) jsonResult.get("processDefinitionUrl");
		String businessKey = (String) jsonResult.get("businessKey");
		String activityId = (String) jsonResult.get("activityId");
		Boolean suspended = (Boolean)jsonResult.get("suspended")
		String tenantId = (String) jsonResult.get("tenantId");

		ProcessInstance processInstance = new ProcessInstance(
				id:id,
				processDefinitionUrl:processDefinitionUrl,
				businessKey:businessKey,
				activityId:activityId,
				suspended:suspended,
				url:url,
				tenantId:tenantId)

		log.info "toProcessInstance end:{} $processInstance"
		return processInstance
	}

	GenericCollectionModel<ProcessInstance> toProcessInstances(String jsonText) {
		log.info "toProcessInstances start:{} $jsonText"

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

		GenericCollectionModel<ProcessInstance> processInstances = new GenericCollectionModel<ProcessInstance>(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single processInstance:{} "+it
			ProcessInstance processInstance = new ProcessInstance(
					id:it.id,
					processDefinitionUrl:it.processDefinitionUrl,
					businessKey:it.businessKey,
					activityId:it.activityId,
					suspended:it.suspended,
					url:it.url,
					tenantId:it.tenantId)


			processInstances.addModel(processInstance)
		}
		log.info "toProcessInstances end:{}"
		return processInstances
	}
}
