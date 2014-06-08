package nz.co.activiti.tutorial.traningprocess;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TrainingRequestProcessTestUtils {

	public static Map<String, Object> getRequestVariables() {
		Date date = new Date();
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("customerName", "David");
		variables.put("customerEamil", "david.yuan124@gmail.com");
		variables.put("trainingTopic", "Activiti");
		variables.put("trainingDate", date);

		return variables;
	}
}
