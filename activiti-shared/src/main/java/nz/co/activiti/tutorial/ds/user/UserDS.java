package nz.co.activiti.tutorial.ds.user;

import java.io.InputStream;
import java.util.Map;

import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.user.User;
import nz.co.activiti.tutorial.model.user.UserQueryParameter;
import nz.co.activiti.tutorial.model.user.Users;

public interface UserDS {

	/**
	 * Get a single user
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	User getUserById(String userId) throws Exception;

	/**
	 * Create a user
	 * 
	 * @param userId
	 * @param updateUser
	 * @return
	 * @throws Exception
	 */
	User updateUser(String userId, User updateUser) throws Exception;

	/**
	 * 
	 * @param addUser
	 * @return
	 * @throws Exception
	 */
	User createUser(User addUser) throws Exception;

	/**
	 * 
	 * @param userId
	 * @throws Exception
	 */
	void deleteUser(String userId) throws Exception;

	/**
	 * Get a list of users
	 * 
	 * @param userQueryParameters
	 * @param paginAndSort
	 * @return
	 * @throws Exception
	 */
	Users getUsers(Map<UserQueryParameter, String> userQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception;

	/**
	 * 
	 * @throws Exception
	 */
	void updateUsersPicture(String userId, InputStream pictureStream)
			throws Exception;

}
