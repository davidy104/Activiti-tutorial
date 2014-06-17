package nz.co.activiti.tutorial.rest.model.task

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","url","name","type","taskUrl","processInstanceUrl","externalUrl"])
class TaskAttachment implements Serializable {

	String id
	String url
	String name
	String description
	//Type of attachment, optional. Supports any arbitrary string or a valid HTTP content-type.
	String type
	String taskUrl
	String processInstanceUrl
	String externalUrl
	String contentUrl
}
