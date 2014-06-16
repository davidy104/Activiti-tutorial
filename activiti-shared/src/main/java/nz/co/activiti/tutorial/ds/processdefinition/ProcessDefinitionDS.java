package nz.co.activiti.tutorial.ds.processdefinition;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import nz.co.activiti.tutorial.model.Family;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.Identity;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinition;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinitionQueryParameter;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinitions;

public interface ProcessDefinitionDS {

	ProcessDefinitions getProcessDefinitions(
			Map<ProcessDefinitionQueryParameter, String> processDefinitionQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception;

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

	Set<Identity> getAllIdentities(String processDefinitionId) throws Exception;

	Identity addIdentity(String processDefinitionId, Family family, String name)
			throws Exception;

	/**
	 * 
	 * @param processDefinitionId
	 * @param family
	 *            Either users or groups, depending on the type of identity
	 *            link.
	 * @param identityId
	 *            Either the userId or groupId of the identity to remove as
	 *            candidate starter
	 * @return
	 * @throws Exception
	 */
	void deleteIdentity(String processDefinitionId, Family family,
			String identityId) throws Exception;

	Identity getIdentity(String processDefinitionId, Family family,
			String identityId) throws Exception;
}
