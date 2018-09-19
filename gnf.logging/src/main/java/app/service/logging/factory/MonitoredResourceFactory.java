package app.service.logging.factory;

import java.util.Map;

import com.google.cloud.MonitoredResource;

import app.service.logging.MonitoredResourceType;
import app.service.logging.model.LogRequest;
import app.service.logging.model.MonitoredResourceModel;

/**
 * @author AdarshSinghal
 *
 */
public class MonitoredResourceFactory {

	public static MonitoredResource getInstance(LogRequest logRequest) {

		MonitoredResourceModel resource = logRequest.getMonitoredResource();
		
		if(resource==null)
			return MonitoredResource.newBuilder(MonitoredResourceType.GLOBAL.toString()).build();
		
		String type = resource.getType();
		MonitoredResource.Builder builder = MonitoredResource.newBuilder(type);

		Map<String, String> labels = resource.getLabels();
		if (labels != null && !labels.isEmpty())
			builder.setLabels(labels);

		if (type != null && !type.isEmpty())
			builder.setType(type);

		return builder.build();

	}

}
