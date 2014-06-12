package nz.co.activiti.tutorial.model.processdefinition

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class ProcessDefinitions implements Serializable {
	Integer total
	Integer start
	Integer size
	String sort
	String order
	Set<ProcessDefinition> processDefinitionSet

	void addProcessDefinition(ProcessDefinition processDefinition){
		if(!processDefinitionSet){
			processDefinitionSet = new HashSet<ProcessDefinition>()
		}
		processDefinitionSet << processDefinition
	}
}
