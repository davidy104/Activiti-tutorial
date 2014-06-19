package nz.co.activiti.tutorial.ds.group;

import nz.co.activiti.tutorial.DuplicatedException;

import org.activiti.engine.identity.Group;

public interface GroupDS {

	Group getGroupById(String groupId) throws Exception;

	void updateGroup(String groupId, String name, String type) throws Exception;

	void createGroup(String groupId, String name, String type) throws Exception;

	void deleteGroup(String groupId) throws Exception;

	void createMemberToGroup(String groupId, String userId) throws Exception;

	void deleteMemberFromGroup(String groupId, String userId) throws Exception;

	boolean checkIfGroupExisted(String groupId) throws DuplicatedException;

}
