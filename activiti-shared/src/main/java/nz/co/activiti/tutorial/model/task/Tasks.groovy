package nz.co.activiti.tutorial.model.task

import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
class Tasks implements Serializable {
	Integer total
	Integer start
	Integer size
	String sort
	String order

	Set<Task> tasks

	void addTask(Task task){
		if(!tasks){
			tasks = new HashSet<Task>()
		}
		tasks << task
	}
}
