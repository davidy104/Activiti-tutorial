package nz.co.activiti.tutorial.model.processinstance


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
