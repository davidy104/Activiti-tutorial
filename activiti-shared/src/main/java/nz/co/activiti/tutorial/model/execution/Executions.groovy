package nz.co.activiti.tutorial.model.execution

import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
class Executions implements Serializable {
	Integer total

	Integer start
	Integer size
	String sort
	String order

	Set<Execution> executionSet

	void addExecution(Execution execution){
		if(!executionSet){
			executionSet = new HashSet<Execution>()
		}
		executionSet << execution
	}
}
