package nz.co.activiti.tutorial.rest.model.task


enum TaskQueryParameter {
	name,
	nameLike,
	description,
	priority,
	minimumPriority,
	maximumPriority,
	assignee,
	assigneeLike,

	owner,
	ownerLike,

	unassigned,
	delegationState,

	candidateUser,
	candidateGroup,

	involvedUser,
	taskDefinitionKey,
	taskDefinitionKeyLike,

	processInstanceId,
	processInstanceBusinessKey,
	processInstanceBusinessKeyLike,

	processDefinitionKey,
	processDefinitionKeyLike,
	processDefinitionName,
	processDefinitionNameLike,
	executionId,

	createdOn,
	createdBefore,
	createdAfter,
	dueOn,
	dueBefore,
	dueAfter,

	withoutDue,
	excludeSubTasks,
	active,
	includeTaskLocalVariables,
	includeProcessVariables,
	tenantId,
	tenantIdLike,
	withoutTenantId,
}
