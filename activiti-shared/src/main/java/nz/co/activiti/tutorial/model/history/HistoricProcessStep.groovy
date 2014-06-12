package nz.co.activiti.tutorial.model.history

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","name","processDefinition","processInstance","taskDefinitionKey","url"])
class HistoricProcessStep implements Serializable{
	String id
	String businessKey
	String processDefinitionId
	String processDefinitionUrl
	
	//format:"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
	String startTime
	String endTime
	Long durationInMillis
	String startUserId
	String startActivityId
	String endActivityId
	String deleteReason
	String superProcessInstanceId
	String url
	String tenantId
	
	
}
