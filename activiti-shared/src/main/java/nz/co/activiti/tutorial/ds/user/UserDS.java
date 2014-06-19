package nz.co.activiti.tutorial.ds.user;

import org.activiti.engine.identity.User;

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

	boolean checkIfUserExisted(String userId);

}
