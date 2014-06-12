package nz.co.activiti.tutorial.model.processinstance


enum ProcessInstanceQueryParameters {
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
