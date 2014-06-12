package nz.co.activiti.tutorial.ds.user;

import java.io.File;
import java.util.Map;

import nz.co.activiti.tutorial.model.PagingAndSortingParameters;
import nz.co.activiti.tutorial.model.user.User;
import nz.co.activiti.tutorial.model.user.UserQueryParameters;
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
	 * Get a list of users
	 * 
	 * @param userQueryParameters
	 * @param paginAndSort
	 * @return
	 * @throws Exception
	 */
	Users getUsers(Map<UserQueryParameters, String> userQueryParameters,
			Map<PagingAndSortingParameters, String> paginAndSortParameters)
			throws Exception;

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
	 * 
	 * @throws Exception
	 */
	void updateUsersPicture(String userId, File picture) throws Exception;

}
