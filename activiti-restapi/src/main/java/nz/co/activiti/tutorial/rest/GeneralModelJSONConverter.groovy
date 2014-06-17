package nz.co.activiti.tutorial.rest

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import nz.co.activiti.tutorial.ConvertException
import nz.co.activiti.tutorial.rest.model.Identity
import nz.co.activiti.tutorial.rest.model.IdentityType
import nz.co.activiti.tutorial.rest.model.Variable

import org.springframework.stereotype.Component
@Component
@Slf4j
class GeneralModelJSONConverter {

	Set<Identity> toIdentities(String jsonText){
		log.info "toIdentities start:{} $jsonText"
		Set<Identity> parties = new HashSet<Identity>()
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object[] listResult = (Object[])jsonSlurper.parseText(jsonText);
		listResult.each {
			println "single identity:{} "+it
			IdentityType identityType
			switch(it.type){
				case "assignee":
					identityType = IdentityType.assignee
					break
				case "owner":
					identityType = IdentityType.owner
					break
				case "participant":
					identityType = IdentityType.participant
					break
				case "starter":
					identityType = IdentityType.starter
					break
				default:
					identityType = IdentityType.candidate
			}

			Identity candidate = new Identity(
					url:it.url,
					user:it.user,
					group:it.group,
					type:identityType
					)
			parties.add(candidate)
		}
		log.info "toIdentities end:{} "
		return parties
	}

	Identity toIdentity(String jsonText){
		log.info "toIdentity start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String url = (String) jsonResult.get("url");
		String user = (String) jsonResult.get("user");
		String group = (String) jsonResult.get("group");
		String type = (String) jsonResult.get("type");


		IdentityType identityType
		if(type){
			switch(type){
				case "assignee":
					identityType = IdentityType.assignee
					break
				case "owner":
					identityType = IdentityType.owner
					break
				case "participant":
					identityType = IdentityType.participant
					break
				case "starter":
					identityType = IdentityType.starter
					break
				default:
					identityType = IdentityType.candidate
			}
		}

		Identity identity = new Identity(
				url:url,
				user:user,
				group:group,
				type:identityType
				)
		log.info "toIdentity end:{} "
		return identity
	}

	Variable toVariable(String jsonText){
		log.info "toVariable start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String name = (String) jsonResult.get("name");
		String scope = (String) jsonResult.get("scope");
		String value = (String) jsonResult.get("value");
		String type = (String) jsonResult.get("type");

		Variable variable = new Variable(
				name:name,
				scope:scope,
				value:value,
				type:type
				)
		log.info "toVariable end:{} "
		return variable
	}


	List<Variable> toVariables(String jsonText){
		log.info "toVariables start:{} $jsonText"
		List<Variable> variables = new ArrayList<Variable>()
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object[] listResult = (Object[])jsonSlurper.parseText(jsonText);
		listResult.each {
			println "single variable:{} "+it
			Variable variable = new Variable(
					name:it.name,
					scope:it.scope,
					value:it.value,
					type:it.type
					)
			variables.add(variable)
		}
		log.info "toVariables end:{} "
		return variables
	}

	String toVariablesJson(List<Variable> variablesParam)throws ConvertException{
		def builder = new JsonBuilder()
		builder(
				variablesParam.collect { variable->
					[
						name:variable.name,
						scope:variable.scope,
						value:variable.value,
						type:variable.type,
					]
				}
				)
		return builder.toString()
	}

	String toVariableJson(Variable variable,ActionType action)throws ConvertException{
		def json = new JsonBuilder()
		json{
			name variable.name
			scope variable.scope
			value variable.value
			if(action != ActionType.update){
				type variable.type
			}
		}
		return json.toString();
	}
}
