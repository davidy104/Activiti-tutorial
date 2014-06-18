package nz.co.activiti.tutorial.ds.processdefinition;

import java.util.Date;
import java.util.Set;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;

public interface ProcessDefinitionDS {

	ProcessDefinition getProcessDefinitionByProcessDefinitionId(
			String processDefinitionId) throws Exception;

	ProcessDefinition updateCategory(String processDefinitionId, String category)
			throws Exception;

	ProcessDefinition suspendProcessDefinition(String processDefinitionId,
			boolean includeProcessInstances, Date effectiveDate)
			throws Exception;

	ProcessDefinition activeProcessDefinition(String processDefinitionId,
			boolean includeProcessInstances, Date effectiveDate)
			throws Exception;

	Set<IdentityLink> getAllIdentities(String processDefinitionId)
			throws Exception;

	void addIdentity(String processDefinitionId, String name) throws Exception;

	void deleteIdentity(String processDefinitionId, String identityId)
			throws Exception;

	IdentityLink getIdentity(String processDefinitionId, String identityId)
			throws Exception;
}
