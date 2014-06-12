package nz.co.activiti.tutorial.rest

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import nz.co.activiti.tutorial.model.Party

import org.springframework.stereotype.Component
@Component
@Slf4j
class GeneralModelJSONConverter {
	Set<Party> toParties(String jsonText){
		log.info "toParties start:{} $jsonText"
		Set<Party> parties = new HashSet<Party>()
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object[] listResult = (Object[])jsonSlurper.parseText(jsonText);
		listResult.each {
			println "single party:{} "+it
			Party candidate = new Party(
					url:it.url,
					user:it.user,
					group:it.group,
					type:it.type
					)
			parties.add(candidate)
		}
		log.info "toParties end:{} "
		return parties
	}

	Party toParty(String jsonText){
		log.info "toParty start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String url = (String) jsonResult.get("url");
		String user = (String) jsonResult.get("user");
		String group = (String) jsonResult.get("group");
		String type = (String) jsonResult.get("type");

		Party party = new Party(
				url:url,
				user:user,
				group:group,
				type:type
				)
		log.info "toParty end:{} "
		return party
	}
}
