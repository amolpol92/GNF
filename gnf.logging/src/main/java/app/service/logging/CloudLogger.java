package app.service.logging;

import java.util.Collections;

import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import com.google.cloud.logging.Payload.StringPayload;
import com.google.cloud.logging.Severity;

import app.service.logging.factory.LogEntryFactory;
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
	 * @param logRequest
	 * @throws Exception
	 */
	public void log(LogRequest logRequest) throws Exception {
		try (Logging logging = LoggingOptions.getDefaultInstance().getService();) {

			LogEntry entry = LogEntryFactory.getInstance(logRequest);

			logging.write(Collections.singleton(entry));
			logging.flush();
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * @param message
	 * @param severity
	 * @param monitoredResourceType
	 * @param logName
	 */
	public void log(String message, Severity severity, String monitoredResourceType, String logName) {

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

	public static void main(String[] args) {

		try (Logging logging = LoggingOptions.getDefaultInstance().getService();) {
			MonitoredResource monitoredResource = MonitoredResource.newBuilder(MonitoredResourceType.GLOBAL.toString())
					.build();
			LogEntry entry = LogEntry.newBuilder(StringPayload.of("Sample text")).setSeverity(Severity.INFO)
					.setLogName("GNFAppLogger").setResource(monitoredResource).setSourceLocation(null).build();

			logging.write(Collections.singleton(entry));
			logging.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
