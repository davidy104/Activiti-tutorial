package nz.co.activiti.tutorial.rest.processdefinition;

import java.util.Date;
import java.util.Set;

public interface ProcessDefinitionDS {

	ProcessDefinitionsResponse getAllProcessDefinitions() throws Exception;

	ProcessDefinitionResponse getProcessDefinitionByProcessDefinitionId(
			String processDefinitionId) throws Exception;

	ProcessDefinitionResponse updateCategory(String processDefinitionId,
			String category) throws Exception;

	ProcessDefinitionResponse suspendProcess(String processDefinitionId,
			Date effectiveDate) throws Exception;

	ProcessDefinitionResponse activeProcess(String processDefinitionId,
			Date effectiveDate) throws Exception;

	Set<Candidate> getAllCandidates(String processDefinitionId)
			throws Exception;

	Candidate addCandidate(String processDefinitionId, String family,
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

	Candidate getCandidate(String processDefinitionId, String family,
			String identityId) throws Exception;
}
