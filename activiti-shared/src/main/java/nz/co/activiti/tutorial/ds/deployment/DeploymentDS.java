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
			InputStream resourceStream);

	void undeployment(String deploymentId);

	Deployment getDeploymentByDeploymentId(String deploymentId);

	List<Deployment> getDeployments(String name, String category);

	boolean checkIfDeploymentExisted(String name, String category);
}
