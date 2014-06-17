package nz.co.activiti.tutorial.ds.processdefinition;

import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import nz.co.activiti.tutorial.model.Family;
import nz.co.activiti.tutorial.model.Identity;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinition;

import org.activiti.engine.RepositoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("processDefinitionDSGeneralImpl")
public class ProcessDefinitionDSGeneralImpl extends
		ProcessDefinitionDSGeneralAdapter {

	private static final Logger LOGGER = LoggerFactory
			.getLogger(ProcessDefinitionDSGeneralImpl.class);

	@Resource
	private RepositoryService repositoryService;

	@Override
	public ProcessDefinition getProcessDefinitionByProcessDefinitionId(
			String processDefinitionId) throws Exception {

		repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processDefinitionId).singleResult();

		return null;
	}

	@Override
	public ProcessDefinition updateCategory(String processDefinitionId,
			String category) throws Exception {
		repositoryService.setProcessDefinitionCategory(processDefinitionId,
				category);
		return null;
	}

	@Override
	public ProcessDefinition suspendProcessDefinition(
			String processDefinitionId, boolean includeProcessInstances,
			Date effectiveDate) throws Exception {
		repositoryService.suspendProcessDefinitionById(processDefinitionId,
				includeProcessInstances, effectiveDate);
		return null;
	}

	@Override
	public ProcessDefinition activeProcessDefinition(
			String processDefinitionId, boolean includeProcessInstances,
			Date effectiveDate) throws Exception {

		return null;
	}

	@Override
	public Set<Identity> getAllIdentities(String processDefinitionId)
			throws Exception {

		return null;
	}

	@Override
	public Identity addIdentity(String processDefinitionId, Family family,
			String name) throws Exception {

		return null;
	}

	@Override
	public void deleteIdentity(String processDefinitionId, Family family,
			String identityId) throws Exception {

	}

	@Override
	public Identity getIdentity(String processDefinitionId, Family family,
			String identityId) throws Exception {
	
		return null;
	}

	// private ProcessDefinition toProcessDefinition(
	// ProcessDefinitionEntity processDefinitionEntity) {
	// ProcessDefinition processDefinition = new ProcessDefinition();
	// processDefinition.setCategory(processDefinitionEntity.getCategory());
	// processDefinition.setDeploymentId(processDefinitionEntity
	// .getDeploymentId());
	// processDefinition.setDescription(processDefinitionEntity
	// .getDescription());
	// processDefinition.setDiagramResource(processDefinitionEntity
	// .getDiagramResourceName());
	// processDefinition.setGraphicalNotationDefined(processDefinitionEntity
	// .isGraphicalNotationDefined());
	// processDefinition.setId(processDefinitionEntity.getId());
	// processDefinition.setKey(processDefinitionEntity.getKey());
	// processDefinition.setName(processDefinitionEntity.getName());
	// processDefinition
	// .setResource(processDefinitionEntity.getResourceName());
	// processDefinition.setStartFormDefined(processDefinitionEntity
	// .getHasStartFormKey());
	// processDefinition.setSuspended(processDefinitionEntity.isSuspended());
	// processDefinition.setVersion(processDefinitionEntity.getVersion());
	// return processDefinition;
	// }

}
