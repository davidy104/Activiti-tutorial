package nz.co.activiti.tutorial.rest;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.deployment.DeploymentDS;
import nz.co.activiti.tutorial.model.GenericCollectionModel;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.deployment.Deployment;
import nz.co.activiti.tutorial.model.deployment.DeploymentQueryParameter;
import nz.co.activiti.tutorial.model.deployment.DeploymentResource;
import nz.co.activiti.tutorial.rest.config.ApplicationContextConfiguration;
import nz.co.activiti.tutorial.utils.GeneralUtils;

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
@Ignore("not run all the time")
public class DeploymentIntegrationTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeploymentIntegrationTest.class);

	@Resource
	private DeploymentDS deploymentDSRest;

	private static final String PROCESS_LOCATION = "process/laptopOrderHumanProcess.bpmn20.xml";

	private static final String TEST_DEPLOYMENT_RESOURCE_ID = "org/activiti/explorer/demo/process/FixSystemFailureProcess.bpmn20.xml";

	private static final String TEST_DEPLOYMENT_ID = "23";

	private static final String TENANT_ID = "tenantId7890";

	@Test
	public void testDeployment() throws Exception {

		InputStream processStream = ProcessDeploymentMetaDataTest.class
				.getClassLoader().getResourceAsStream(PROCESS_LOCATION);

		File processFile = File.createTempFile("laptopOrderHumanProcess",
				".bpmn20.xml");
		GeneralUtils.inputStreamToFile(processStream, processFile);

		Deployment deployment = deploymentDSRest.deployment(TENANT_ID,
				processFile);
		assertNotNull(deployment);
		LOGGER.info("deployment:{} ", deployment);
	}

	@Test
	public void testGetDeployments() throws Exception {
		Map<DeploymentQueryParameter, String> deploymentQueryParameters = new HashMap<DeploymentQueryParameter, String>();
		deploymentQueryParameters.put(DeploymentQueryParameter.tenantId,
				"tenantId7890");

		Map<PagingAndSortingParameter, String> pagingAndSortingParameters = new HashMap<PagingAndSortingParameter, String>();
		// pagingAndSortingParameters.put(PagingAndSortingParameters.size, "2");

		GenericCollectionModel<Deployment> deploymentsResponse = deploymentDSRest
				.getDeployments(deploymentQueryParameters,
						pagingAndSortingParameters);
		assertNotNull(deploymentsResponse);
		LOGGER.info("deploymentsResponse:{} ", deploymentsResponse);
	}

	@Test
	public void testGetDeploymentByDeploymentId() throws Exception {
		Deployment deploymentResponse = deploymentDSRest
				.getDeploymentByDeploymentId(TEST_DEPLOYMENT_ID);
		assertNotNull(deploymentResponse);
		LOGGER.info("deploymentResponse:{} ", deploymentResponse);
	}

	@Test
	public void testGetDeploymentResourcesByDeployId() throws Exception {
		List<DeploymentResource> deploymentResources = deploymentDSRest
				.getDeploymentResourcesByDeployId(TEST_DEPLOYMENT_ID);
		assertNotNull(deploymentResources);

		for (DeploymentResource deploymentResource : deploymentResources) {
			LOGGER.info("deploymentResource:{} ", deploymentResource);
		}
	}

	@Test
	// @Ignore("try to resolve it later, for sending encoding parameter")
	public void testGetDeploymentResource() throws Exception {
		DeploymentResource deploymentResource = deploymentDSRest
				.getDeploymentResource(TEST_DEPLOYMENT_ID,
						TEST_DEPLOYMENT_RESOURCE_ID);
		assertNotNull(deploymentResource);

		LOGGER.info("deploymentResource:{} ", deploymentResource);
	}

	@Test
	// @Ignore("we need to know exact deploymentId before deleting")
	public void testUnDeployment() throws Exception {
		String deploymentId = "14013";
		deploymentDSRest.undeployment(deploymentId);
	}
}
