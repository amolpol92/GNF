package app.service.logging.factory;

import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Payload.StringPayload;
import com.google.cloud.logging.Severity;
import com.google.cloud.logging.SourceLocation;

import app.service.logging.model.LogRequest;

public class LogEntryFactory {

	public static LogEntry getInstance(LogRequest logRequest) {

		StringPayload messagePayload = getMessagePayload(logRequest);

		LogEntry.Builder builder = LogEntry.newBuilder(messagePayload);

		setSeverity(logRequest, builder);
		setLogName(logRequest, builder);
		setMonitoredResource(logRequest, builder);
		setLabels(logRequest, builder);
		setSourceLocation(logRequest, builder);

		return builder.build();

	}

	private static StringPayload getMessagePayload(LogRequest logRequest) {
		String messageString = logRequest.getMessage();
		if (messageString == null)
			messageString = "";
		StringPayload message = StringPayload.of(messageString);
		return message;
	}

	private static void setLabels(LogRequest logRequest, LogEntry.Builder builder) {
		if (logRequest.getLabels() != null && !logRequest.getLabels().isEmpty())
			builder.setLabels(logRequest.getLabels());
	}

	private static void setMonitoredResource(LogRequest logRequest, LogEntry.Builder builder) {
		MonitoredResource monitoredResource = MonitoredResource.newBuilder(logRequest.getMonitoredResource()).build();
		builder.setResource(monitoredResource);
	}

	private static void setSourceLocation(LogRequest logRequest, LogEntry.Builder builder) {
		SourceLocation sourceLocation = SourceLocationFactory.getInstance(logRequest);
		if (sourceLocation != null)
			builder.setSourceLocation(sourceLocation);
	}

	private static void setLogName(LogRequest logRequest, LogEntry.Builder builder) {
		builder.setLogName("Default");
		if (logRequest.getLogName() != null && !logRequest.getLogName().isEmpty())
			builder.setLogName(logRequest.getLogName());
	}

	private static void setSeverity(LogRequest logRequest, LogEntry.Builder builder) {
		if (logRequest.getSeverity() != null && !logRequest.getSeverity().isEmpty()) {
			Severity severity;
			try {
				severity = Severity.valueOf(logRequest.getSeverity());
			} catch (IllegalArgumentException e) {
				severity = Severity.DEFAULT;
			}
			builder.setSeverity(severity);
		}
	}

}
