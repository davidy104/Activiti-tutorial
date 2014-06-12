package nz.co.activiti.tutorial.ds.processdefinition;

import java.util.Date;
import java.util.Set;

import nz.co.activiti.tutorial.model.Party;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinition;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinitions;

public interface ProcessDefinitionDS {

	ProcessDefinitions getAllProcessDefinitions() throws Exception;

	ProcessDefinition getProcessDefinitionByProcessDefinitionId(
			String processDefinitionId) throws Exception;

	ProcessDefinition updateCategory(String processDefinitionId,
			String category) throws Exception;

	ProcessDefinition suspendProcess(String processDefinitionId,
			Date effectiveDate) throws Exception;

	ProcessDefinition activeProcess(String processDefinitionId,
			Date effectiveDate) throws Exception;

	Set<Party> getAllCandidates(String processDefinitionId)
			throws Exception;

	Party addCandidate(String processDefinitionId, String family,
			String name) throws Exception;

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
	void deleteCandidate(String processDefinitionId, String family,
			String identityId) throws Exception;

	Party getCandidate(String processDefinitionId, String family,
			String identityId) throws Exception;
}
