package nz.co.activiti.tutorial.model

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class GenericCollectionModel<T> {
	Integer total
	Integer start
	Integer size
	String sort
	String order
	List<T> modelList
	void addModel(T model){
		if(!modelList){
			modelList = new ArrayList<T>()
		}
		modelList << model
	}
}
