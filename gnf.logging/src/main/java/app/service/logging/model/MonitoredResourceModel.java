package app.service.logging.model;

import java.util.Map;

/**
 * @author AdarshSinghal
 *
 */
public class MonitoredResourceModel {

	private String type;
	private Map<String, String> labels;

	public String getType() {
		return type;
	}

	public MonitoredResourceModel() {

	}

	public MonitoredResourceModel(String type) {
		this.type = type;
	}

	public MonitoredResourceModel(String type, Map<String, String> labels) {
		this.type = type;
		this.labels = labels;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	@Override
	public String toString() {
		return "MonitoredResourceModel [type=" + type + ", labels=" + labels + "]";
	}

}
