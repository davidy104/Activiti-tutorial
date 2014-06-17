package nz.co.activiti.tutorial.rest.deployment

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import nz.co.activiti.tutorial.ConvertException
import nz.co.activiti.tutorial.model.GenericCollectionModel
import nz.co.activiti.tutorial.model.deployment.Deployment
import nz.co.activiti.tutorial.model.deployment.DeploymentResource

import org.springframework.stereotype.Component

@Component
@Slf4j
class DeploymentJSONConverter {

	Deployment toDeployment(String jsonText)  throws ConvertException{
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

		Deployment response = new Deployment(id:id,
		name:name,
		deploymentTime:deploymentTime,
		category:category,
		url:url,
		tenantId:tenantId)

		log.info "after convert:{} $response"
		return response
	}

	List<DeploymentResource> toDeploymentResources(String jsonText)  throws ConvertException{
		log.info "toDeploymentResources start:{} $jsonText"
		List<DeploymentResource> deploymentResources = new ArrayList<DeploymentResource>();
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object[] listResult = (Object[])jsonSlurper.parseText(jsonText);
		listResult.each {
			println "single resource:{} "+it
			DeploymentResource deploymentResource = new DeploymentResource(id:it.id,
			url:it.url,
			dataUrl:it.dataUrl,
			mediaType:it.mediaType,
			type:it.type
			)
			deploymentResources.add(deploymentResource)
		}
		return deploymentResources
	}


	DeploymentResource toDeploymentResource(String jsonText)  throws ConvertException{
		log.info "toDeploymentResource start:{} $jsonText"

		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String id = (String) jsonResult.get("id");
		String url = (String) jsonResult.get("url");
		String dataUrl = (String) jsonResult.get("dataUrl");
		String mediaType = (String) jsonResult.get("mediaType");
		String type = (String) jsonResult.get("type");

		DeploymentResource deploymentResource = new DeploymentResource(id:id,
		url:url,
		dataUrl:dataUrl,
		mediaType:mediaType,
		type:type
		)

		log.info "toDeploymentResource end:{} $deploymentResource"
	}


	GenericCollectionModel<Deployment> toDeployments(String jsonText) throws ConvertException{
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

		GenericCollectionModel<Deployment> deployments = new GenericCollectionModel<Deployment>(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single response:{} "+it
			Deployment deployment = new Deployment(id:it.id,
			name:it.name,
			deploymentTime:it.deploymentTime,
			category:it.category,
			url:it.url,
			tenantId:it.tenantId)
			deployments.addModel(deployment)
		}
		log.info "after convert:{} $deployments"
		return deployments
	}
}
