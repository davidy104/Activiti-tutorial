package nz.co.activiti.tutorial.rest.model.deployment

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","name","deploymentTime","category","url","tenantId"])
class Deployment implements Serializable {
	String id
	String name
	String deploymentTime
	String category
	String url
	String tenantId
}
