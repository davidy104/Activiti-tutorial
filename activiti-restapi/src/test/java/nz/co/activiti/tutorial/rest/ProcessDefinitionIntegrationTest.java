package nz.co.activiti.tutorial.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.processdefinition.ProcessDefinitionDS;
import nz.co.activiti.tutorial.model.Family;
import nz.co.activiti.tutorial.model.PagingAndSortingParameters;
import nz.co.activiti.tutorial.model.Party;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinition;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinitionQueryParameters;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinitions;
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
public class ProcessDefinitionIntegrationTest {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessDefinitionIntegrationTest.class);

	private static final String TEST_PROCESS_DEFINITION_ID = "createTimersProcess:1:37";

	private static final String TEST_UPDATE_CATEGORY = "testCategory";

	private static final String TEST_DEPLOYMENT_ID = "23";
	private static final String TEST_PROCESS_DEFINITION_KEY = "createTimersProcess";

	@Resource
	private ProcessDefinitionDS processDefinitionDSRest;

	@Test
	public void testGetAllProcessDefinitions() throws Exception {
		Map<ProcessDefinitionQueryParameters, String> processDefinitionQueryParameters = new HashMap<ProcessDefinitionQueryParameters, String>();
		Map<PagingAndSortingParameters, String> pagingAndSortingParameters = new HashMap<PagingAndSortingParameters, String>();
		ProcessDefinitions processDefinitionsResponse = processDefinitionDSRest
				.getProcessDefinitions(processDefinitionQueryParameters,
						pagingAndSortingParameters);
		assertNotNull(processDefinitionsResponse);
		LOGGER.info("processDefinitionsResponse:{} ",
				processDefinitionsResponse);
	}

	@Test
	public void testGetProcessDefinitionByConditions() throws Exception {
		Map<ProcessDefinitionQueryParameters, String> processDefinitionQueryParameters = new HashMap<ProcessDefinitionQueryParameters, String>();
		Map<PagingAndSortingParameters, String> pagingAndSortingParameters = new HashMap<PagingAndSortingParameters, String>();
		processDefinitionQueryParameters.put(
				ProcessDefinitionQueryParameters.deploymentId,
				TEST_DEPLOYMENT_ID);
		processDefinitionQueryParameters.put(
				ProcessDefinitionQueryParameters.key,
				TEST_PROCESS_DEFINITION_KEY);
		
		
		ProcessDefinitions processDefinitionsResponse = processDefinitionDSRest
				.getProcessDefinitions(processDefinitionQueryParameters,
						pagingAndSortingParameters);
		assertNotNull(processDefinitionsResponse);
		assertEquals(1, processDefinitionsResponse.getProcessDefinitionSet()
				.size());
		LOGGER.info("processDefinitionsResponse:{} ",
				processDefinitionsResponse);

//		processDefinitionQueryParameters.clear();
//		processDefinitionQueryParameters.put(
//				ProcessDefinitionQueryParameters.key,
//				TEST_PROCESS_DEFINITION_KEY);
//		processDefinitionsResponse = processDefinitionDSRest
//				.getProcessDefinitions(processDefinitionQueryParameters,
//						pagingAndSortingParameters);
//		assertNotNull(processDefinitionsResponse);
//		LOGGER.info("processDefinitionsResponse:{} ",
//				processDefinitionsResponse);
	}

	@Test
	public void testGetProcessDefinition() throws Exception {
		ProcessDefinition processDefinitionResponse = processDefinitionDSRest
				.getProcessDefinitionByProcessDefinitionId(TEST_PROCESS_DEFINITION_ID);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);
	}

	// Examples (its original category)
	@Test
	public void testUpdateCategory() throws Exception {
		ProcessDefinition processDefinitionResponse = processDefinitionDSRest
				.updateCategory(TEST_PROCESS_DEFINITION_ID,
						TEST_UPDATE_CATEGORY);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);

		String updatedCategory = processDefinitionResponse.getCategory();
		assertEquals(TEST_UPDATE_CATEGORY, updatedCategory);

		Thread.sleep(500);

		processDefinitionResponse = processDefinitionDSRest.updateCategory(
				TEST_PROCESS_DEFINITION_ID, "Examples");

		updatedCategory = processDefinitionResponse.getCategory();
		assertEquals("Examples", updatedCategory);
	}

	@Test
	@Ignore("not run all the time")
	public void testSuspendProcess() throws Exception {
		ProcessDefinition processDefinitionResponse = processDefinitionDSRest
				.suspendProcessDefinition(TEST_PROCESS_DEFINITION_ID, false,
						null);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);
		assertTrue(processDefinitionResponse.getSuspended());
	}

	@Test
	@Ignore("not run all the time")
	public void testActiveProcess() throws Exception {
		ProcessDefinition processDefinitionResponse = processDefinitionDSRest
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
		Party candidate = processDefinitionDSRest.addCandidate(
				TEST_PROCESS_DEFINITION_ID, Family.users, user);
		assertNotNull(candidate);
		LOGGER.info("candidate:{} ", candidate);

		// add group as candidate
		candidate = processDefinitionDSRest.addCandidate(
				TEST_PROCESS_DEFINITION_ID, Family.groups, group);
		assertNotNull(candidate);
		LOGGER.info("candidate:{} ", candidate);

		processDefinitionDSRest.deleteCandidate(TEST_PROCESS_DEFINITION_ID,
				Family.users, user);
		processDefinitionDSRest.deleteCandidate(TEST_PROCESS_DEFINITION_ID,
				Family.groups, group);
	}

	@Test
	// @Ignore("need specific processDefinitionId, or run after @testAddCandidate")
	public void testGetCandidates() throws Exception {
		Set<Party> candidates = processDefinitionDSRest
				.getAllCandidates(TEST_PROCESS_DEFINITION_ID);
		assertNotNull(candidates);
		for (Party candidate : candidates) {
			LOGGER.info("candidate:{} ", candidate);
		}
	}
}
