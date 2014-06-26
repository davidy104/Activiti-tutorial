package nz.co.activiti.tutorial.rest.ds.form;

import java.util.Map;

import javax.annotation.Resource;
import javax.ws.rs.core.MediaType;

import nz.co.activiti.tutorial.rest.ActivitiRestClientAccessor;
import nz.co.activiti.tutorial.rest.GenericActivitiRestException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.sun.jersey.api.client.WebResource;

@Service
public class FormRestDSImpl extends ActivitiRestClientAccessor implements
		FormRestDS {
	private static final Logger LOGGER = LoggerFactory
			.getLogger(FormRestDSImpl.class);

	@Resource
	private FormJSONConverter formJSONConverter;

	// form/form-data,post
	/**
	 * { "taskId" : "5", "properties" : [ { "id" : "room", "value" : "normal" }
	 * ] }
	 */
	@Override
	public void submitTaskForm(String taskId, Map<String, String> properties)
			throws Exception {
		LOGGER.info("submitTaskForm start:{} ", taskId);
		String jsonEntity = formJSONConverter.toSubmitTaskForm(taskId,
				properties);

		LOGGER.info("jsonEntity:{} ", jsonEntity);
		WebResource webResource = client.resource(baseUrl).path(
				"/form/form-data");

		ClientResponse response = webResource
				.accept(MediaType.APPLICATION_JSON)
				.type(MediaType.APPLICATION_JSON)
				.post(ClientResponse.class, jsonEntity);

		Status statusCode = response.getClientResponseStatus();
		LOGGER.info("statusCode:{} ", statusCode);
		String respStr = getResponsePayload(response);
		LOGGER.info("respStr:{} ", respStr);

		if (statusCode == ClientResponse.Status.BAD_REQUEST) {
			throw new GenericActivitiRestException(respStr);
		} else if (statusCode != ClientResponse.Status.OK) {
			throw new Exception("Unknow exception:{} " + respStr);
		}
		LOGGER.info("submitTaskForm end:{} ");
	}

}
