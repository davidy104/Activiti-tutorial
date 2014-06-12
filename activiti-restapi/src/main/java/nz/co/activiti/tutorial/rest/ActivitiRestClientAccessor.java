package nz.co.activiti.tutorial.rest;

import java.util.Map;

import javax.annotation.PostConstruct;

import nz.co.activiti.tutorial.model.PagingAndSortingParameters;

import org.apache.commons.lang3.StringUtils;
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

	/**
	 * paging And Sorting query parameters in URL build
	 * 
	 * @param parameters
	 * @return
	 */
	protected String pagingAndSortQueryParametersUrlBuild(
			Map<PagingAndSortingParameters, String> parameters) {
		StringBuilder sb = new StringBuilder("");
		for (Map.Entry<PagingAndSortingParameters, String> entry : parameters
				.entrySet()) {
			if (!StringUtils.isEmpty(entry.getValue())) {
				sb.append("&" + String.valueOf(entry.getKey()) + "="
						+ String.valueOf(entry.getValue()));
			}
		}
		return sb.toString();
	}

	protected String variablesJsonBuild(Map<String, Object> variables) {
		StringBuilder stringBuilder = new StringBuilder("\"variables\": [");
		int variablesSize = variables.size();
		int count = 0;
		for (Map.Entry<String, Object> entry : variables.entrySet()) {
			stringBuilder.append("{\"name\":\"" + entry.getKey()
					+ "\",\"value\":\"" + String.valueOf(entry.getValue())
					+ "\"}");
			if (count != variablesSize) {
				stringBuilder.append(",");
			}
		}
		stringBuilder.append("]");
		return stringBuilder.toString();
	}
}
