package app.service.authorization.model;

import java.util.Map;

/**
 * @author AdarshSinghal
 *
 */
public class LogRequest {

	private String message;
	private String severity;
	private String monitoredResource;
	private String logName;
	private Map<String, String> labels;
	private SourceLocationModel sourceLocation;

	public LogRequest() {
	}

	public LogRequest(String messsage, String severity, String monitoredResource, String logName) {
		super();
		this.message = messsage;
		this.severity = severity;
		this.monitoredResource = monitoredResource;
		this.logName = logName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String messsage) {
		this.message = messsage;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getMonitoredResource() {
		return monitoredResource;
	}

	public void setMonitoredResource(String monitoredResource) {
		this.monitoredResource = monitoredResource;
	}

	public String getLogName() {
		return logName;
	}

	public void setLogName(String logName) {
		this.logName = logName;
	}

	public Map<String, String> getLabels() {
		return labels;
	}

	public void setLabels(Map<String, String> labels) {
		this.labels = labels;
	}

	public SourceLocationModel getSourceLocation() {
		return sourceLocation;
	}

	public void setSourceLocation(SourceLocationModel sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	@Override
	public String toString() {
		return "LogRequest [message=" + message + ", severity=" + severity + ", monitoredResource=" + monitoredResource
				+ ", logName=" + logName + ", labels=" + labels + ", sourceLocation=" + sourceLocation + "]";
	}

}
