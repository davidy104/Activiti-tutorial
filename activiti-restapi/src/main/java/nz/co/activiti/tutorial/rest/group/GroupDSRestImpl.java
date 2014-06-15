package nz.co.activiti.tutorial.rest.group;

import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.GenericActivitiRestException;
import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.group.GroupDS;
import nz.co.activiti.tutorial.model.PagingAndSortingParameters;
import nz.co.activiti.tutorial.model.group.Group;
import nz.co.activiti.tutorial.model.group.GroupQueryParameters;
import nz.co.activiti.tutorial.model.group.Groups;
import nz.co.activiti.tutorial.model.group.MemberShip;
import nz.co.activiti.tutorial.rest.ActionType;
import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

@Service("groupDSRest")
public class GroupDSRestImpl extends ActivitiRestClientAccessor implements
		GroupDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(GroupDSRestImpl.class);

	@Resource
	private GroupJSONConverter groupJSONConverter;

	@Override
	public Groups getGroups(
			Map<GroupQueryParameters, String> groupQueryParameters,
			Map<PagingAndSortingParameters, String> pagingAndSortingParameters)
			throws Exception {
		Groups groups = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/identity/groups");

		if (groupQueryParameters != null) {
			for (Map.Entry<GroupQueryParameters, String> entry : groupQueryParameters
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

		LOGGER.info("URI:{}", webResource.getURI().toString());

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("getGroups failed:{} " + respStr);
		} else {
			groups = groupJSONConverter.toGroups(respStr);
		}

		return groups;
	}

	@Override
	public Group getGroupById(String groupId) throws Exception {
		LOGGER.info("getGroupById start:{} ", groupId);
		Group group = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/identity/groups/" + groupId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			group = groupJSONConverter.toGroup(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("group not found by id[" + groupId
					+ "]");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("getGroupById end:{} ", group);
		return group;
	}

	@Override
	public Group updateGroup(String groupId, Group group) throws Exception {
		LOGGER.info("updateGroup start:{} ", groupId);
		Group updatedGroup = null;
		String jsonEntity = groupJSONConverter.toGroupJson(group,
				ActionType.update);
		LOGGER.info("jsonEntity:{} ", jsonEntity);
		WebResource webResource = client.resource(baseUrl).path(
				"/identity/groups/" + groupId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.put(ClientResponse.class, jsonEntity);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.OK) {
			updatedGroup = groupJSONConverter.toGroup(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("group not found by id[" + groupId
					+ "]");
		} else if (statusCode == ClientResponse.Status.CONFLICT) {
			throw new GenericActivitiRestException(
					"the requested group was updated simultaneously");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("updateGroup end:{} ", updatedGroup);
		return updatedGroup;
	}

	@Override
	public Group createGroup(Group group) throws Exception {
		Group addGroup = null;
		LOGGER.info("createGroup start:{} ", group);
		String jsonEntity = groupJSONConverter.toGroupJson(group,
				ActionType.create);

		LOGGER.info("jsonEntity:{} ", jsonEntity);
		WebResource webResource = client.resource(baseUrl).path(
				"/identity/groups");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonEntity);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.CREATED) {
			addGroup = groupJSONConverter.toGroup(respStr);
		} else if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(
					"the id of the group was missing");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("createGroup end:{} ", addGroup);
		return addGroup;
	}

	@Override
	public void deleteGroup(String groupId) throws Exception {
		WebResource webResource = client.resource(baseUrl).path(
				"/identity/groups/" + groupId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("Group not found by Id[" + groupId
					+ "]");
		} else if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknown exception:{}" + respStr);
		}

	}

	@Override
	public MemberShip createMemberToGroup(String groupId, String userId)
			throws Exception {
		LOGGER.info("createMemberToGroup start:{} ", groupId);
		LOGGER.info("userId:{} ", userId);
		MemberShip memberShip = null;
		WebResource webResource = client.resource(baseUrl).path(
				"/identity/groups" + groupId + "/members");
		String requestJson = "{\"userId\":\"" + userId + "\"}";
		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, requestJson);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.CREATED) {
			memberShip = groupJSONConverter.toMemberShip(respStr);
		} else if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("Group not found by Id[" + groupId
					+ "], or User not found by Id[" + userId + "]");
		} else if (statusCode == ClientResponse.Status.CONFLICT) {
			throw new GenericActivitiRestException(
					"the requested user is already a member of the group.");
		} else {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("createMemberToGroup end:{} ", memberShip);
		return memberShip;
	}

	@Override
	public void deleteMemberFromGroup(String groupId, String userId)
			throws Exception {
		WebResource webResource = client.resource(baseUrl).path(
				"/identity/groups" + groupId + "/members/" + userId);

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON).delete(ClientResponse.class);
		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.NOT_FOUND) {
			throw new NotFoundException("Group not found by Id[" + groupId
					+ "], or User not found by Id[" + userId + "]");
		} else if (statusCode != ClientResponse.Status.NO_CONTENT) {
			throw new Exception("Unknow exception:{} " + respStr);
		}

	}

}
