package nz.co.activiti.tutorial.taskprocess.ds

import groovy.util.logging.Slf4j
import nz.co.activiti.tutorial.taskprocess.model.LaptopModel
import nz.co.activiti.tutorial.taskprocess.model.OrderModel

import org.springframework.stereotype.Component

@Component("orderCalculator")
@Slf4j
class OrderCalculator {
	void calculate(OrderModel order){
		log.info "calculate start:{} $order"
		Set<LaptopModel> laptops = order.laptopModelSet;
		int totalitems =laptops.size()
		BigDecimal totalprice = BigDecimal.ZERO
		if(laptops){
			laptops.each {laptop->
				totalprice = totalprice.plus(laptop.price * laptop.qty)
			}
		}

		order.totalPrice = totalprice
		order.totalItems=totalitems
		log.info "calculate end:{} $order"
	}
}
