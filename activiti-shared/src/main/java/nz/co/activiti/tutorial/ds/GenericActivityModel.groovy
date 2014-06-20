package nz.co.activiti.tutorial.ds;

import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
@EqualsAndHashCode(includes=["activityId","name","type"])
public class GenericActivityModel implements Serializable {
	String activityId
	String name
	String type
	Date startTime
	Date endTime
	Map<String, GenericActivityModel> outgoingActivityMap
	Map<String, GenericActivityModel> incomeActivityMap

	static Builder getBuilder(String activityId, String name, String type) {
		return new Builder(activityId, name, type)
	}

	static class Builder {
		GenericActivityModel built
		Builder(String activityId, String name, String type) {
			built = new GenericActivityModel()
			built.activityId = activityId
			built.name = name
			built.type = type
		}

		GenericActivityModel build() {
			return built
		}
	}

	void addOutgoingActivity(String transition, GenericActivityModel outgoingActivity){
		if(!outgoingActivityMap){
			outgoingActivityMap = [:]
		}
		outgoingActivityMap.put(transition,outgoingActivity)
	}

	void addIncomingActivity(String transition, GenericActivityModel incomingActivity){
		if(!incomeActivityMap){
			incomeActivityMap = [:]
		}
		incomeActivityMap.put(transition,incomingActivity)
	}
}
