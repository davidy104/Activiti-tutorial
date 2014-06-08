package nz.co.activiti.tutorial.rest.deployment;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author david { "id": "10", "name": "activiti-examples.bar",
 *         "deploymentTime": "2010-10-13T14:54:26.750+02:00", "category":
 *         "examples", "url":
 *         "http://localhost:8081/service/repository/deployments/10", "tenantId"
 *         : null }
 */
@SuppressWarnings("serial")
public class DeploymentResponse implements Serializable {

	private String id;
	private String name;
	private String deploymentTime;
	private String category;
	private String url;
	private String tenantId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeploymentTime() {
		return deploymentTime;
	}

	public void setDeploymentTime(String deploymentTime) {
		this.deploymentTime = deploymentTime;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder.append(this.id, ((DeploymentResponse) obj).id)
				.append(this.category, ((DeploymentResponse) obj).category)
				.append(this.name, ((DeploymentResponse) obj).name).isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.id).append(this.category).append(this.name)
				.toHashCode();
	}

}
