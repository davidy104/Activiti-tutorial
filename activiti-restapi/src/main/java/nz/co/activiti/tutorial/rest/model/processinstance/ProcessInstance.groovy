package nz.co.activiti.tutorial.rest.model.processinstance

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","url","businessKey","activityId","processDefinitionUrl"])
class ProcessInstance implements Serializable {

	String id
	String url
	String businessKey
	boolean suspended	
	String processDefinitionUrl
	String activityId
	String tenantId
	
	
}
