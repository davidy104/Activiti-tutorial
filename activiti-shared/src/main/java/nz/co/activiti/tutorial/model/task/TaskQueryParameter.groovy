package nz.co.activiti.tutorial.model.task


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

	candiUser,
	candiGroup,

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
