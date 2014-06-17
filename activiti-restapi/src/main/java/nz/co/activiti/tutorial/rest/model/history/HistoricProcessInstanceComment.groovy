package nz.co.activiti.tutorial.rest.model.history

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["id","url","author"])
class HistoricProcessInstanceComment implements Serializable {

	String id
	String url
	String message
	String author
}
