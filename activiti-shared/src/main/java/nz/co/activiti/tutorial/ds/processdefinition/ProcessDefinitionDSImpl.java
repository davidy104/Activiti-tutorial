package nz.co.activiti.tutorial.ds.processdefinition;

import java.util.Date;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.task.IdentityLink;

public class ProcessDefinitionDSImpl implements ProcessDefinitionDS{
	
	@Resource
	private RepositoryService repositoryService;
	
	@Override
	public ProcessDefinition getProcessDefinitionByProcessDefinitionId(
			String processDefinitionId) throws Exception {
		repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId);
		return null;
	}

	@Override
	public ProcessDefinition updateCategory(String processDefinitionId,
			String category) throws Exception {
		repositoryService.setProcessDefinitionCategory(processDefinitionId, category);
		return null;
	}

	@Override
	public ProcessDefinition suspendProcessDefinition(
			String processDefinitionId, boolean includeProcessInstances,
			Date effectiveDate) throws Exception {
		
		return null;
	}

	@Override
	public ProcessDefinition activeProcessDefinition(
			String processDefinitionId, boolean includeProcessInstances,
			Date effectiveDate) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<IdentityLink> getAllIdentities(String processDefinitionId)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addIdentity(String processDefinitionId, String name)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteIdentity(String processDefinitionId, String identityId)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IdentityLink getIdentity(String processDefinitionId,
			String identityId) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
