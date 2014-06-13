package nz.co.activiti.tutorial.model.group

import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
class MemberShip implements Serializable{
	String userId
	String groupId
	String url
}
