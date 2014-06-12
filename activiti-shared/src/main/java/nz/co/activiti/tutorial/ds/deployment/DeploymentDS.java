package nz.co.activiti.tutorial.ds.deployment;

import java.util.List;

import nz.co.activiti.tutorial.model.deployment.DeploymentResource;
import nz.co.activiti.tutorial.model.deployment.Deployment;
import nz.co.activiti.tutorial.model.deployment.Deployments;

/**
 * 
 * @author dyuan
 * 
 */
public interface DeploymentDS {

	Deployment deployment(String tenantId, String classpathBpmn,
			String fileName, String fileExtension) throws Exception;

	void undeployment(String deploymentId) throws Exception;

	Deployments getAllDeployments() throws Exception;

	Deployment getDeploymentByDeploymentId(String deploymentId)
			throws Exception;

	List<DeploymentResource> getDeploymentResourcesByDeployId(
			String deploymentId) throws Exception;

	DeploymentResource getDeploymentResource(String deploymentId,
			String resourceId) throws Exception;

}
