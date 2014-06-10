package nz.co.activiti.tutorial.rest.processdefinition

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class ProcessDefinitionsResponse implements Serializable {
	Integer total
	Integer start
	String sort
	String order
	Integer size

	Set<ProcessDefinitionResponse> processDefinitionResponseSet

	void addProcessDefinitionResponse(ProcessDefinitionResponse processDefinitionResponse){
		if(!processDefinitionResponseSet){
			processDefinitionResponseSet = new HashSet<ProcessDefinitionResponse>()
		}
		processDefinitionResponseSet << processDefinitionResponse
	}
}
