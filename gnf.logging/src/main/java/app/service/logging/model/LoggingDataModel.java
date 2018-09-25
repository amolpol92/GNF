package app.service.logging.model;

/**
 * @author adarshsinghal
 *
 */
public class LoggingDataModel {

	private String insertId;
	private String globalTxnId;
	private String message;
	private String severity;
	private String monitoredResource;
	private String loggerName;

	public LoggingDataModel() {
	}

	public String getInsertId() {
		return insertId;
	}

	public void setInsertId(String insertId) {
		this.insertId = insertId;
	}

	public String getGlobalTxnId() {
		return globalTxnId;
	}

	public void setGlobalTxnId(String globalTxnId) {
		this.globalTxnId = globalTxnId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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

	public String getLoggerName() {
		return loggerName;
	}

	public void setLoggerName(String loggerName) {
		this.loggerName = loggerName;
	}

	@Override
	public String toString() {
		return "LoggingDataModel [insertId=" + insertId + ", globalTxnId=" + globalTxnId + ", message=" + message
				+ ", severity=" + severity + ", monitoredResource=" + monitoredResource + ", loggerName=" + loggerName
				+ "]";
	}

}
