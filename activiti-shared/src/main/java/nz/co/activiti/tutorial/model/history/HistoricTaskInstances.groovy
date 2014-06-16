package nz.co.activiti.tutorial.model.history

import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
class HistoricTaskInstances implements Serializable {
	Integer total
	Integer start
	Integer size
	String sort
	String order

	Set<HistoricTaskInstance> historicTaskInstanceSet

	void addHistoricTaskInstance(HistoricTaskInstance historicTaskInstance){
		if(!historicTaskInstanceSet){
			historicTaskInstanceSet = new HashSet<HistoricTaskInstance>()
		}
		historicTaskInstanceSet << historicTaskInstance
	}
}
