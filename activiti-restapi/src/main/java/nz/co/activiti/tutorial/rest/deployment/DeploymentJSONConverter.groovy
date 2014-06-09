package nz.co.activiti.tutorial.rest.deployment

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import org.springframework.stereotype.Component

@Component
@Slf4j
class DeploymentJSONConverter {

	DeploymentResponse toDeploymentResponse(String jsonText){
		log.info "toDeploymentResponse start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String id = (String) jsonResult.get("id");

		String name = (String) jsonResult.get("name");
		String deploymentTime = (String) jsonResult.get("deploymentTime");
		String category = (String) jsonResult.get("category");
		String url = (String) jsonResult.get("url");
		String tenantId = (String) jsonResult.get("tenantId");

		DeploymentResponse response = new DeploymentResponse(id:id,
		name:name,
		deploymentTime:deploymentTime,
		category:category,
		url:url,
		tenantId:tenantId)

		log.info "after convert:{} $response"
		return response
	}

	DeploymentsResponse toDeploymentsResponse(String jsonText){
		log.info "toDeploymentsResponse start:{} $jsonText"
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

		DeploymentsResponse deploymentsResponse = new DeploymentsResponse(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single response:{} "+it
			DeploymentResponse deploymentResponse = new DeploymentResponse(id:it.id,
			name:it.name,
			deploymentTime:it.deploymentTime,
			category:it.category,
			url:it.url,
			tenantId:it.tenantId)
			deploymentsResponse.addData(deploymentResponse)
		}
		log.info "after convert:{} $deploymentsResponse"
		return deploymentsResponse
	}
}
