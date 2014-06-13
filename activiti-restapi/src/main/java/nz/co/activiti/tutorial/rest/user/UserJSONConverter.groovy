package nz.co.activiti.tutorial.rest.user

import groovy.json.JsonSlurper
import groovy.util.logging.Slf4j
import nz.co.activiti.tutorial.ConvertException
import nz.co.activiti.tutorial.model.user.User
import nz.co.activiti.tutorial.model.user.Users

import org.springframework.stereotype.Component
@Component
@Slf4j
class UserJSONConverter {

	User toUser(String jsonText) throws ConvertException {
		log.info "toUser start:{} $jsonText"
		JsonSlurper jsonSlurper = new JsonSlurper();
		Object result = jsonSlurper.parseText(jsonText);
		Map jsonResult = (Map) result;
		String id = (String) jsonResult.get("id");
		String firstName = (String) jsonResult.get("firstName");
		String lastName = (String) jsonResult.get("lastName");
		String url = (String) jsonResult.get("url");
		String email = (String) jsonResult.get("email");

		User user = new User(
				id:id,
				url:url,
				firstName:firstName,
				lastName:lastName,
				email:email
				)

		log.info "toUser end:{} $user"
		return user
	}

	Users toUsers(String jsonText)throws ConvertException {
		log.info "toUsers start:{} $jsonText"

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

		Users users = new Users(total:total,
		start:start,
		size:ssize,
		sort:ssort,
		order:order
		)

		datajson.each {
			println "single user:{} "+it
			User user = new User(
					id:it.id,
					url:it.url,
					firstName:it.firstName,
					lastName:it.lastName,
					email:it.email
					)


			users.addUser(user)
		}
		log.info "toUsers end:{}"
		return users
	}
}
