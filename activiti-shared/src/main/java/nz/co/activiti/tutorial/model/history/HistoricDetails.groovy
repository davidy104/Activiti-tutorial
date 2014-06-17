package nz.co.activiti.tutorial.model.history

import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
class HistoricDetails implements Serializable {
	Integer total
	Integer start
	Integer size
	String sort
	String order

	Set<HistoricDetail> historicDetailSet

	void addHistoricDetail(HistoricDetail historicDetail){
		if(!historicDetailSet){
			historicDetailSet = new HashSet<HistoricDetail>()
		}
		historicDetailSet << historicDetail
	}
}
