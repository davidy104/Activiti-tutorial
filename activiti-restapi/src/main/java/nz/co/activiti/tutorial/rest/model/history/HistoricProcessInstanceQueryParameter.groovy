package nz.co.activiti.tutorial.rest.model.history

enum HistoricProcessInstanceQueryParameter {
	processInstanceId,processDefinitionKey,processDefinitionId,businessKey,involvedUser,finished,
	superProcessInstanceId,excludeSubprocesses,finishedAfter,finishedBefore,startedAfter,
	startedBefore,startedBy,includeProcessVariables,tenantId,tenantIdLike,withoutTenantId
}
