package nz.co.activiti.tutorial.rest;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.user.UserDS;
import nz.co.activiti.tutorial.model.GenericCollectionModel;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.user.User;
import nz.co.activiti.tutorial.model.user.UserQueryParameter;
import nz.co.activiti.tutorial.rest.config.ApplicationContextConfiguration;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { ApplicationContextConfiguration.class })
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class UserIntegrationTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(UserIntegrationTest.class);

	@Resource
	private UserDS userDSRest;

	private static final String TEST_USER_PICTURE = "davidy.png";
	private static final String TEST_USER_ID = "fozzie";
	private static final String TEST_USER_FIRSTNAME = "Fozzie";
	private static final String TEST_USER_LASTNAME = "Bear";
	private static final String TEST_USER_EMAIL = "fozzie@activiti.org";

	// @Before
	// public void initialize() throws Exception {
	// User addUser = mockUser();
	// addUser.setId(TEST_USER_ID);
	// addUser = userDSRest.createUser(addUser);
	// LOGGER.info("addUser:{} ", addUser);
	// }
	//
	// @After
	// public void clean() throws Exception {
	// if (ifCleanUser) {
	// userDSRest.deleteUser(TEST_USER_ID);
	// }
	// }

	@Test
	public void testCreateAndDeleteUser() throws Exception {
		User addUser = mockUser();
		addUser.setId("davidy");
		addUser = userDSRest.createUser(addUser);
		assertNotNull(addUser);
		LOGGER.info("addUser:{} ", addUser);
		userDSRest.deleteUser(addUser.getId());
	}

	@Test
	public void testGetUserById() throws Exception {
		User foundUser = userDSRest.getUserById(TEST_USER_ID);
		assertNotNull(foundUser);
		LOGGER.info("foundUser:{} ", foundUser);
	}

	@Test
	public void testUpdateUser() throws Exception {
		User addUser = mockUser();
		addUser.setId("davidy");
		addUser = userDSRest.createUser(addUser);
		assertNotNull(addUser);
		LOGGER.info("addUser:{} ", addUser);

		User updateUser = new User();
		updateUser.setEmail("david.yuan124@gmail.com");
		updateUser = userDSRest.updateUser("davidy", updateUser);
		assertNotNull(updateUser);
		LOGGER.info("updated users:{} ", updateUser);

		userDSRest.deleteUser("davidy");
	}

	@Test
	public void testPagingUsers() throws Exception {
		Map<UserQueryParameter, String> userQueryParameters = new HashMap<UserQueryParameter, String>();
		Map<PagingAndSortingParameter, String> pagingAndSortingParameters = new HashMap<PagingAndSortingParameter, String>();
		pagingAndSortingParameters.put(PagingAndSortingParameter.size, "4");
		GenericCollectionModel<User> users = userDSRest.getUsers(
				userQueryParameters, pagingAndSortingParameters);
		assertNotNull(users);
		LOGGER.info("users:{} ", users);
	}

	@Test
	public void testGetUsersByConditions() throws Exception {
		Map<UserQueryParameter, String> userQueryParameters = new HashMap<UserQueryParameter, String>();
		userQueryParameters.put(UserQueryParameter.firstName,
				TEST_USER_FIRSTNAME);
		userQueryParameters
				.put(UserQueryParameter.lastName, TEST_USER_LASTNAME);
		userQueryParameters.put(UserQueryParameter.email, TEST_USER_EMAIL);
		GenericCollectionModel<User> users = userDSRest.getUsers(
				userQueryParameters, null);
		assertNotNull(users);
		LOGGER.info("users:{} ", users);
	}

	@Test
	@Ignore("need another workaround")
	public void testUpdateUsersPicture() throws Exception {
		User addUser = mockUser();
		addUser.setId("davidy");
		addUser = userDSRest.createUser(addUser);
		assertNotNull(addUser);
		LOGGER.info("addUser:{} ", addUser);

		InputStream processStream = ProcessDeploymentMetaDataTest.class
				.getClassLoader().getResourceAsStream(TEST_USER_PICTURE);
		// File picture = File.createTempFile("davidy", ".png");
		// GeneralUtils.inputStreamToFile(processStream, picture);

		userDSRest.updateUsersPicture("davidy", processStream);
	}

	private User mockUser() {
		User user = new User();
		user.setEmail("david.yuan@propellerhead.co.nz");
		user.setFirstName("David");
		user.setLastName("Yuan");
		user.setPassword("123456");
		return user;
	}

}
