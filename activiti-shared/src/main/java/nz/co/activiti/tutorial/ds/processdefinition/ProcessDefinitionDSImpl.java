package nz.co.activiti.tutorial.ds.processdefinition;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.DuplicatedException;
import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.Family;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.ActivitiObjectNotFoundException;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.task.IdentityLink;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessDefinitionDSImpl implements ProcessDefinitionDS {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessDefinitionDSImpl.class);

	@Resource
	private RepositoryService repositoryService;

	@Override
	public ProcessDefinition getProcessDefinitionByProcessDefinitionId(
			String processDefinitionId) {
		return repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();
	}

	@Override
	public ProcessDefinition getProcessDefinition(String processDefinitionName,
			String processDefinitionCategory, String deploymentId) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService
				.createProcessDefinitionQuery().processDefinitionTenantId(processDefinitionName);

		if (!StringUtils.isEmpty(processDefinitionName)) {
			processDefinitionQuery = processDefinitionQuery
					.processDefinitionName(processDefinitionName);
		}
		if (!StringUtils.isEmpty(processDefinitionCategory)) {
			processDefinitionQuery = processDefinitionQuery
					.processDefinitionCategory(processDefinitionCategory);
		}

		if (!StringUtils.isEmpty(deploymentId)) {
			processDefinitionQuery = processDefinitionQuery
					.deploymentId(deploymentId);
		}

		return processDefinitionQuery.singleResult();
	}

	@Override
	public void updateCategory(String processDefinitionId, String category)
			throws NotFoundException {
		try {
			repositoryService.setProcessDefinitionCategory(processDefinitionId,
					category);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException("ProcessDefinition not found by id["
					+ processDefinitionId + "]");
		}
	}

	@Override
	public void suspendProcessDefinition(String processDefinitionId,
			boolean includeProcessInstances, Date effectiveDate)
			throws Exception {
		try {
			repositoryService.suspendProcessDefinitionById(processDefinitionId,
					includeProcessInstances, effectiveDate);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException(e);
		} catch (ActivitiException e1) {
			throw new DuplicatedException(e1);
		}
	}

	@Override
	public void activeProcessDefinition(String processDefinitionId,
			boolean includeProcessInstances, Date effectiveDate)
			throws Exception {
		try {
			repositoryService
					.activateProcessDefinitionById(processDefinitionId,
							includeProcessInstances, effectiveDate);
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException(e);
		} catch (ActivitiException e1) {
			throw new DuplicatedException(e1);
		}
	}

	@Override
	public List<IdentityLink> getAllIdentities(String processDefinitionId)
			throws Exception {
		return repositoryService
				.getIdentityLinksForProcessDefinition(processDefinitionId);
	}

	@Override
	public void addCandidateStarter(String processDefinitionId, Family family,
			String identityId) throws NotFoundException {
		LOGGER.info("addIdentity start:{}", processDefinitionId);
		LOGGER.info("family:{}", family);
		LOGGER.info("identityId:{}", identityId);
		try {
			if (family == Family.groups) {
				repositoryService.addCandidateStarterGroup(processDefinitionId,
						identityId);
			} else if (family == Family.users) {
				repositoryService.addCandidateStarterUser(processDefinitionId,
						identityId);
			} else {
				throw new IllegalArgumentException("Family[" + family
						+ "] can not be identified");
			}

		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException(e);
		}
	}

	@Override
	public void deleteCandidateStarter(String processDefinitionId,
			Family family, String identityId) throws NotFoundException {
		try {
			if (family == Family.groups) {
				repositoryService.deleteCandidateStarterGroup(
						processDefinitionId, identityId);
			} else if (family == Family.users) {
				repositoryService.deleteCandidateStarterUser(
						processDefinitionId, identityId);
			} else {
				throw new IllegalArgumentException("Family[" + family
						+ "] can not be identified");
			}
		} catch (ActivitiObjectNotFoundException e) {
			throw new NotFoundException(e);
		}

	}

	@Override
	public IdentityLink getIdentity(String processDefinitionId, Family family,
			String identityId) throws Exception {
		List<IdentityLink> identities = this
				.getAllIdentities(processDefinitionId);
		if (identities != null) {
			for (IdentityLink identity : identities) {
				if (family == Family.groups
						&& !StringUtils.isEmpty(identity.getGroupId())
						&& identity.getGroupId().equals(identityId)) {
					return identity;
				} else if (family == Family.users
						&& !StringUtils.isEmpty(identity.getUserId())
						&& identity.getUserId().equals(identityId)) {
					return identity;
				}
			}
		}
		return null;
	}

}
