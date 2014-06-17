package nz.co.activiti.tutorial.model.history

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import nz.co.activiti.tutorial.model.Variable

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","processInstanceId","processInstanceUrl","taskId"])
class HistoricVariableInstance {

	String id
	String processInstanceId
	String processInstanceUrl
	String taskId
	List<Variable> variables
}
