package nz.co.activiti.tutorial.rest.processdefinition

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 
 * @author dyuan
 * candidate starters for a process-definition
 */
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["url","user","group","version","type"])
class Candidate implements Serializable{
	String url
	String user
	String group
	String type = "candidate"
}
