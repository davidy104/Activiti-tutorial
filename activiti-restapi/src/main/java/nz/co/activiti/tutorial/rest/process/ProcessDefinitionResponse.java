/* Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nz.co.activiti.tutorial.rest.process;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 
 * @author david
 * 
 *         respStr:{ "id":"createTimersProcess:1:31", "url":
 *         "http://localhost:8383/activiti-rest/service/repository/process-definitions/createTimersProcess%3A1%3A31"
 *         , "key":"createTimersProcess", "version":1,
 *         "name":"Create timers process",
 *         "description":"Test process to create a number of timers.",
 *         "deploymentId":"20","deploymentUrl":
 *         "http://localhost:8383/activiti-rest/service/repository/deployments/20"
 *         , "resource":
 *         "http://localhost:8383/activiti-rest/service/repository/deployments/20/resources/createTimersProcess.bpmn20.xml"
 *         , "diagramResource":null, "category":"Examples",
 *         "graphicalNotationDefined":false, "suspended":false,
 *         "startFormDefined":false}
 */

@SuppressWarnings("serial")
public class ProcessDefinitionResponse implements Serializable {

	private String id;
	private String url;
	private String key;
	private int version;
	private String name;
	private String description;

	private String deploymentId;
	private String deploymentUrl;
	private String resource;

	private String diagramResource;
	private String category;
	private boolean graphicalNotationDefined;
	private boolean suspended;
	private boolean startFormDefined;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDeploymentId() {
		return deploymentId;
	}

	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}

	public String getDeploymentUrl() {
		return deploymentUrl;
	}

	public void setDeploymentUrl(String deploymentUrl) {
		this.deploymentUrl = deploymentUrl;
	}

	public String getResource() {
		return resource;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getDiagramResource() {
		return diagramResource;
	}

	public void setDiagramResource(String diagramResource) {
		this.diagramResource = diagramResource;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isGraphicalNotationDefined() {
		return graphicalNotationDefined;
	}

	public void setGraphicalNotationDefined(boolean graphicalNotationDefined) {
		this.graphicalNotationDefined = graphicalNotationDefined;
	}

	public boolean isSuspended() {
		return suspended;
	}

	public void setSuspended(boolean suspended) {
		this.suspended = suspended;
	}

	public boolean isStartFormDefined() {
		return startFormDefined;
	}

	public void setStartFormDefined(boolean startFormDefined) {
		this.startFormDefined = startFormDefined;
	}

	@Override
	public boolean equals(Object obj) {
		EqualsBuilder builder = new EqualsBuilder();
		return builder
				.append(this.id, ((ProcessDefinitionResponse) obj).id)
				.append(this.key, ((ProcessDefinitionResponse) obj).key)
				.append(this.name, ((ProcessDefinitionResponse) obj).name)
				.append(this.version, ((ProcessDefinitionResponse) obj).version)
				.isEquals();
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		return builder.append(this.id).append(this.key).append(this.name)
				.append(this.version).toHashCode();
	}
}
