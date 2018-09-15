package app.service.dlp;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import app.constants.ConstantsURL;
import app.exception.PANDataFoundSecurityViolationException;
import app.logging.CloudLogger;
import app.model.InspectResult;
import app.model.MessageWrapper;

/**
 * Responsible for DLP Inspection, Risk analysis & Deidentification <br>
 * <br>
 * Service class should be 'As Simple As Possible'. If you're modifying this
 * class, add operation &amp; delegate logics to other class
 * 
 * @author AdarshSinghal
 *
 */
public class DLPServiceInvoker {

	private CloudLogger LOGGER = CloudLogger.getLogger();

	/**
	 * @param inputMessage
	 * @return inspectResult
	 * @throws IOException
	 */
	public List<InspectResult> getInspectionResult(String inputMessage) throws IOException {
		LOGGER.info("Inside DLP Service. Performing DLP Inspection for message - " + inputMessage);
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(ConstantsURL.DLP_INSPECT_URL);

		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(new MessageWrapper(inputMessage));
		StringEntity entity = new StringEntity(requestJson);
		httppost.setEntity(entity);

		HttpResponse response = httpclient.execute(httppost);
		System.out.println(response.getStatusLine().getStatusCode());

		HttpEntity httpEntity = response.getEntity();
		if (httpEntity == null || httpEntity.getContent() == null) {
			return Collections.emptyList();
		}
		List<InspectResult> inspectionResult = mapper.readValue(httpEntity.getContent(),
				new TypeReference<List<InspectResult>>() {
				});

		return inspectionResult;
	}

	/**
	 * @param inputMessage
	 * @return deidentifiedMessage
	 * @throws IOException
	 */
	public String getDeidentifiedString(String inputMessage) throws IOException {
		LOGGER.info("Inside DLP Service. Performing DLP Deidentification for message.");

		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(ConstantsURL.DLP_DEIDENTIFY_URL);

		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(new MessageWrapper(inputMessage));
		StringEntity entity = new StringEntity(requestJson);
		httppost.setEntity(entity);

		HttpResponse response = httpclient.execute(httppost);

		HttpEntity httpEntity = response.getEntity();
		MessageWrapper messageWrapper = mapper.readValue(httpEntity.getContent(), MessageWrapper.class);
		LOGGER.info("Inside DLP Service. Deidentified message: \n" + messageWrapper.getMessage());
		return messageWrapper.getMessage();
	}

	/**
	 * @param inputMessage
	 * @throws IOException
	 * @throws PANDataFoundSecurityViolationException
	 */
	public void checkForSensitiveData(String inputMessage) throws IOException, PANDataFoundSecurityViolationException {
		LOGGER.info("Inside DLP Service. Checking for sensitive data.");
		List<InspectResult> inspectionResults = getInspectionResult(inputMessage);
		boolean sensitiveDataPresent = inspectionResults.stream().anyMatch(result -> hasSensitiveData(result));

		if (sensitiveDataPresent) {
			LOGGER.warning(
					"Inside DLP Service. Throwing PANDataFoundSecurityViolationException. Reason:- PAN data present. "
							+ "Redacted Message:- " + getDeidentifiedString(inputMessage));
			throw new PANDataFoundSecurityViolationException();
		}

		LOGGER.info("Inside DLP Service. Sensitive data not present. PIIs found: \n" + inspectionResults);

	}

	private boolean hasSensitiveData(InspectResult inspectResult) {
		List<String> sensitiveInfoTypes = Arrays.asList("CREDIT_CARD_NUMBER", "INDIA_PAN_INDIVIDUAL");
		return sensitiveInfoTypes.contains(inspectResult.getInfoType());
	}

}
