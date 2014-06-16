package nz.co.activiti.tutorial.model.history

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import nz.co.activiti.tutorial.model.Variable
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","name","processDefinitionId","processInstanceId","executionId","tenantId"])
class HistoricTaskInstance implements Serializable {

	String id
	String processDefinitionId
	String processDefinitionUrl
	String processInstanceId
	String processInstanceUrl
	String executionId
	String name
	String description
	String deleteReason
	String owner
	String assignee
	String startTime
	String endTime
	String durationInMillis
	String workTimeInMillis

	String claimTime
	String taskDefinitionKey
	String formKey
	Integer priority
	String dueDate
	String parentTaskId
	String url
	String tenantId
	List<Variable> variables
}
