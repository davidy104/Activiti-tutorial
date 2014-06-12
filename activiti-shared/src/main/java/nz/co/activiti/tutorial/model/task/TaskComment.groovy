package nz.co.activiti.tutorial.model.task

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","url","author","message"])
class TaskComment implements Serializable {

	String id
	String url
	String message
	String author
}
