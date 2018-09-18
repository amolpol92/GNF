package app.service.dlp;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import app.service.dlp.logger.CloudLogger;
import app.service.dlp.model.InspectionEntry;
import app.service.dlp.model.InspectionResultWrapper;

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

	private CloudLogger LOGGER = CloudLogger.getLogger();

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
	public InspectionResultWrapper inspect(String inputMessage) throws IOException {
		InspectionResultWrapper inspectionResults = getInspectionResult(inputMessage);

		boolean hasSensitiveData = inspectionResults.getInspectResults().stream()
				.anyMatch(result -> hasSensitiveData(result));

		if (hasSensitiveData) {
			LOGGER.warning(
					"Inside DLP Service. Throwing PANDataFoundSecurityViolationException. Reason:- PAN data present. "
							+ "Redacted Message:- " + getDeidentifiedString(inputMessage));
			inspectionResults.setSensitiveDataFlag(true);
		} else {
			LOGGER.info(
					"Inside DLP Service. Performed DLP Inspection on input message. Sensitive data not present. PIIs found? \n"
							+ !inspectionResults.getInspectResults().isEmpty());
		}

		return inspectionResults;

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
