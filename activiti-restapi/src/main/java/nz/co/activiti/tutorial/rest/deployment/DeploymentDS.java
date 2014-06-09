package nz.co.activiti.tutorial.rest.deployment;

public interface DeploymentDS {

	DeploymentResponse deployment(String tenantId, String classpathBpmn,
			String fileName, String fileExtension) throws Exception;

	void undeployment(String deploymentId) throws Exception;

	DeploymentsResponse getAllDeployments() throws Exception;
}
