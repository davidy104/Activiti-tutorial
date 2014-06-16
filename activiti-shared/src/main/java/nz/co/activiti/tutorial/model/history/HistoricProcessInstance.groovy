package nz.co.activiti.tutorial.model.history

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import nz.co.activiti.tutorial.model.Variable
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","name","processDefinition","processInstance","taskDefinitionKey","url"])
class HistoricProcessInstance implements Serializable{
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
	List<Variable> variables
	String url
	String tenantId


}
