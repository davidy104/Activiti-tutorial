package nz.co.activiti.tutorial.generalapi.group;

import java.util.Map;

import nz.co.activiti.tutorial.ds.group.GroupDS;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.group.GroupQueryParameter;
import nz.co.activiti.tutorial.model.group.Groups;

public abstract class GroupDSGeneralAdapter implements GroupDS {

	@Override
	public Groups getGroups(
			Map<GroupQueryParameter, String> groupQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		return null;
	}

	
}
