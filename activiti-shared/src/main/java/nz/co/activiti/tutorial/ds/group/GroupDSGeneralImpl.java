package nz.co.activiti.tutorial.ds.group;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.model.group.Group;
import nz.co.activiti.tutorial.model.group.MemberShip;

import org.activiti.engine.IdentityService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service("groupDSGeneralImpl")
public class GroupDSGeneralImpl extends GroupDSGeneralAdapter {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(GroupDSGeneralImpl.class);

	@Resource
	private IdentityService identityService;

	@Override
	public Group getGroupById(String groupId) throws Exception {
		LOGGER.info("getGroupById start:{} ", groupId);
		Group found = null;
		org.activiti.engine.identity.Group activitiGroup = this
				.getActivitiGroup(groupId);
		BeanUtils.copyProperties(activitiGroup, found);
		LOGGER.info("getGroupById end:{} ", found);
		return found;
	}

	@Override
	public Group updateGroup(String groupId, Group group) throws Exception {
		LOGGER.info("updateGroup start:{}", groupId);
		LOGGER.info("updateGroup:{}", group);
		this.getActivitiGroup(groupId);
		identityService.deleteGroup(groupId);
		org.activiti.engine.identity.Group newActivitiGroup = identityService
				.newGroup(groupId);
		newActivitiGroup.setName(group.getName());
		newActivitiGroup.setType(group.getType());
		identityService.saveGroup(newActivitiGroup);
		group.setId(groupId);
		LOGGER.info("updateGroup end:{}", group);
		return group;
	}

	@Override
	public Group createGroup(Group group) throws Exception {
		LOGGER.info("createGroup start:{} ", group);
		String groupId = group.getId();
		if (StringUtils.isEmpty(groupId)) {
			throw new IllegalArgumentException("GroupId must be provided");
		}
		org.activiti.engine.identity.Group newActivitiGroup = identityService
				.newGroup(groupId);
		BeanUtils
				.copyProperties(group, newActivitiGroup, new String[] { "id" });
		identityService.saveGroup(newActivitiGroup);
		LOGGER.info("createGroup end:{} ", group);
		return group;
	}

	@Override
	public void deleteGroup(String groupId) throws Exception {
		LOGGER.info("deleteGroup start:{}", groupId);
		if (StringUtils.isEmpty(groupId)) {
			throw new IllegalArgumentException("GroupId must be provided");
		}
		identityService.deleteGroup(groupId);
		LOGGER.info("deleteGroup end:{}");
	}

	@Override
	public MemberShip createMemberToGroup(String groupId, String userId)
			throws Exception {

		if (!this.verifyMemberShip(groupId, userId)) {
			throw new NotFoundException("User or Group not been created yet");
		}
		identityService.createMembership(userId, groupId);
		MemberShip memberShip = new MemberShip();
		memberShip.setGroupId(groupId);
		memberShip.setUrl(userId);

		return memberShip;
	}

	@Override
	public void deleteMemberFromGroup(String groupId, String userId)
			throws Exception {
		LOGGER.info("deleteMemberFromGroup start:{} ");
		LOGGER.info("groupId:{} ", groupId);
		LOGGER.info("userId:{} ", userId);
		if (!this.verifyMemberShip(groupId, userId)) {
			throw new NotFoundException("User or Group not been created yet");
		}
		identityService.deleteMembership(userId, groupId);
		LOGGER.info("deleteMemberFromGroup end:{} ");
	}

	private boolean verifyMemberShip(String groupId, String userId) {
		long groupCount = identityService.createGroupQuery().groupId(groupId)
				.count();
		long userCount = identityService.createUserQuery().userId(userId)
				.count();

		if (groupCount == 0 || userCount == 0) {
			return false;
		}

		return true;
	}

	private org.activiti.engine.identity.Group getActivitiGroup(String groupId)
			throws NotFoundException {
		org.activiti.engine.identity.Group activitiGroup = identityService
				.createGroupQuery().groupId(groupId).singleResult();
		if (activitiGroup == null) {
			throw new NotFoundException("Group not found by id[" + groupId
					+ "]");
		}
		return activitiGroup;
	}
}
