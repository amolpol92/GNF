package app.service.dlp;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import app.service.LogServiceClient;
import app.service.dlp.constants.Constants;
import app.service.dlp.model.DLPClientRequest;
import app.service.dlp.model.InspectionEntry;
import app.service.dlp.model.InspectionResultWrapper;
import app.service.dlp.model.LogRequest;

/**
 * Responsible for DLP Inspection, Risk analysis & Deidentification <br>
 * <br>
 * Service class should be 'As Simple As Possible'. If you're modifying this
 * class, add operation &amp; delegate logics to other class
 * 
 * @author AdarshSinghal
 *
 */
public class DLPService {

	/**
	 * @param inputMessage
	 * @return deidentifiedMessage
	 * @throws IOException
	 */
	public String getDeidentifiedString(String inputMessage) throws IOException {
		DLPDeIdentifier dlpDeidentifier = new DLPDeIdentifier();
		String deidentifiedString = dlpDeidentifier.getDeidentifiedString(inputMessage);
		return deidentifiedString;
	}

	/**
	 * @param inputMessage
	 * @throws IOException
	 * @throws PANDataFoundSecurityViolationException
	 */
	public InspectionResultWrapper inspect(DLPClientRequest dlpRequest) throws IOException {
		String inputMessage = dlpRequest.getMessage();
		InspectionResultWrapper inspectionResults = getInspectionResult(inputMessage);

		boolean hasSensitiveData = inspectionResults.getInspectResults().stream()
				.anyMatch(result -> hasSensitiveData(result));

		if (hasSensitiveData) {
			inspectionResults.setSensitiveDataFlag(true);
		} else {

			String message = "Performed DLP Inspection on input message. Sensitive data not present.";
			logMessage(dlpRequest, message, "INFO");
		}

		return inspectionResults;

	}

	private void logMessage(DLPClientRequest dlpRequest, String message, String severity)
			throws ClientProtocolException, IOException {
		LogRequest logRequest = new LogRequest(message, severity);
		Map<String, String> labels = new HashMap<>();
		labels.put(Constants.GB_TXN_ID_KEY, dlpRequest.getGlobalTxnId());
		labels.put("Source Authorization Level", String.valueOf(dlpRequest.getSourceAuth()));
		labels.put("Target Group Id", dlpRequest.getGroupId());
		logRequest.setLabels(labels);
		LogServiceClient.getLogger().log(logRequest);
	}

	/**
	 * @param inputMessage
	 * @return inspectResult
	 * @throws IOException
	 */
	private InspectionResultWrapper getInspectionResult(String inputMessage) throws IOException {
		DLPInspector dlpInspector = new DLPInspector();
		List<InspectionEntry> inspectionResult = dlpInspector.getInspectionResult(inputMessage);
		return new InspectionResultWrapper(inspectionResult);
	}

	private boolean hasSensitiveData(InspectionEntry inspectResult) {
		List<String> sensitiveInfoTypes = Arrays.asList("CREDIT_CARD_NUMBER", "INDIA_PAN_INDIVIDUAL");
		return sensitiveInfoTypes.contains(inspectResult.getInfoType());
	}

}
