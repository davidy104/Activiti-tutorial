package nz.co.activiti.tutorial.rest;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;

@Component
public class ActivitiRestClientAccessor extends JerseyClientSupport {

	@Value("${activiti.auth.userid}")
	private String userId;

	@Value("${activiti.auth.password:kermit}")
	private String password;

	@PostConstruct
	private void init() {
		client.addFilter(new HTTPBasicAuthFilter(userId, password));
	}

	public String getUserId() {
		return userId;
	}

	public String getPassword() {
		return password;
	}

}
