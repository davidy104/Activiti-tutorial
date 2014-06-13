package nz.co.activiti.tutorial.rest.user;

import java.io.InputStream;
import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.GenericActivitiRestException;
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
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.multipart.FormDataMultiPart;
import com.sun.jersey.multipart.file.StreamDataBodyPart;

@Service("userDSRest")
public class UserDSRestImpl extends ActivitiRestClientAccessor implements
		UserDS {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDSRestImpl.class);

	@Resource
	private UserJSONConverter userJSONConverter;

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
			Map<PagingAndSortingParameters, String> pagingAndSortingParameters)
			throws Exception {
		LOGGER.info("getUsers start:{} ");
		Users users = null;

		WebResource webResource = client.resource(baseUrl).path(
				"/identity/users");

		if (userQueryParameters != null) {
			for (Map.Entry<UserQueryParameters, String> entry : userQueryParameters
					.entrySet()) {
				if (!StringUtils.isEmpty(entry.getValue())) {
					webResource = webResource.queryParam(entry.getKey().name(),
							entry.getValue());
				}
			}
		}

		if (pagingAndSortingParameters != null) {
			this.pagingAndSortQueryParametersUrlBuild(webResource,
					pagingAndSortingParameters);
		}

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
		LOGGER.info("updateUser start:{} ", userId);
		User updatedUser = null;
		String userEntityJson = this.userJsonEntityBuild(updateUser, false);
		LOGGER.info("userEntityJson:{} ", userEntityJson);

		WebResource webResource = client.resource(baseUrl).path(
				"/identity/users/" + userId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, userEntityJson);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			updatedUser = userJSONConverter.toUser(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("User not found by Id[" + userId + "]");
		} else if (statusCode == ClientResponse.Status.CONFLICT) {
			throw new GenericActivitiRestException(
					"User was updated simultaneously");
		} else {
			throw new Exception("getUserById [" + userId
					+ "] failed caused by unknown exception");
		}

		LOGGER.info("updateUser end:{} ", updatedUser);
		return updatedUser;
	}

	@Override
	public User createUser(User addUser) throws Exception {
		LOGGER.info("createUser start:{} ", addUser);
		User addedUser = null;
		String userEntityJson = this.userJsonEntityBuild(addUser, true);
		LOGGER.info("userEntityJson:{} ", userEntityJson);

		WebResource webResource = client.resource(baseUrl).path(
				"/identity/users");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, userEntityJson);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.CREATED) {
			addedUser = userJSONConverter.toUser(respStr);
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(
					"the id of the user was missing");
		} else {
			throw new Exception("Unknown exception:{}" + respStr);
		}

		LOGGER.info("createUser end:{} ", addedUser);
		return addedUser;
	}

	@Override
	public void deleteUser(String userId) throws Exception {
		LOGGER.info("deleteUser start:{} ", userId);

		WebResource webResource = client.resource(baseUrl).path(
				"/identity/users/" + userId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("User not found by Id[" + userId + "]");
		} else if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknown exception:{}" + respStr);
		}

		LOGGER.info("deleteUser end:{} ");
	}

	@Override
	public void updateUsersPicture(String userId, InputStream pictureStream)
			throws Exception {
		LOGGER.info("updateUsersPicture start:{} ", userId);
		FormDataMultiPart multiPart = new FormDataMultiPart();
		// multiPart.bodyPart(new FileDataBodyPart("picture", picture));

		multiPart.bodyPart(new StreamDataBodyPart("davidy.png", pictureStream));

		WebResource webResource = client.resource(baseUrl).path(
				"/identity/users/" + userId + "/picture");

		ClientResponse response = webResource
				.type(MediaType.MULTIPART_FORM_DATA)
				.accept(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, multiPart);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("User not found by Id[" + userId + "]");
		} else if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("Unknown exception:{}" + respStr);
		}
		LOGGER.info("updateUsersPicture end:{} ");
	}

	private String userJsonEntityBuild(User user, boolean creation) {
		StringBuilder sb = new StringBuilder();
		if (user != null) {
			if (!StringUtils.isEmpty(user.getFirstName())) {
				sb.append("{\"firstName\":").append(
						"\"" + user.getFirstName() + "\"");
			}

			if (!StringUtils.isEmpty(user.getEmail())) {
				if (StringUtils.isEmpty(sb.toString())) {
					sb.append("{\"email\":").append(
							"\"" + user.getEmail() + "\"");
				} else {
					sb.append(",\"email\":").append(
							"\"" + user.getEmail() + "\"");
				}
			}

			if (!StringUtils.isEmpty(user.getLastName())) {
				if (StringUtils.isEmpty(sb.toString())) {
					sb.append("{\"lastName\":").append(
							"\"" + user.getLastName() + "\"");
				} else {
					sb.append(",\"lastName\":").append(
							"\"" + user.getLastName() + "\"");
				}
			}

			if (!StringUtils.isEmpty(user.getPassword())) {
				if (StringUtils.isEmpty(sb.toString())) {
					sb.append("{\"password\":").append(
							"\"" + user.getPassword() + "\"");
				} else {
					sb.append(",\"password\":").append(
							"\"" + user.getPassword() + "\"");
				}
			}

			if (creation) {
				if (!StringUtils.isEmpty(user.getId())) {
					if (StringUtils.isEmpty(sb.toString())) {
						sb.append("{\"id\":")
								.append("\"" + user.getId() + "\"");
					} else {
						sb.append(",\"id\":")
								.append("\"" + user.getId() + "\"");
					}
				}

			}
			if (!StringUtils.isEmpty(sb.toString())) {
				sb.append("}");
			}
		}
		return sb.toString();
	}

}
