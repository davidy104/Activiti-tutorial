package nz.co.activiti.tutorial.model.processinstance

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class ProcessInstances implements Serializable {
	Integer total
	Integer start
	Integer size
	String sort
	String order

	Set<ProcessInstance> processInstanceSet

	void addProcessInstance(ProcessInstance ProcessInstance){
		if(!processInstanceSet){
			processInstanceSet = new HashSet<ProcessInstance>()
		}
		processInstanceSet << ProcessInstance
	}
}
