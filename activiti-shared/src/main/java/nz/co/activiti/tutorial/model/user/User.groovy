package nz.co.activiti.tutorial.model.user

import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
class User implements Serializable {

	String id
	String firstName
	String lastName
	String url
	String email
	String password
}
