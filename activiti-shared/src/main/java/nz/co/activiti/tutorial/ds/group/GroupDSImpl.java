package nz.co.activiti.tutorial.ds.group;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.DuplicatedException;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
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
	public Group getGroupById(String groupId) {
		return identityService.createGroupQuery().groupId(groupId)
				.singleResult();
	}

	@Override
	public void updateGroup(String groupId, String name, String type)
			throws DuplicatedException {
		LOGGER.info("updateGroup start:{}", groupId);
		LOGGER.info("name:{}", name);
		LOGGER.info("type:{}", type);
		Group updateGroup = new GroupEntity();
		updateGroup.setName(name);
		updateGroup.setType(type);
		updateGroup.setId(groupId);
		try {
			identityService.saveGroup(updateGroup);
		} catch (RuntimeException e) {
			throw new DuplicatedException(e);
		}
		LOGGER.info("updateGroup end:{}");
	}

	@Override
	public void createGroup(String groupId, String name, String type)
			throws DuplicatedException {
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
	public void deleteGroup(String groupId) {
		identityService.deleteGroup(groupId);
	}

	@Override
	public void createMemberToGroup(String groupId, String userId) {
		identityService.createMembership(userId, groupId);
	}

	@Override
	public void deleteMemberFromGroup(String groupId, String userId) {
		identityService.deleteMembership(userId, groupId);
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
