package nz.co.activiti.tutorial.rest.model.history

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","activityId","activityName","activityType","processDefinitionId","processInstanceId","taskId"])
class HistoricActivityInstance implements Serializable {

	String id
	String activityId
	String activityName
	String activityType
	String processDefinitionId
	String processDefinitionUrl
	String processInstanceId
	String processInstanceUrl
	String executionId
	String taskId
	String calledProcessInstanceId
	String assignee
	//yyyy-MM-dd'T'HH:mm:ss.SSSZ
	Date startTime
	Date endTime
	long durationInMillis
	String tenantId
}
