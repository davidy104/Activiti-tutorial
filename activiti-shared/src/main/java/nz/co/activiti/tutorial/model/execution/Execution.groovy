package nz.co.activiti.tutorial.model.execution

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","url","parentId","processInstanceId","tenantId"])
class Execution implements Serializable {

	String id
	String url
	String parentId
	String parentUrl
	String processInstanceId
	String processInstanceUrl
	boolean suspended
	String activityId
	String tenantId
}
