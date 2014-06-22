package nz.co.activiti.tutorial.taskprocess;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import nz.co.activiti.tutorial.taskprocess.model.LaptopModel;
import nz.co.activiti.tutorial.taskprocess.model.OrderModel;

public class TestUtils {

	public static OrderModel initOrder() {
		OrderModel order = new OrderModel();
		String orderNo = UUID.randomUUID().toString();
		order.setOrderNo(orderNo);

		LaptopModel laptop = new LaptopModel();
		laptop.setLaptopName("MAC");
		laptop.setModeNo("AIR");
		laptop.setQty(2);
		laptop.setPrice(new BigDecimal(1200.00));

		order.addLaptopOrder(laptop);

		laptop = new LaptopModel();
		laptop.setLaptopName("DELL");
		laptop.setModeNo("LATITUDE");
		laptop.setQty(3);
		laptop.setPrice(new BigDecimal(920.00));

		order.addLaptopOrder(laptop);
		order.setTotalItems(2);

		return order;
	}

	public static void submitOrderInfo(OrderModel order) {
		order.getCustomer().setCustomerEmail("david.yuan@propellerhead.co.nz");
		order.getCustomer().setCustomerName("david");
		order.setOrderTime(new Date());
		order.setAddress("20 opal ave");

	}
}
