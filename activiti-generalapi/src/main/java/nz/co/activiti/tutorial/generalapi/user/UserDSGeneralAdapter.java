package nz.co.activiti.tutorial.generalapi.user;

import java.io.InputStream;
import java.util.Map;

import nz.co.activiti.tutorial.ds.user.UserDS;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.user.UserQueryParameter;
import nz.co.activiti.tutorial.model.user.Users;

public abstract class UserDSGeneralAdapter implements UserDS {

	@Override
	public Users getUsers(Map<UserQueryParameter, String> userQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		return null;
	}

	@Override
	public void updateUsersPicture(String userId, InputStream pictureStream)
			throws Exception {

	}

}
