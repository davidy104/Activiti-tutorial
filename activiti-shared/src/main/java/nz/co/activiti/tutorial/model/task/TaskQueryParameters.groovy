package nz.co.activiti.tutorial.model.task

import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
class TaskQueryParameters implements Serializable{

	String name
	String nameLike
	String description
	Integer priority
	Integer minimumPriority
	Integer maximumPriority
	String assignee
	String assigneeLike

	String owner
	String ownerLike

	boolean unassigned
	String delegationState

	String candidateUser
	String candidateGroup

	String involvedUser
	String taskDefinitionKey
	String taskDefinitionKeyLike

	String processInstanceId
	String processInstanceBusinessKey
	String processInstanceBusinessKeyLike

	String processDefinitionKey
	String processDefinitionKeyLike
	String processDefinitionName

	String processDefinitionNameLike
	String executionId

	Date createdOn
	Date createdBefore
	Date createdAfter
	Date dueOn
	Date dueBefore
	Date dueAfter

	boolean withoutDueDate
	boolean excludeSubTasks
	boolean active
	boolean includeTaskLocalVariables
	boolean includeProcessVariables
	String tenantId
	String tenantIdLike
	boolean withoutTenantId
}
