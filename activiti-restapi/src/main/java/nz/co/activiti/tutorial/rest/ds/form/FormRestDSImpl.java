package nz.co.activiti.tutorial.rest.ds.form;

import java.util.Map;

import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class FormRestDSImpl extends ActivitiRestClientAccessor implements
		FormRestDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FormRestDSImpl.class);

	// form/form-data,post
	/**
	 * { "taskId" : "5", "properties" : [ { "id" : "room", "value" : "normal" }
	 * ] }
	 */
	@Override
	public void submitTaskForm(String taskId, Map<String, String> properties)
			throws Exception {

	}

}
