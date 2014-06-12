package nz.co.activiti.tutorial.model.deployment

import groovy.transform.ToString
@ToString(includeNames = true, includeFields=true)
class Deployments {
	Integer total

	Integer start
	Integer size
	String sort
	String order

	Set<Deployment> deploymentSet

	void addDeployment(Deployment deployment){
		if(!deploymentSet){
			deploymentSet = new HashSet<Deployment>()
		}
		deploymentSet << deployment
	}
}
