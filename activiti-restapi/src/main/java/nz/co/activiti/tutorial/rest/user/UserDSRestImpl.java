package nz.co.activiti.tutorial.rest.user;

import java.io.File;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.user.UserDS;
import nz.co.activiti.tutorial.model.PagingAndSortingParameters;
import nz.co.activiti.tutorial.model.user.User;
import nz.co.activiti.tutorial.model.user.UserQueryParameters;
import nz.co.activiti.tutorial.model.user.Users;
import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

@Service("userDSRest")
public class UserDSRestImpl extends ActivitiRestClientAccessor implements
		UserDS {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDSRestImpl.class);

	@Resource
	private UserJSONConverter userJSONConverter;

	@Value("${activiti.api.baseurl}")
	private String baseUrl;

	@Override
	public User getUserById(String userId) throws Exception {
		LOGGER.info("getUserById start:{} ", userId);
		User user = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/identity/users/" + userId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			user = userJSONConverter.toUser(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("User not found by Id[" + userId + "]");
		} else {
			throw new Exception("getUserById [" + userId
					+ "] failed caused by unknown exception");
		}
		LOGGER.info("getUserById end:{} ", user);
		return user;
	}

	@Override
	public Users getUsers(Map<UserQueryParameters, String> userQueryParameters,
			Map<PagingAndSortingParameters, String> paginAndSortParameters)
			throws Exception {
		LOGGER.info("getUsers start:{} ");
		Users users = null;
		StringBuilder pathSb = new StringBuilder("/identity/users");
		String userQueryParametersUrl = this
				.userQueryParametersUrlBuild(userQueryParameters);
		String paingAndSortingParametersUrl = this
				.pagingAndSortQueryParametersUrlBuild(paginAndSortParameters);

		if (!StringUtils.isEmpty(userQueryParametersUrl)) {
			pathSb.append("?").append(userQueryParametersUrl.substring(1));
		}

		if (!StringUtils.isEmpty(paingAndSortingParametersUrl)) {
			if (!StringUtils.isEmpty(userQueryParametersUrl)) {
				pathSb.append(paingAndSortingParametersUrl);
			} else {
				pathSb.append("?").append(
						paingAndSortingParametersUrl.substring(1));
			}
		}
		String path = pathSb.toString();
		LOGGER.info("path:{} ", path);

		WebResource webResource = client.resource(baseUrl).path(path);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			users = userJSONConverter.toUsers(respStr);
		} else {
			throw new Exception("getUsers failed caused by unknown exception");
		}
		LOGGER.info("getUsers end:{} ");
		return users;
	}

	@Override
	public User updateUser(String userId, User updateUser) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public User createUser(User addUser) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(String userId) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateUsersPicture(String userId, File picture)
			throws Exception {
		// TODO Auto-generated method stub

	}

	private String userQueryParametersUrlBuild(
			Map<UserQueryParameters, String> parameters) {
		StringBuilder sb = new StringBuilder("");
		for (Map.Entry<UserQueryParameters, String> entry : parameters
				.entrySet()) {
			if (!StringUtils.isEmpty(entry.getValue())) {
				sb.append("&" + String.valueOf(entry.getKey()) + "="
						+ String.valueOf(entry.getValue()));
			}
		}
		return sb.toString();
	}
}
