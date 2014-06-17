package nz.co.activiti.tutorial.rest.ds.deployment;

import java.io.File;
import java.util.List;
import java.util.Map;

import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.rest.model.deployment.Deployment;
import nz.co.activiti.tutorial.rest.model.deployment.DeploymentQueryParameter;
import nz.co.activiti.tutorial.rest.model.deployment.DeploymentResource;

/**
 * 
 * @author dyuan
 * 
 */
public interface DeploymentRestDS {

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
