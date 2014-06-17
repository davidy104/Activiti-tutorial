package nz.co.activiti.tutorial.rest;

import static org.junit.Assert.fail;

import java.util.UUID;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.ds.deployment.DeploymentDS;
import nz.co.activiti.tutorial.ds.execution.ExecutionDS;
import nz.co.activiti.tutorial.ds.group.GroupDS;
import nz.co.activiti.tutorial.ds.processdefinition.ProcessDefinitionDS;
import nz.co.activiti.tutorial.ds.processinstance.ProcessInstanceDS;
import nz.co.activiti.tutorial.ds.task.TaskDS;
import nz.co.activiti.tutorial.ds.user.UserDS;
import nz.co.activiti.tutorial.rest.config.ApplicationContextConfiguration;

import org.junit.After;
import org.junit.Before;
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
public class LaptopOrderProcessIntegrationTest {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(LaptopOrderProcessIntegrationTest.class);

	@Resource
	private DeploymentDS deploymentDSRest;

	@Resource
	private ProcessDefinitionDS processDefinitionDSRest;

	@Resource
	private ProcessInstanceDS processInstanceDSRest;

	@Resource
	private ExecutionDS executionDSRest;

	@Resource
	private GroupDS groupDSRest;

	@Resource
	private TaskDS taskDSRest;

	@Resource
	private UserDS userDSRest;

	private static final String PROCESS_LOCATION = "process/laptopOrderHumanProcess01.bpmn20.xml";

	private static final String TENANT_ID = "tenantId1111";
	private static final String PROCESS_DEFINITION_KEY = "laptopHumanProcess";

	private String processInstanceId;
	private String deploymentId;
	private String processDefinitionId;
	private String orderNo = UUID.randomUUID().toString();

	@Before
	public void initialize() throws Exception {

	}

	@After
	public void clean() throws Exception {
		if (processInstanceId != null) {
			processInstanceDSRest.deleteProcessInstance(processInstanceId);
		}
		deploymentDSRest.undeployment(deploymentId);
	}

	@Test
	public void test() {
		fail("Not yet implemented");
	}

}
