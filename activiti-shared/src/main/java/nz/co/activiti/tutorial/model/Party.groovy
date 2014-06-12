package nz.co.activiti.tutorial.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 
 * @author dyuan
 * candidate starters for a process-definition
 */
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["url","user","group","version","type"])
class Party implements Serializable{
	String url
	String user
	String group
	String type = "candidate"
}
