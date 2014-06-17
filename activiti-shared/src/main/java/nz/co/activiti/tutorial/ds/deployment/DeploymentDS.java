package nz.co.activiti.tutorial.ds.deployment;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.repository.Deployment;

/**
 * 
 * @author dyuan
 * 
 */
public interface DeploymentDS {

	Deployment deployment(String name, String category,
			InputStream resourceStream) throws Exception;

	void undeployment(String deploymentId) throws Exception;

	Deployment getDeploymentByDeploymentId(String deploymentId)
			throws Exception;

	List<Deployment> getDeployments(String name, String category)
			throws Exception;

}
