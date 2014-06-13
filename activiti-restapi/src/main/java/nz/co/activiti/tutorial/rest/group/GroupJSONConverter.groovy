package nz.co.activiti.tutorial.rest.group

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import nz.co.activiti.tutorial.ConvertException
import nz.co.activiti.tutorial.model.group.Group
import nz.co.activiti.tutorial.model.group.Groups
import nz.co.activiti.tutorial.model.group.MemberShip
import nz.co.activiti.tutorial.rest.ActionType

import org.springframework.stereotype.Component
@Component
@Slf4j
class GroupJSONConverter {

	String toGroupJson(Group group,ActionType action)throws ConvertException{
		def json = new JsonBuilder()
		json{
			if(action == ActionType.create){
				id group.id
			}
			name group.name
			type group.type
		}
		return json.toString()
	}

	MemberShip toMemberShip(String jsonText) throws ConvertException {
		log.info "toGroup start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String userId = (String) jsonResult.get("userId");
		String groupId = (String) jsonResult.get("groupId");
		String url = (String) jsonResult.get("url");

		MemberShip memberShip = new MemberShip(
				userId:userId,
				groupId:groupId,
				url:url)

		log.info "toGroup end:{} $memberShip"
		return memberShip
	}


	Group toGroup(String jsonText)throws ConvertException{
		log.info "toGroup start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String id = (String) jsonResult.get("id");

		String name = (String) jsonResult.get("name");
		String url = (String) jsonResult.get("url");
		String type = (String) jsonResult.get("type");


		Group group = new Group(id:id,
		name:name,
		type:type,
		url:url)

		log.info "toGroup end:{} $group"
		return group
	}

	Groups toGroups(String jsonText) throws ConvertException{
		log.info "toGroups start:{} $jsonText"
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

		Groups groups = new Groups(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single group:{} "+it
			Group group = new Group(id:it.id,
			name:it.name,
			type:it.type,
			url:it.url)
			groups.addGroup(group)
		}
		log.info "toGroups end:{} $groups"
		return groups
	}
}
