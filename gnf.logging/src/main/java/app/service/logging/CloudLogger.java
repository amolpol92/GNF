package app.service.logging;

import java.util.Collections;

import com.google.api.client.util.Strings;
import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.Payload.StringPayload;
import com.google.cloud.logging.Severity;

import app.service.logging.model.LogRequest;

/**
 * This class is responsible for writing logs into Stackdriver.
 * 
 * @author AdarshSinghal
 *
 */
public class CloudLogger {

	private static final CloudLogger LOGGER = new CloudLogger();

	public static CloudLogger getLogger() {
		return LOGGER;
	}

	private CloudLogger() {
	}

	/**
	 * @param message
	 */
	public void info(String message) {
		log(message, Severity.INFO);
	}

	/**
	 * @param message
	 */
	public void warning(String message) {
		log(message, Severity.WARNING);
	}

	/**
	 * @param message
	 */
	public void error(String message) {
		log(message, Severity.ERROR);
	}

	/**
	 * @param message
	 * @param severity
	 */
	public void log(String message, Severity severity) {
		String monitoredResourceType = MonitoredResourceType.GLOBAL.toString();
		String logName = "GNFAppLogger";

		log(message, severity, monitoredResourceType, logName);
	}

	/**
	 * @param logRequest
	 */
	public void log(LogRequest logRequest) {
		Severity severity = Severity.valueOf(logRequest.getSeverity());
		log(logRequest.getMessage(), severity, logRequest.getMonitoredResource(), logRequest.getLogName());
	}

	/**
	 * @param message
	 * @param severity
	 * @param monitoredResourceType
	 * @param logName
	 */
	public void log(String message, Severity severity, String monitoredResourceType, String logName) {

		if (severity == null) {
			severity = Severity.INFO;
		}

		if (Strings.isNullOrEmpty(monitoredResourceType)) {
			monitoredResourceType = MonitoredResourceType.GLOBAL.toString();
		}

		if (Strings.isNullOrEmpty(logName)) {
			logName = "GNFAppLogger";
		}

		try (Logging logging = LoggingOptions.getDefaultInstance().getService();) {
			MonitoredResource monitoredResource = MonitoredResource.newBuilder(monitoredResourceType).build();
			LogEntry entry = LogEntry.newBuilder(StringPayload.of(message)).setSeverity(severity).setLogName(logName)
					.setResource(monitoredResource).build();

			logging.write(Collections.singleton(entry));
			logging.flush();
		} catch (Exception e) {
			// Do nothing
		}

	}

}
