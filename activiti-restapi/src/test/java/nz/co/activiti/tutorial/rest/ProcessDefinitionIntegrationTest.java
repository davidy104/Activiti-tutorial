package nz.co.activiti.tutorial.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.Family;
import nz.co.activiti.tutorial.rest.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.rest.ds.processdefinition.ProcessDefinitionRestDS;
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.Identity;
import nz.co.activiti.tutorial.rest.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.rest.model.processdefinition.ProcessDefinition;
import nz.co.activiti.tutorial.rest.model.processdefinition.ProcessDefinitionQueryParameter;

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
public class ProcessDefinitionIntegrationTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessDefinitionIntegrationTest.class);

	private static final String TEST_PROCESS_DEFINITION_ID = "createTimersProcess:1:37";

	private static final String TEST_UPDATE_CATEGORY = "testCategory";

	private static final String TEST_DEPLOYMENT_ID = "23";
	private static final String TEST_PROCESS_DEFINITION_KEY = "createTimersProcess";

	@Resource
	private ProcessDefinitionRestDS processDefinitionRestDS;

	@Test
	public void testGetAllProcessDefinitions() throws Exception {
		Map<ProcessDefinitionQueryParameter, String> processDefinitionQueryParameters = new HashMap<ProcessDefinitionQueryParameter, String>();
		Map<PagingAndSortingParameter, String> pagingAndSortingParameters = new HashMap<PagingAndSortingParameter, String>();
		GenericCollectionModel<ProcessDefinition> processDefinitionsResponse = processDefinitionRestDS
				.getProcessDefinitions(processDefinitionQueryParameters,
						pagingAndSortingParameters);
		assertNotNull(processDefinitionsResponse);
		LOGGER.info("processDefinitionsResponse:{} ",
				processDefinitionsResponse);
	}

	@Test
	public void testGetProcessDefinitionByConditions() throws Exception {
		Map<ProcessDefinitionQueryParameter, String> processDefinitionQueryParameters = new HashMap<ProcessDefinitionQueryParameter, String>();
		Map<PagingAndSortingParameter, String> pagingAndSortingParameters = new HashMap<PagingAndSortingParameter, String>();
		processDefinitionQueryParameters.put(
				ProcessDefinitionQueryParameter.deploymentId,
				TEST_DEPLOYMENT_ID);
		processDefinitionQueryParameters.put(
				ProcessDefinitionQueryParameter.key,
				TEST_PROCESS_DEFINITION_KEY);

		GenericCollectionModel<ProcessDefinition> processDefinitionsResponse = processDefinitionRestDS
				.getProcessDefinitions(processDefinitionQueryParameters,
						pagingAndSortingParameters);
		assertNotNull(processDefinitionsResponse);
		assertEquals(1, processDefinitionsResponse.getModelList().size());
		LOGGER.info("processDefinitionsResponse:{} ",
				processDefinitionsResponse);

		// processDefinitionQueryParameters.clear();
		// processDefinitionQueryParameters.put(
		// ProcessDefinitionQueryParameters.key,
		// TEST_PROCESS_DEFINITION_KEY);
		// processDefinitionsResponse = processDefinitionRestDS
		// .getProcessDefinitions(processDefinitionQueryParameters,
		// pagingAndSortingParameters);
		// assertNotNull(processDefinitionsResponse);
		// LOGGER.info("processDefinitionsResponse:{} ",
		// processDefinitionsResponse);
	}

	@Test
	public void testGetProcessDefinition() throws Exception {
		ProcessDefinition processDefinitionResponse = processDefinitionRestDS
				.getProcessDefinitionByProcessDefinitionId(TEST_PROCESS_DEFINITION_ID);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);
	}

	// Examples (its original category)
	@Test
	public void testUpdateCategory() throws Exception {
		ProcessDefinition processDefinitionResponse = processDefinitionRestDS
				.updateCategory(TEST_PROCESS_DEFINITION_ID,
						TEST_UPDATE_CATEGORY);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);

		String updatedCategory = processDefinitionResponse.getCategory();
		assertEquals(TEST_UPDATE_CATEGORY, updatedCategory);

		Thread.sleep(500);

		processDefinitionResponse = processDefinitionRestDS.updateCategory(
				TEST_PROCESS_DEFINITION_ID, "Examples");

		updatedCategory = processDefinitionResponse.getCategory();
		assertEquals("Examples", updatedCategory);
	}

	@Test
	@Ignore("not run all the time")
	public void testSuspendProcess() throws Exception {
		ProcessDefinition processDefinitionResponse = processDefinitionRestDS
				.suspendProcessDefinition(TEST_PROCESS_DEFINITION_ID, false,
						null);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);
		assertTrue(processDefinitionResponse.getSuspended());
	}

	@Test
	@Ignore("not run all the time")
	public void testActiveProcess() throws Exception {
		ProcessDefinition processDefinitionResponse = processDefinitionRestDS
				.activeProcessDefinition(TEST_PROCESS_DEFINITION_ID, true, null);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);
		assertFalse(processDefinitionResponse.getSuspended());
	}

	@Test
	public void testAddAndDeleteCandidate() throws Exception {
		String user = "kermit";
		String group = "engineering";
		// add user as candidate
		Identity candidate = processDefinitionRestDS.addIdentity(
				TEST_PROCESS_DEFINITION_ID, Family.users, user);
		assertNotNull(candidate);
		LOGGER.info("candidate:{} ", candidate);

		// add group as candidate
		candidate = processDefinitionRestDS.addIdentity(
				TEST_PROCESS_DEFINITION_ID, Family.groups, group);
		assertNotNull(candidate);
		LOGGER.info("candidate:{} ", candidate);

		processDefinitionRestDS.deleteIdentity(TEST_PROCESS_DEFINITION_ID,
				Family.users, user);
		processDefinitionRestDS.deleteIdentity(TEST_PROCESS_DEFINITION_ID,
				Family.groups, group);
	}

	@Test
	// @Ignore("need specific processDefinitionId, or run after @testAddCandidate")
	public void testGetCandidates() throws Exception {
		Set<Identity> candidates = processDefinitionRestDS
				.getAllIdentities(TEST_PROCESS_DEFINITION_ID);
		assertNotNull(candidates);
		for (Identity candidate : candidates) {
			LOGGER.info("candidate:{} ", candidate);
		}
	}
}
