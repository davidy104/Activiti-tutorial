package nz.co.activiti.tutorial.model.history

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class HistoricVariableInstances {
	Integer total
	Integer start
	Integer size
	String sort
	String order

	Set<HistoricVariableInstance> historicVariableInstanceSet

	void addHistoricVariableInstance(HistoricVariableInstance historicVariableInstance){
		if(!historicVariableInstanceSet){
			historicVariableInstanceSet = new HashSet<HistoricVariableInstance>()
		}
		historicVariableInstanceSet << historicVariableInstance
	}
}
