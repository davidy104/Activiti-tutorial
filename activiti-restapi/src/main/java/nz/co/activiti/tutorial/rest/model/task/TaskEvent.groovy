package nz.co.activiti.tutorial.rest.model.task

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","url","userId","action"])
class TaskEvent implements Serializable {

	String action
	String id
	String[] message
	String taskUrl
	//format:"yyyy-MM-dd'T'HH:mm:ss.SSSZ"
	String time
	String url
	String userId

}
