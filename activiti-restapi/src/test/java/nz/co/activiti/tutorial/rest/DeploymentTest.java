package nz.co.activiti.tutorial.rest;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.rest.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.rest.deployment.DeploymentDS;
import nz.co.activiti.tutorial.rest.deployment.DeploymentResponse;
import nz.co.activiti.tutorial.rest.deployment.DeploymentsResponse;

import org.junit.Ignore;
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
public class DeploymentTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeploymentTest.class);

	@Resource
	private DeploymentDS deploymentDS;

	private static final String PROCESS_LOCATION = "process/laptopOrderHumanProcess.bpmn20.xml";

	@Test
	public void testDeployment() throws Exception {
		DeploymentResponse deploymentResponse = deploymentDS.deployment(
				"tenantId7890", PROCESS_LOCATION, "laptopOrderHumanProcess",
				".bpmn20.xml");
		assertNotNull(deploymentResponse);
		LOGGER.info("deploymentResponse:{} ", deploymentResponse);
	}

	@Test
	public void testGetAllDeployments() throws Exception {
		DeploymentsResponse deploymentsResponse = deploymentDS
				.getAllDeployments();
		assertNotNull(deploymentsResponse);
		LOGGER.info("deploymentsResponse:{} ", deploymentsResponse);
	}

	@Test
	// @Ignore("we need to know exact deploymentId before deleting")
	public void testUnDeployment() throws Exception {
		String deploymentId = "40";
		deploymentDS.undeployment(deploymentId);
	}
}
