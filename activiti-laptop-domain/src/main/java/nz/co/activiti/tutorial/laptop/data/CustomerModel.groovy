package nz.co.activiti.tutorial.laptop.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["customerEmail"])
class CustomerModel implements Serializable{
	String customerName
	String customerEmail
}
