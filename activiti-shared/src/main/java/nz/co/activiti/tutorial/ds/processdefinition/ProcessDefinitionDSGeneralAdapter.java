package nz.co.activiti.tutorial.ds.processdefinition;

import java.util.Map;

import nz.co.activiti.tutorial.ds.processdefinition.ProcessDefinitionDS;
import nz.co.activiti.tutorial.model.GenericCollectionModel;
import nz.co.activiti.tutorial.model.PagingAndSortingParameter;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinition;
import nz.co.activiti.tutorial.model.processdefinition.ProcessDefinitionQueryParameter;

public abstract class ProcessDefinitionDSGeneralAdapter implements
		ProcessDefinitionDS {

	@Override
	public GenericCollectionModel<ProcessDefinition> getProcessDefinitions(
			Map<ProcessDefinitionQueryParameter, String> processDefinitionQueryParameters,
			Map<PagingAndSortingParameter, String> pagingAndSortingParameters)
			throws Exception {
		return null;
	}

}
