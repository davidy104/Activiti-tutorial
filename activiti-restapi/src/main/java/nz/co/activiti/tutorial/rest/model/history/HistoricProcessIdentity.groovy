package nz.co.activiti.tutorial.rest.model.history

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import nz.co.activiti.tutorial.rest.model.IdentityType

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["processInstanceId","userId","groupId","taskId"])
class HistoricProcessIdentity  implements Serializable {
	String userId
	String groupId
	IdentityType type

	String taskId
	String taskUrl
	String processInstanceId
	String processInstanceUrl
}
