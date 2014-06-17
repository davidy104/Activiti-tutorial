package nz.co.activiti.tutorial.rest.ds.processdefinition;

import java.util.Date;
import java.util.Map;
import java.util.Set;

import nz.co.activiti.tutorial.rest.model.Family;
import nz.co.activiti.tutorial.rest.model.GenericCollectionModel;
import nz.co.activiti.tutorial.rest.model.Identity;
import nz.co.activiti.tutorial.rest.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.rest.model.processdefinition.ProcessDefinition;
import nz.co.activiti.tutorial.rest.model.processdefinition.ProcessDefinitionQueryParameter;

public interface ProcessDefinitionRestDS {

	GenericCollectionModel<ProcessDefinition> getProcessDefinitions(
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
