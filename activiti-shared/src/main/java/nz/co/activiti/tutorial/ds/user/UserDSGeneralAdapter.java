package nz.co.activiti.tutorial.ds.user;

import java.io.InputStream;
import java.util.Map;

import nz.co.activiti.tutorial.ds.user.UserDS;
import nz.co.activiti.tutorial.model.GenericCollectionModel;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.user.User;
import nz.co.activiti.tutorial.model.user.UserQueryParameter;

public abstract class UserDSGeneralAdapter implements UserDS {

	@Override
	public GenericCollectionModel<User> getUsers(
			Map<UserQueryParameter, String> userQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		return null;
	}

	@Override
	public void updateUsersPicture(String userId, InputStream pictureStream)
			throws Exception {

	}

}
