package nz.co.activiti.tutorial.ds.group;

import java.util.Map;

import nz.co.activiti.tutorial.ds.group.GroupDS;
import nz.co.activiti.tutorial.model.GenericCollectionModel;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.group.Group;
import nz.co.activiti.tutorial.model.group.GroupQueryParameter;

public abstract class GroupDSGeneralAdapter implements GroupDS {

	@Override
	public GenericCollectionModel<Group> getGroups(
			Map<GroupQueryParameter, String> groupQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		return null;
	}

}
