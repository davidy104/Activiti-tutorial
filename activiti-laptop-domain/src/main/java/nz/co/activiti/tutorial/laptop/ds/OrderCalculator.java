package nz.co.activiti.tutorial.laptop.ds;

import java.math.BigDecimal;
import java.util.Set;

import nz.co.activiti.tutorial.laptop.data.LaptopModel;
import nz.co.activiti.tutorial.laptop.data.OrderModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("orderCalculator")
public class OrderCalculator {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(OrderCalculator.class);

	public void calculate(OrderModel order) throws Exception {
		LOGGER.info("calculate start:{}", order);

		Set<LaptopModel> laptops = order.getLaptopModelSet();
		if (laptops != null) {
			BigDecimal totalprice = BigDecimal.ZERO;
			for (LaptopModel laptop : laptops) {
				BigDecimal totalPricePerPizza = laptop.getPrice().multiply(
						new BigDecimal(laptop.getQty()));
				totalprice = totalprice.add(totalPricePerPizza);
			}
			order.setTotalItems(laptops.size());
			order.setTotalPrice(totalprice);
		}

		LOGGER.info("calculate end:{}", order);
	}
}
