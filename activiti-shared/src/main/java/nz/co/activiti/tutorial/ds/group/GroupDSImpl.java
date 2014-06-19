package nz.co.activiti.tutorial.ds.group;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.DuplicatedException;
import nz.co.activiti.tutorial.NotFoundException;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GroupDSImpl implements GroupDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(GroupDSImpl.class);

	@Resource
	private IdentityService identityService;

	@Override
	public Group getGroupById(String groupId) throws Exception {
		return identityService.createGroupQuery().groupId(groupId)
				.singleResult();
	}

	@Override
	public void updateGroup(String groupId, String name, String type)
			throws Exception {
		LOGGER.info("updateGroup start:{}", groupId);
		LOGGER.info("name:{}", name);
		LOGGER.info("type:{}", type);
		Group existedGroup = this.getGroupById(groupId);
		if (existedGroup == null) {
			throw new NotFoundException("group not found by id[" + groupId
					+ "]");
		}

		if (!StringUtils.isEmpty(name)) {
			existedGroup.setName(name);
		}
		if (!StringUtils.isEmpty(type)) {
			existedGroup.setType(type);
		}
		try {
			identityService.saveGroup(existedGroup);
		} catch (RuntimeException e) {
			throw new DuplicatedException(e);
		}
		LOGGER.info("updateGroup end:{}");
	}

	@Override
	public void createGroup(String groupId, String name, String type)
			throws Exception {
		Group newGroup = identityService.newGroup(groupId);
		newGroup.setName(name);
		newGroup.setType(type);
		try {
			identityService.saveGroup(newGroup);
		} catch (RuntimeException e) {
			throw new DuplicatedException(e);
		}
	}

	@Override
	public void deleteGroup(String groupId) throws Exception {
		identityService.deleteGroup(groupId);
	}

	@Override
	public void createMemberToGroup(String groupId, String userId)
			throws Exception {
		try {
			identityService.createMembership(userId, groupId);
		} catch (RuntimeException e) {
			throw new Exception(e);
		}
	}

	@Override
	public void deleteMemberFromGroup(String groupId, String userId)
			throws Exception {
		try {
			identityService.deleteMembership(userId, groupId);
		} catch (RuntimeException e) {
			throw new Exception(e);
		}
	}

	@Override
	public boolean checkIfGroupExisted(String groupId)
			throws DuplicatedException {
		long count = identityService.createGroupQuery().groupId(groupId)
				.count();
		if (count == 1) {
			return true;
		} else if (count > 1) {
			throw new DuplicatedException("Duplicated groupId[" + groupId + "]");
		}
		return false;
	}

}
