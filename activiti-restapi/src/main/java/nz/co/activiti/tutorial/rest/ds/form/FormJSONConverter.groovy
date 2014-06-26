package nz.co.activiti.tutorial.rest.ds.form

import java.util.Map;

import groovy.json.JsonBuilder;
import groovy.util.logging.Slf4j
import nz.co.activiti.tutorial.ConvertException
import nz.co.activiti.tutorial.rest.model.form.Form

import org.springframework.stereotype.Component

@Component
@Slf4j
class FormJSONConverter {

	Form toForm(String jsonText)throws ConvertException{
	}

	String toSubmitTaskForm(String taskIdParam, Map propertiesMap)throws ConvertException{
		def json = new JsonBuilder()

		json{
			taskId taskIdParam
			if(propertiesMap){
				properties(
						propertiesMap.collect {key, val ->
							[id:"${key}",value:"${val}"]
						}
						)
			}
		}
		return json.toString();
	}
}
