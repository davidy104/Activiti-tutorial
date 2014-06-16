package nz.co.activiti.tutorial.model.history

enum HistoricTaskInstanceQueryParameter {
	processInstanceId,
	processDefinitionKey,
	processDefinitionKeyLike,
	processDefinitionId,
	processDefinitionName,
	processDefinitionNameLike,
	processBusinessKey,
	processBusinessKeyLike,
	executionId,
	taskName,
	taskNameLike,
	taskDescription,
	taskDescriptionLike,
	taskDefinitionKey,
	taskDeleteReason,
	taskDeleteReasonLike,
	taskAssignee,
	taskAssigneeLike,
	taskOwner,
	taskOwnerLike,
	taskInvolvedUser,
	taskPriority,
	finished,
	processFinished,
	parentTaskId,
	dueDate,
	dueDateAfter,
	dueDateBefore,
	withoutDueDate,
	taskCompletedOn,
	taskCompletedAfter,
	taskCompletedBefore,
	taskCreatedOn,
	taskCreatedBefore,
	taskCreatedAfter,
	includeTaskLocalVariables,
	includeProcessVariables,
	tenantId,
	tenantIdLike
}