package nz.co.activiti.tutorial.ds.deployment;

import java.io.InputStream;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.model.deployment.Deployment;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.DeploymentBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service("deploymentDSGeneralImpl")
public class DeploymentDSGeneralImpl extends DeploymentDSGeneralAdapter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeploymentDSGeneralImpl.class);

	@Resource
	private RepositoryService repositoryService;

	@Override
	public Deployment deployment(String name, String category,
			InputStream resourceStream) throws Exception {
		LOGGER.info("deployment start:{}");
		LOGGER.info("name:{}", name);
		LOGGER.info("category:{}", category);
		Deployment deployment = null;
		DeploymentBuilder deploymentBuilder = repositoryService
				.createDeployment();
		if (!StringUtils.isEmpty(name)) {
			deploymentBuilder = deploymentBuilder.name(name);
		}

		if (!StringUtils.isEmpty(category)) {
			deploymentBuilder = deploymentBuilder.category(category);
		}
		org.activiti.engine.repository.Deployment activitiDeployment = deploymentBuilder
				.addInputStream(name, resourceStream).deploy();
		BeanUtils.copyProperties(activitiDeployment, deployment);

		LOGGER.info("deployment end:{}", deployment);
		return deployment;
	}

	@Override
	public void undeployment(String deploymentId) throws Exception {
		repositoryService.deleteDeployment(deploymentId, true);
	}

	@Override
	public Deployment getDeploymentByDeploymentId(String deploymentId)
			throws Exception {
		LOGGER.info("getDeploymentByDeploymentId start:{}", deploymentId);
		Deployment foundDeployment = null;
		org.activiti.engine.repository.Deployment activitiDeployment = this
				.getActivitiDeployment(deploymentId);
		BeanUtils.copyProperties(activitiDeployment, foundDeployment);
		LOGGER.info("getDeploymentByDeploymentId end:{}", foundDeployment);
		return foundDeployment;
	}

	private org.activiti.engine.repository.Deployment getActivitiDeployment(
			String deploymentId) throws NotFoundException {
		org.activiti.engine.repository.Deployment result = repositoryService
				.createDeploymentQuery().deploymentId(deploymentId)
				.singleResult();
		if (result == null) {
			throw new NotFoundException("Deployment not found by id["
					+ deploymentId + "]");
		}
		return result;
	}

}
