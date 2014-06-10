package nz.co.activiti.tutorial.rest.processdefinition

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import org.springframework.stereotype.Component

@Component
@Slf4j
class ProcessDefinitionJSONConverter {

	Set<Candidate> toCandidates(String jsonText){
		log.info "toCandidates start:{} $jsonText"
		Set<Candidate> candidates = new HashSet<Candidate>()
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object[] listResult = (Object[])jsonSlurper.parseText(jsonText);
		listResult.each {
			println "single candidate:{} "+it
			Candidate candidate = new Candidate(
					url:it.url,
					user:it.user,
					group:it.group,
					type:it.type
					)
			candidates.add(candidate)
		}
		log.info "toCandidates end:{} "
		return candidates
	}

	Candidate toCandidate(String jsonText){
		log.info "toCandidate start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String url = (String) jsonResult.get("url");
		String user = (String) jsonResult.get("user");
		String group = (String) jsonResult.get("group");
		String type = (String) jsonResult.get("type");

		Candidate candidate = new Candidate(
				url:url,
				user:user,
				group:group,
				type:type
				)
		log.info "toCandidate end:{} "
		return candidate
	}

	ProcessDefinitionResponse toProcessDefinitionResponse(String jsonText){
		log.info "toProcessDefinitionResponse start:{} $jsonText"
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

		ProcessDefinitionResponse processDefinitionResponse = new ProcessDefinitionResponse(
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

		log.info "toProcessDefinitionResponse end:{} $processDefinitionResponse"
		return processDefinitionResponse
	}

	ProcessDefinitionsResponse toProcessDefinitionsResponse(String jsonText){
		log.info "toProcessDefinitionsResponse start:{} $jsonText"

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

		ProcessDefinitionsResponse processDefinitionsResponse = new ProcessDefinitionsResponse(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single definition:{} "+it
			ProcessDefinitionResponse processDefinitionResponse = new ProcessDefinitionResponse(
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


			processDefinitionsResponse.addProcessDefinitionResponse(processDefinitionResponse)
		}
		log.info "toProcessDefinitionsResponse end:{}"
		return processDefinitionsResponse
	}
}
