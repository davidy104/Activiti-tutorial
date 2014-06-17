package nz.co.activiti.tutorial.rest.ds.processdefinition

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel
import nz.co.activiti.tutorial.rest.model.processdefinition.ProcessDefinition

import org.springframework.stereotype.Component

@Component
@Slf4j
class ProcessDefinitionJSONConverter {

	ProcessDefinition toProcessDefinition(String jsonText){
		log.info "toProcessDefinition start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String id = (String) jsonResult.get("id");
		String url = (String) jsonResult.get("url");
		String key = (String) jsonResult.get("key");
		String name = (String) jsonResult.get("name");
		Integer version = (Integer) jsonResult.get("version");
		String description = (String) jsonResult.get("description");

		String deploymentId = (String) jsonResult.get("deploymentId");
		String deploymentUrl = (String) jsonResult.get("deploymentUrl");
		String resource = (String) jsonResult.get("resource");
		String diagramResource = (String) jsonResult.get("diagramResource");
		String category = (String) jsonResult.get("category");

		Boolean graphicalNotationDefined = (Boolean) jsonResult.get("graphicalNotationDefined");
		Boolean suspended = (Boolean) jsonResult.get("suspended");
		Boolean startFormDefined = (Boolean) jsonResult.get("startFormDefined");

		ProcessDefinition processDefinition = new ProcessDefinition(
				id:id,
				url:url,
				key:key,
				version:version,
				name:name,
				description:description,

				deploymentId:deploymentId,
				deploymentUrl:deploymentUrl,
				resource:resource,
				diagramResource:diagramResource,
				category:category,

				graphicalNotationDefined:graphicalNotationDefined,
				suspended:suspended,
				startFormDefined:startFormDefined)

		log.info "toProcessDefinition end:{} $processDefinition"
		return processDefinition
	}

	GenericCollectionModel<ProcessDefinition> toProcessDefinitions(String jsonText){
		log.info "toProcessDefinitions start:{} $jsonText"

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

		GenericCollectionModel<ProcessDefinition> processDefinitions = new GenericCollectionModel<ProcessDefinition>(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single definition:{} "+it
			ProcessDefinition processDefinition = new ProcessDefinition(
					id:it.id,
					url:it.url,
					key:it.key,
					version:it.version,
					name:it.name,
					description:it.description,

					deploymentId:it.deploymentId,
					deploymentUrl:it.deploymentUrl,
					resource:it.resource,
					diagramResource:it.diagramResource,
					category:it.category,

					graphicalNotationDefined:it.graphicalNotationDefined,
					suspended:it.suspended,
					startFormDefined:it.startFormDefined)


			processDefinitions.addModel(processDefinition)
		}
		log.info "toProcessDefinitions end:{}"
		return processDefinitions
	}
}
