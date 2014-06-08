package nz.co.activiti.tutorial.rest.process;

import java.util.List;

public interface ActivitiProcessDS {

	List<ProcessDefinitionResponse> getProcessDefinitions() throws Exception;

	ProcessDefinitionResponse getProcessDefinitionById(String processId)
			throws Exception;

}
