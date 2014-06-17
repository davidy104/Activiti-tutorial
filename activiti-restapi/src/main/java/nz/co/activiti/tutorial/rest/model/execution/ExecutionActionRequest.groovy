package nz.co.activiti.tutorial.rest.model.execution

import groovy.transform.ToString
import nz.co.activiti.tutorial.rest.model.Variable
@ToString(includeNames = true, includeFields=true)
class ExecutionActionRequest implements Serializable {

	ExecutionAction action
	//Notifies the execution that a signal event has been received, requires a signalName parameter
	String signalName
	//Notifies the execution that a message event has been received, requires a messageName parameter.
	String messageName
	List<Variable> variables

}
