package nz.co.activiti.tutorial.laptop.component;

import java.math.BigDecimal;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class Calculator implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		System.out.println("Calculator start:{}");
		String customerName = (String) execution.getVariable("customerName");
		System.out.println("customerName:{}" + customerName);
		execution.setVariable("totalPrice", new BigDecimal(5000.00));
		System.out.println("Calculator end:{}");
	}

}
