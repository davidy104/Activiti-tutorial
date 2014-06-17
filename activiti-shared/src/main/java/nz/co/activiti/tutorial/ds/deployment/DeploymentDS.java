package nz.co.activiti.tutorial.ds.deployment;

import java.io.File;
import java.util.List;
import java.util.Map;

import nz.co.activiti.tutorial.model.GenericCollectionModel;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.deployment.Deployment;
import nz.co.activiti.tutorial.model.deployment.DeploymentQueryParameter;
import nz.co.activiti.tutorial.model.deployment.DeploymentResource;

/**
 * 
 * @author dyuan
 * 
 */
public interface DeploymentDS {

	Deployment deployment(String tenantId, File uploadFile) throws Exception;

	void undeployment(String deploymentId) throws Exception;

	GenericCollectionModel<Deployment> getDeployments(
			Map<DeploymentQueryParameter, String> deploymentQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception;

	Deployment getDeploymentByDeploymentId(String deploymentId)
			throws Exception;

	List<DeploymentResource> getDeploymentResourcesByDeployId(
			String deploymentId) throws Exception;

	DeploymentResource getDeploymentResource(String deploymentId,
			String resourceId) throws Exception;

}
