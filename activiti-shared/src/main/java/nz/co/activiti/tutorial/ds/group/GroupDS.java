package nz.co.activiti.tutorial.ds.group;

import nz.co.activiti.tutorial.DuplicatedException;

import org.activiti.engine.identity.Group;

public interface GroupDS {

	Group getGroupById(String groupId);

	void updateGroup(String groupId, String name, String type)
			throws DuplicatedException;

	void createGroup(String groupId, String name, String type)
			throws DuplicatedException;

	void deleteGroup(String groupId);

	void createMemberToGroup(String groupId, String userId);

	void deleteMemberFromGroup(String groupId, String userId);

	boolean checkIfGroupExisted(String groupId) throws DuplicatedException;

}
