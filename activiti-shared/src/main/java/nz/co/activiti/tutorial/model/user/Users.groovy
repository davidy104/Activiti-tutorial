package nz.co.activiti.tutorial.model.user

import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
class Users implements Serializable {
	Integer total

	Integer start
	Integer size
	String sort
	String order

	List<User> userList

	void addUser(User user){
		if(!userList){
			userList = new ArrayList<User>()
		}
		userList << user
	}
}
