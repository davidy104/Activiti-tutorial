package nz.co.activiti.tutorial.model.group

import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
class Groups implements Serializable {
	Integer total

	Integer start
	Integer size
	String sort
	String order

	Set<Group> groupSet

	void addGroup(Group group){
		if(!groupSet){
			groupSet = new HashSet<Group>()
		}
		groupSet << group
	}
}
