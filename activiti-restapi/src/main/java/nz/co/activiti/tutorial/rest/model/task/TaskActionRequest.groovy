package nz.co.activiti.tutorial.rest.model.task

import groovy.transform.ToString
import nz.co.activiti.tutorial.ds.TaskAction
import nz.co.activiti.tutorial.rest.model.Variable

@ToString(includeNames = true, includeFields=true)
class TaskActionRequest  implements Serializable {
	TaskAction action
	String assignee
	List<Variable> variables
}
