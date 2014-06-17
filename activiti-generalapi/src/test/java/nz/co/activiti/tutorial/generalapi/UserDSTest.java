package nz.co.activiti.tutorial.generalapi;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.generalapi.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.generalapi.user.UserDSGeneralAdapter;
import nz.co.activiti.tutorial.model.user.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UserDSTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserDSTest.class);

	@Resource
	private UserDSGeneralAdapter userDSGeneralImpl;

	@Test
	public void testCreateUser() throws Exception {
		User addUser = new User();
		addUser.setEmail("david.yuan@propellerhead.co.nz");
		addUser.setLastName("Yuan");
		addUser.setFirstName("David");
		addUser.setPassword("2323254");
		addUser.setId("davidy");
		userDSGeneralImpl.createUser(addUser);

		User foundUser = userDSGeneralImpl.getUserById("davidy");
		LOGGER.info("foundUser:{} ", foundUser);
		assertNotNull(foundUser);
	}

}
