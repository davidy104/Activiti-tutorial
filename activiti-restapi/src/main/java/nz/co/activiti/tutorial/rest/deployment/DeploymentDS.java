package nz.co.activiti.tutorial.rest.deployment;

import java.util.List;

/**
 * 
 * @author dyuan
 * 
 */
public interface DeploymentDS {

	DeploymentResponse deployment(String tenantId, String classpathBpmn,
			String fileName, String fileExtension) throws Exception;

	void undeployment(String deploymentId) throws Exception;

	DeploymentsResponse getAllDeployments() throws Exception;

	DeploymentResponse getDeploymentByDeploymentId(String deploymentId)
			throws Exception;

	List<DeploymentResource> getDeploymentResourcesByDeployId(
			String deploymentId) throws Exception;

	DeploymentResource getDeploymentResource(String deploymentId,
			String resourceId) throws Exception;

}
