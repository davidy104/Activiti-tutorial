package nz.co.activiti.tutorial.rest.model.processinstance


enum ProcessInstanceQueryParameter {
	id,
	processDefinitionKey,
	processDefinitionId,
	businessKey,
	involvedUser,
	suspended,
	superProcessInstanceId,
	subProcessInstanceId,
	excludeSubprocesses,
	includeProcessVariables,
	sort,
	tenantId,
	tenantIdLike,
	withoutTenantId,
}
