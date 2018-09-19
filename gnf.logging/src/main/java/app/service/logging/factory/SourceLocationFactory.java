package app.service.logging.factory;

import com.google.cloud.logging.SourceLocation;

import app.service.logging.model.LogRequest;
import app.service.logging.model.SourceLocationModel;

/**
 * @author AdarshSinghal
 *
 */
public class SourceLocationFactory {

	public static SourceLocation getInstance(LogRequest logRequest) {
		SourceLocationModel srcLoc = logRequest.getSourceLocation();

		if (srcLoc == null || srcLoc.getFileName()==null || srcLoc.getFileName().isEmpty()) {
			return null;
		}

		SourceLocation.Builder builder = SourceLocation.newBuilder();
		builder.setFile(srcLoc.getFileName());			

		long lineNumber = srcLoc.getLineNumber();
		if (lineNumber < 1)
			builder.setLine(lineNumber);

		String function = srcLoc.getFunction();
		if (function != null && !function.isEmpty())
			builder.setFunction(function);

		return builder.build();
	}

}
