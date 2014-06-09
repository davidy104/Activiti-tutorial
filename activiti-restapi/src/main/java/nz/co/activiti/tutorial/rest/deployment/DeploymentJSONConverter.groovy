package nz.co.activiti.tutorial.rest.deployment

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j

import org.springframework.stereotype.Component

@Component
@Slf4j
class DeploymentJSONConverter {

	DeploymentResponse toDeploymentResponse(String jsonText){
		log.info "toProcessDefinitionResponse start:{} $jsonText"
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
	}
}
