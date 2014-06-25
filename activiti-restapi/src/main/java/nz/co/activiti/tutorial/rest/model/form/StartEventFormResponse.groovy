package nz.co.activiti.tutorial.rest.model.form

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["url","id","businessKey","processDefinitionId","activityId"])
class StartEventFormResponse implements Serializable{

	String id
	String url
	String businessKey
	boolean suspended
	String processDefinitionId
	String processDefinitionUrl
	String activityId
}
