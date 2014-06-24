package nz.co.activiti.tutorial.laptop.data

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["orderNo"])
class OrderModel implements Serializable {
	String orderNo
	Set<LaptopModel> laptopModelSet
	long totalItems = 0
	BigDecimal totalPrice = BigDecimal.ZERO

	@Delegate
	CustomerModel customer = new CustomerModel()
	String address

	//dataEntry,pendingOnBilling,pendingOnApproval,payed,rejected,delivered
	String status = "dataEntry"
	Date orderTime


	void addLaptopOrder(LaptopModel laptopModel){
		if(!laptopModelSet){
			laptopModelSet = new HashSet<LaptopModel>()
		}
		laptopModelSet << laptopModel
	}
}
