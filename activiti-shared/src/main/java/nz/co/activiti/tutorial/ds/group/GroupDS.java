package nz.co.activiti.tutorial.ds.group;

import java.util.Map;

import nz.co.activiti.tutorial.model.PagingAndSortingParameters;
import nz.co.activiti.tutorial.model.group.Group;
import nz.co.activiti.tutorial.model.group.GroupQueryParameters;
import nz.co.activiti.tutorial.model.group.Groups;
import nz.co.activiti.tutorial.model.group.MemberShip;

public interface GroupDS {
	Groups getGroups(Map<GroupQueryParameters, String> groupQueryParameters,
			Map<PagingAndSortingParameters, String> pagingAndSortingParameters)
			throws Exception;

	Group getGroupById(String groupId) throws Exception;

	Group updateGroup(String groupId, Group group) throws Exception;

	/**
	 * to create new group, url is generated by engin
	 * 
	 * @param group
	 * @return
	 * @throws Exception
	 */
	Group createGroup(Group group) throws Exception;

	void deleteGroup(String groupId) throws Exception;

	MemberShip createMemberToGroup(String groupId, String userId)
			throws Exception;

	void deleteMemberFromGroup(String groupId, String userId) throws Exception;

}
