package nz.co.activiti.tutorial.ds.user;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.DuplicatedException;

import org.activiti.engine.IdentityService;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserDSImpl implements UserDS {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDSImpl.class);

	@Resource
	private IdentityService identityService;

	@Override
	public User getUserById(String userId) {
		return identityService.createUserQuery().userId(userId).singleResult();
	}

	@Override
	public void updateUser(String userId, String firstName, String lastName,
			String email, String password) throws DuplicatedException {
		LOGGER.info("updateUser start:{}");
		try {
			User updateUser = new UserEntity();
			updateUser.setFirstName(firstName);
			updateUser.setEmail(email);
			updateUser.setLastName(lastName);
			updateUser.setId(userId);
			updateUser.setPassword(password);
			identityService.saveUser(updateUser);
		} catch (RuntimeException e) {
			throw new DuplicatedException(e);
		}
	}

	@Override
	public void createUser(String userId, String firstName, String lastName,
			String email, String password) throws DuplicatedException {
		try {
			User addUser = identityService.newUser(userId);
			addUser.setFirstName(firstName);
			addUser.setEmail(email);
			addUser.setLastName(lastName);
			addUser.setPassword(password);
			identityService.saveUser(addUser);
		} catch (RuntimeException e) {
			throw new DuplicatedException(e);
		}
	}

	@Override
	public void deleteUser(String userId) {
		identityService.deleteUser(userId);
	}

	@Override
	public boolean checkIfUserExisted(String userId) throws DuplicatedException {
		long count = identityService.createUserQuery().userId(userId).count();
		if (count == 1) {
			return true;
		} else if (count > 1) {
			throw new DuplicatedException("Duplicated User with same id["
					+ userId + "]");
		}
		return false;
	}

}
