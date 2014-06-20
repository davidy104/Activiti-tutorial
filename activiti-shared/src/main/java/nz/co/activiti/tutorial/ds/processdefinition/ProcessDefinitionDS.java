package nz.co.activiti.tutorial.ds.processdefinition;

import java.util.Date;
import java.util.List;

import nz.co.activiti.tutorial.NotFoundException;
import nz.co.activiti.tutorial.ds.Family;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;

public interface ProcessDefinitionDS {

	ProcessDefinition getProcessDefinitionByProcessDefinitionId(
			String processDefinitionId);

	ProcessDefinition getProcessDefinitionByDeploymentId(String deploymentId);

	void updateCategory(String processDefinitionId, String category)
			throws NotFoundException;

	void suspendProcessDefinition(String processDefinitionId,
			boolean includeProcessInstances, Date effectiveDate)
			throws Exception;

	void activeProcessDefinition(String processDefinitionId,
			boolean includeProcessInstances, Date effectiveDate)
			throws Exception;

	List<IdentityLink> getAllIdentities(String processDefinitionId)
			throws Exception;

	void addCandidateStarter(String processDefinitionId, Family family,
			String identityId) throws NotFoundException;

	void deleteCandidateStarter(String processDefinitionId, Family family,
			String identityId) throws NotFoundException;

	IdentityLink getIdentity(String processDefinitionId, Family family,
			String identityId) throws Exception;
}
