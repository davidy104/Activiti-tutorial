package nz.co.activiti.tutorial.rest

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["orderId","orderNo"])
class OrderDetails {

	Long orderId
	String orderNo
	Date orderTime
	String shipAddress
}
