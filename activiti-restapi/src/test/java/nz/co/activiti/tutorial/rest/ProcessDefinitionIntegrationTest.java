package nz.co.activiti.tutorial.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.processdefinition.ProcessDefinitionDS;
import nz.co.activiti.tutorial.model.Party;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinition;
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

	@Resource
	private ProcessDefinitionDS processDefinitionDSRest;

	@Test
	public void testGetAllProcessDefinitions() throws Exception {
		ProcessDefinitions processDefinitionsResponse = processDefinitionDSRest
				.getAllProcessDefinitions();
		assertNotNull(processDefinitionsResponse);
		LOGGER.info("processDefinitionsResponse:{} ",
				processDefinitionsResponse);
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
				.suspendProcess(TEST_PROCESS_DEFINITION_ID, null);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);
		assertTrue(processDefinitionResponse.getSuspended());
	}

	@Test
	@Ignore("not run all the time")
	public void testActiveProcess() throws Exception {
		ProcessDefinition processDefinitionResponse = processDefinitionDSRest
				.activeProcess(TEST_PROCESS_DEFINITION_ID, null);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);
		assertFalse(processDefinitionResponse.getSuspended());
	}

	@Test
	@Ignore("not run all the time")
	public void testAddCandidate() throws Exception {
		// add user as candidate
		Party candidate = processDefinitionDSRest.addCandidate(
				TEST_PROCESS_DEFINITION_ID, "users", "kermit");
		assertNotNull(candidate);
		LOGGER.info("candidate:{} ", candidate);

		// add group as candidate
		candidate = processDefinitionDSRest.addCandidate(
				TEST_PROCESS_DEFINITION_ID, "groups", "engineering");
		assertNotNull(candidate);
		LOGGER.info("candidate:{} ", candidate);
	}

	// family: users or groups
	@Test
	// @Ignore("run after testAddCandidate")
	public void testDeleteCandidate() throws Exception {
		processDefinitionDSRest.deleteCandidate(TEST_PROCESS_DEFINITION_ID,
				"users", "kermit");
		processDefinitionDSRest.deleteCandidate(TEST_PROCESS_DEFINITION_ID,
				"groups", "engineering");
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
