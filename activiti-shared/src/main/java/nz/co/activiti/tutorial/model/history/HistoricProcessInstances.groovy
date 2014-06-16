package nz.co.activiti.tutorial.model.history

import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
class HistoricProcessInstances implements Serializable {

	Integer total
	Integer start
	Integer size
	String sort
	String order

	Set<HistoricProcessInstance> historicProcessInstanceSet

	void addHistoricProcessInstance(HistoricProcessInstance historicProcessInstance){
		if(!historicProcessInstanceSet){
			historicProcessInstanceSet = new HashSet<HistoricProcessInstance>()
		}
		historicProcessInstanceSet << historicProcessInstance
	}
}
