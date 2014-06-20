package nz.co.activiti.tutorial.ds.user;

import nz.co.activiti.tutorial.DuplicatedException;

import org.activiti.engine.identity.User;

public interface UserDS {

	User getUserById(String userId);

	void updateUser(String userId, String firstName, String lastName,
			String email, String password) throws DuplicatedException;

	void createUser(String userId, String firstName, String lastName,
			String email, String password) throws DuplicatedException;

	void deleteUser(String userId);

	boolean checkIfUserExisted(String userId) throws DuplicatedException;

}
