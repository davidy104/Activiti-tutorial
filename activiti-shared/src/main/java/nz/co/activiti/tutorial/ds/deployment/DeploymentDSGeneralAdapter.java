package nz.co.activiti.tutorial.ds.deployment;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import nz.co.activiti.tutorial.ds.deployment.DeploymentDS;
import nz.co.activiti.tutorial.model.GenericCollectionModel;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.deployment.Deployment;
import nz.co.activiti.tutorial.model.deployment.DeploymentQueryParameter;
import nz.co.activiti.tutorial.model.deployment.DeploymentResource;

public abstract class DeploymentDSGeneralAdapter implements DeploymentDS {

	@Override
	public GenericCollectionModel<Deployment> getDeployments(
			Map<DeploymentQueryParameter, String> deploymentQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		return null;
	}

	@Override
	public Deployment deployment(String tenantId, File uploadFile)
			throws Exception {
		return null;
	}

	@Override
	public List<DeploymentResource> getDeploymentResourcesByDeployId(
			String deploymentId) throws Exception {
		return null;
	}

	@Override
	public DeploymentResource getDeploymentResource(String deploymentId,
			String resourceId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	public abstract Deployment deployment(String name, String category,
			InputStream resourceStream) throws Exception;

}
