package nz.co.activiti.tutorial.rest.deployment;

public interface DeploymentDS {

	DeploymentResponse deploymentSingleBpmn(String classpathBpmn,
			String fileName, String fileExtension) throws Exception;

	void undeployment(String deploymentId) throws Exception;
}
