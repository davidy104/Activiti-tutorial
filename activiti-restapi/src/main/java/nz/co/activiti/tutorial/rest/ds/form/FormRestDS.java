package nz.co.activiti.tutorial.rest.ds.form;

import java.util.Map;

public interface FormRestDS {

	void submitTaskForm(String taskId, Map<String, String> properties)
			throws Exception;

}
