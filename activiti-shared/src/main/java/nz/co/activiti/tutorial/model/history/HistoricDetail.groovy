package nz.co.activiti.tutorial.model.history

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","processInstanceId","executionId"])
class HistoricDetail {
	String id
	String processInstanceId

	String executionId
	String activityInstanceId
	String taskId
	boolean selectOnlyFormProperties
	boolean selectOnlyVariableUpdates
}
