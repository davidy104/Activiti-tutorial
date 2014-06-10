package nz.co.activiti.tutorial.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.rest.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.rest.processdefinition.Candidate;
import nz.co.activiti.tutorial.rest.processdefinition.ProcessDefinitionDS;
import nz.co.activiti.tutorial.rest.processdefinition.ProcessDefinitionResponse;
import nz.co.activiti.tutorial.rest.processdefinition.ProcessDefinitionsResponse;

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
	private ProcessDefinitionDS processDefinitionDS;

	@Test
	public void testGetAllProcessDefinitions() throws Exception {
		ProcessDefinitionsResponse processDefinitionsResponse = processDefinitionDS
				.getAllProcessDefinitions();
		assertNotNull(processDefinitionsResponse);
		LOGGER.info("processDefinitionsResponse:{} ",
				processDefinitionsResponse);
	}

	@Test
	public void testGetProcessDefinition() throws Exception {
		ProcessDefinitionResponse processDefinitionResponse = processDefinitionDS
				.getProcessDefinitionByProcessDefinitionId(TEST_PROCESS_DEFINITION_ID);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);
	}

	// Examples (its original category)
	@Test
	public void testUpdateCategory() throws Exception {
		ProcessDefinitionResponse processDefinitionResponse = processDefinitionDS
				.updateCategory(TEST_PROCESS_DEFINITION_ID,
						TEST_UPDATE_CATEGORY);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);

		String updatedCategory = processDefinitionResponse.getCategory();
		assertEquals(TEST_UPDATE_CATEGORY, updatedCategory);

		Thread.sleep(500);

		processDefinitionResponse = processDefinitionDS.updateCategory(
				TEST_PROCESS_DEFINITION_ID, "Examples");

		updatedCategory = processDefinitionResponse.getCategory();
		assertEquals("Examples", updatedCategory);
	}

	@Test
	@Ignore("not run all the time")
	public void testSuspendProcess() throws Exception {
		ProcessDefinitionResponse processDefinitionResponse = processDefinitionDS
				.suspendProcess(TEST_PROCESS_DEFINITION_ID, null);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);
		assertTrue(processDefinitionResponse.getSuspended());
	}

	@Test
	@Ignore("not run all the time")
	public void testActiveProcess() throws Exception {
		ProcessDefinitionResponse processDefinitionResponse = processDefinitionDS
				.activeProcess(TEST_PROCESS_DEFINITION_ID, null);
		assertNotNull(processDefinitionResponse);
		LOGGER.info("processDefinitionResponse:{} ", processDefinitionResponse);
		assertFalse(processDefinitionResponse.getSuspended());
	}

	@Test
	@Ignore("not run all the time")
	public void testAddCandidate() throws Exception {
		// add user as candidate
		Candidate candidate = processDefinitionDS.addCandidate(
				TEST_PROCESS_DEFINITION_ID, "users", "kermit");
		assertNotNull(candidate);
		LOGGER.info("candidate:{} ", candidate);

		// add group as candidate
		candidate = processDefinitionDS.addCandidate(
				TEST_PROCESS_DEFINITION_ID, "groups", "engineering");
		assertNotNull(candidate);
		LOGGER.info("candidate:{} ", candidate);
	}

	// family: users or groups
	@Test
	// @Ignore("run after testAddCandidate")
	public void testDeleteCandidate() throws Exception {
		processDefinitionDS.deleteCandidate(TEST_PROCESS_DEFINITION_ID,
				"users", "kermit");
		processDefinitionDS.deleteCandidate(TEST_PROCESS_DEFINITION_ID,
				"groups", "engineering");
	}

	@Test
	// @Ignore("need specific processDefinitionId, or run after @testAddCandidate")
	public void testGetCandidates() throws Exception {
		Set<Candidate> candidates = processDefinitionDS
				.getAllCandidates(TEST_PROCESS_DEFINITION_ID);
		assertNotNull(candidates);
		for (Candidate candidate : candidates) {
			LOGGER.info("candidate:{} ", candidate);
		}
	}
}
