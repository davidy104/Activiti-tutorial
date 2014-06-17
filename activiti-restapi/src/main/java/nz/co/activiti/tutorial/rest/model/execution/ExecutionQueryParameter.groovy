package nz.co.activiti.tutorial.rest.model.execution

enum ExecutionQueryParameter {
	id,processDefinitionKey,processDefinitionId,processInstanceId,messageEventSubscriptionName,signalEventSubscriptionName,parentId,
	tenantId,tenantIdLike,withoutTenantId,sort
}
