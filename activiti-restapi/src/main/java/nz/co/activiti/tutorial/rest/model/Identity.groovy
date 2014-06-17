package nz.co.activiti.tutorial.rest.model

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * 
 * @author dyuan
 * candidate starters for a process-definition
 */
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["url","user","group","type"])
class Identity implements Serializable{
	String url
	String user
	String group
	IdentityType type = IdentityType.candidate
}
