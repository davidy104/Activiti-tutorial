package nz.co.activiti.tutorial.rest.model.form

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["formKey","deploymentId","processDefinitionId","taskId"])
class Form implements Serializable{

	String formKey
	String deploymentId
	String processDefinitionId
	String processDefinitionUrl
	String taskId
	String taskUrl

	Map<String,Object> formProperties
}
