package nz.co.activiti.tutorial.rest;

import java.util.List;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.rest.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.rest.deployment.DeploymentDS;
import nz.co.activiti.tutorial.rest.deployment.DeploymentResource;
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
public class DeploymentIntegrationTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeploymentIntegrationTest.class);

	@Resource
	private DeploymentDS deploymentDS;

	private static final String PROCESS_LOCATION = "process/laptopOrderHumanProcess.bpmn20.xml";

	private static final String TEST_DEPLOYMENT_RESOURCE_ID = "org/activiti/explorer/demo/process/FixSystemFailureProcess.bpmn20.xml";

	private static final String TEST_DEPLOYMENT_ID = "23";

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
	public void testGetDeploymentByDeploymentId() throws Exception {
		DeploymentResponse deploymentResponse = deploymentDS
				.getDeploymentByDeploymentId(TEST_DEPLOYMENT_ID);
		assertNotNull(deploymentResponse);
		LOGGER.info("deploymentResponse:{} ", deploymentResponse);
	}

	@Test
	public void testGetDeploymentResourcesByDeployId() throws Exception {
		List<DeploymentResource> deploymentResources = deploymentDS
				.getDeploymentResourcesByDeployId(TEST_DEPLOYMENT_ID);
		assertNotNull(deploymentResources);

		for (DeploymentResource deploymentResource : deploymentResources) {
			LOGGER.info("deploymentResource:{} ", deploymentResource);
		}
	}

	@Test
	@Ignore("try to resolve it later, for sending encoding parameter")
	public void testGetDeploymentResource() throws Exception {
		DeploymentResource deploymentResource = deploymentDS
				.getDeploymentResource(TEST_DEPLOYMENT_ID,
						TEST_DEPLOYMENT_RESOURCE_ID);
		assertNotNull(deploymentResource);

		LOGGER.info("deploymentResource:{} ", deploymentResource);
	}

	@Test
//	@Ignore("we need to know exact deploymentId before deleting")
	public void testUnDeployment() throws Exception {
		String deploymentId = "9102";
		deploymentDS.undeployment(deploymentId);
	}
}
