package app.service.dlp;

import java.io.IOException;
import java.util.Collections;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.constants.ConstantsURL;
import app.exception.PANDataFoundSecurityViolationException;
import app.logging.CloudLogger;
import app.model.InspectionResultWrapper;
import app.model.MessageWrapper;

/**
 * Responsible for invoking DLP service for Inspection, Risk analysis &
 * Deidentification <br>
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
	public InspectionResultWrapper getInspectionResult(String inputMessage) throws IOException {
		LOGGER.info("Performing POST on " + ConstantsURL.DLP_INSPECT_URL);
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(ConstantsURL.DLP_INSPECT_URL);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		StringEntity entity = new StringEntity(gson.toJson(new MessageWrapper(inputMessage)));
		httppost.setEntity(entity);

		HttpResponse response = httpclient.execute(httppost);

		HttpEntity httpEntity = response.getEntity();
		if (httpEntity == null || httpEntity.getContent() == null) {
			return new InspectionResultWrapper(Collections.emptyList());
		}

		String jsonStr = EntityUtils.toString(httpEntity);
		InspectionResultWrapper results = gson.fromJson(jsonStr, InspectionResultWrapper.class);

		return results;
	}

	/**
	 * @param inputMessage
	 * @return deidentifiedMessage
	 * @throws IOException
	 */
	public String getDeidentifiedString(String message) throws IOException {
		LOGGER.info("Performing POST on " + ConstantsURL.DLP_DEIDENTIFY_URL);

		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(ConstantsURL.DLP_DEIDENTIFY_URL);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		StringEntity entity = new StringEntity(gson.toJson(new MessageWrapper(message)));
		httppost.setEntity(entity);

		HttpResponse response = httpclient.execute(httppost);

		HttpEntity httpEntity = response.getEntity();

		String responseJson = EntityUtils.toString(httpEntity);

		MessageWrapper messageWrapper = gson.fromJson(responseJson, MessageWrapper.class);

		LOGGER.info("Performed POST on " + ConstantsURL.DLP_DEIDENTIFY_URL + " \n Message: \n"
				+ messageWrapper.getMessage());
		return messageWrapper.getMessage();
	}

	/**
	 * @param inputMessage
	 * @throws IOException
	 * @throws PANDataFoundSecurityViolationException
	 */
	public void checkForSensitiveData(String inputMessage) throws IOException, PANDataFoundSecurityViolationException {
		LOGGER.info("Inside checkForSensitiveData(...) of DLPServiceInvoker. " + "\nChecking for sensitive data.");
		InspectionResultWrapper inspectionResults = getInspectionResult(inputMessage);

		if (inspectionResults.getSensitiveDataFlag()) {
			LOGGER.warning(
					"Inside DLP Service. Throwing PANDataFoundSecurityViolationException. Reason:- PAN data present. "
							+ "Redacted Message:- " + getDeidentifiedString(inputMessage));
			throw new PANDataFoundSecurityViolationException();
		}

		LOGGER.info("Inside DLP Service. Sensitive data not present. PIIs found: \n" + inspectionResults);

	}

}
