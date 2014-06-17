package nz.co.activiti.tutorial.rest.model.task

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","name","processDefinition","processInstance","taskDefinitionKey","url"])
class Task implements Serializable {

	String assignee
	//format:yyyy-MM-dd'T'HH:mm:ss.SSSZ
	String createTime
	String delegationState
	String description

	//format:yyyy-MM-dd'T'HH:mm:ss.SSSZ
	String dueDate
	String execution
	String id
	String name
	String owner
	String parentTask
	Integer priority
	String processDefinition
	String processInstance
	boolean suspended
	String taskDefinitionKey
	String url
	String tenantId

}
