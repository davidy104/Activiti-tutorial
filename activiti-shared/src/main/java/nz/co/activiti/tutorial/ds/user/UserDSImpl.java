package nz.co.activiti.tutorial.ds.user;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.model.user.User;

import org.activiti.engine.IdentityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service("userDSGeneralImpl")
public class UserDSImpl extends UserDSGeneralAdapter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDSImpl.class);

	@Resource
	private IdentityService identityService;

	@Override
	public User getUserById(String userId) throws Exception {
		org.activiti.engine.identity.User activitiUser = identityService
				.createUserQuery().userId(userId).singleResult();
		if (activitiUser == null) {
			throw new NotFoundException("user not found by id [" + userId + "]");
		}
		User user = new User();
		BeanUtils.copyProperties(activitiUser, user);
		return user;
	}

	@Override
	public User updateUser(String userId, User updateUser) throws Exception {
		User user = this.getUserById(userId);
		String originalId = user.getId();
		identityService.deleteUser(originalId);

		org.activiti.engine.identity.User updatedUser = identityService
				.newUser(originalId);
		updatedUser.setEmail(updateUser.getEmail());
		updatedUser.setFirstName(updateUser.getFirstName());
		updatedUser.setLastName(updateUser.getLastName());
		identityService.saveUser(updatedUser);
		BeanUtils.copyProperties(updatedUser, user);
		return user;
	}

	@Override
	public User createUser(User addUser) throws Exception {
		LOGGER.info("createUser start:{}", addUser);
		String userId = addUser.getId();
		org.activiti.engine.identity.User newUser = identityService
				.newUser(userId);
		BeanUtils.copyProperties(addUser, newUser, new String[] { "id" });
		identityService.saveUser(newUser);
		return addUser;
	}

	@Override
	public void deleteUser(String userId) throws Exception {
		identityService.deleteUser(userId);
	}

}
