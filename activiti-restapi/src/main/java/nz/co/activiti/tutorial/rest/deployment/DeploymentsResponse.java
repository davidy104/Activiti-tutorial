package nz.co.activiti.tutorial.rest.deployment;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.lang3.builder.ToStringBuilder;

@SuppressWarnings("serial")
public class DeploymentsResponse implements Serializable {

	private List<DeploymentResponse> data;

	private Integer total;
	private Integer start;
	private String sort;
	private String order;
	private Integer size;

	public List<DeploymentResponse> getData() {
		return data;
	}

	public void setData(List<DeploymentResponse> data) {
		this.data = data;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getStart() {
		return start;
	}

	public void setStart(Integer start) {
		this.start = start;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
