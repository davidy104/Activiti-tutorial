package nz.co.activiti.tutorial.rest.model.history

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import nz.co.activiti.tutorial.rest.model.Variable
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
	//yyyy-MM-dd'T'HH:mm:ss.SSSZ
	Date startTime
	Date endTime
	long durationInMillis
	long workTimeInMillis

	Date claimTime
	String taskDefinitionKey
	String formKey
	Integer priority
	Date dueDate
	String parentTaskId
	String url
	String tenantId
	List<Variable> variables
}
