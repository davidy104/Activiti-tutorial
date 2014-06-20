package nz.co.activiti.tutorial.ds.deployment;

import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.DeploymentBuilder;
import org.activiti.engine.repository.DeploymentQuery;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DeploymentDSImpl implements DeploymentDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(DeploymentDSImpl.class);

	@Resource
	private RepositoryService repositoryService;

	@Override
	public Deployment deployment(String name, String category,
			InputStream resourceStream) {
		LOGGER.info("deployment start:{}");
		LOGGER.info("name:{}", name);
		LOGGER.info("category:{}", category);
		DeploymentBuilder deploymentBuilder = repositoryService
				.createDeployment();
		if (!StringUtils.isEmpty(name)) {
			deploymentBuilder = deploymentBuilder.name(name);
		}

		if (!StringUtils.isEmpty(category)) {
			deploymentBuilder = deploymentBuilder.category(category);
		}
		LOGGER.info("deployment end:{}");
		return deploymentBuilder.addInputStream(name, resourceStream).deploy();
	}

	@Override
	public void undeployment(String deploymentId) {
		repositoryService.deleteDeployment(deploymentId, true);
	}

	@Override
	public Deployment getDeploymentByDeploymentId(String deploymentId) {
		return repositoryService.createDeploymentQuery()
				.deploymentId(deploymentId).singleResult();
	}

	@Override
	public List<Deployment> getDeployments(String name, String category) {
		LOGGER.info("getDeployments start:{} ");
		LOGGER.info("name:{} ", name);
		LOGGER.info("category:{} ", category);
		DeploymentQuery deploymentQuery = repositoryService
				.createDeploymentQuery();
		if (!StringUtils.isEmpty(name)) {
			deploymentQuery = deploymentQuery.deploymentName(name);
		}
		if (!StringUtils.isEmpty(category)) {
			deploymentQuery = deploymentQuery.deploymentCategory(category);
		}
		LOGGER.info("getDeployments end:{} ");
		return deploymentQuery.list();
	}

	@Override
	public boolean checkIfDeploymentExisted(String name, String category) {
		long count = repositoryService.createDeploymentQuery()
				.deploymentName(name).deploymentCategory(category).count();
		if (count == 1) {
			return true;
		}
		return false;
	}

}
