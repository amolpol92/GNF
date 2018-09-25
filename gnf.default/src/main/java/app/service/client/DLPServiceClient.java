package app.service.client;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.constants.Constants;
import app.constants.ConstantsURL;
import app.exception.PANDataFoundSecurityViolationException;
import app.model.DLPClientRequest;
import app.model.InspectionResultWrapper;
import app.model.LogRequest;
import app.model.MessageWrapper;

/**
 * Responsible for invoking DLP service for Inspection, Risk analysis &
 * Deidentification <br>
 * 
 * @author AdarshSinghal
 *
 */
public class DLPServiceClient {


	/**
	 * @param inputMessage
	 * @return inspectResult
	 * @throws IOException
	 */
	public InspectionResultWrapper getInspectionResult(DLPClientRequest request) throws IOException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(ConstantsURL.DLP_INSPECT_URL);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		StringEntity entity = new StringEntity(gson.toJson(request));
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
	public String getDeidentifiedString(DLPClientRequest request) throws IOException {

		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(ConstantsURL.DLP_DEIDENTIFY_URL);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		StringEntity entity = new StringEntity(gson.toJson(request));
		httppost.setEntity(entity);

		HttpResponse response = httpclient.execute(httppost);

		HttpEntity httpEntity = response.getEntity();

		String responseJson = EntityUtils.toString(httpEntity);

		MessageWrapper messageWrapper = gson.fromJson(responseJson, MessageWrapper.class);

		return messageWrapper.getMessage();
	}

	/**
	 * @param inputMessage
	 * @throws IOException
	 * @throws PANDataFoundSecurityViolationException
	 */
	public void checkForSensitiveData(DLPClientRequest request)
			throws IOException, PANDataFoundSecurityViolationException {
		InspectionResultWrapper inspectionResults = getInspectionResult(request);

		if (inspectionResults.getSensitiveDataFlag()) {
			
			String logMessage = "Terminating the transaction. Reason:- PAN data present. "
					+ "Redacted Message:- " + getDeidentifiedString(request);
			
			LogRequest logRequest = new LogRequest(logMessage, "WARNING", "gae_app", "NotifyService");
			logRequest.setLabels(getLabels(request));
			LogServiceClient.getLogger().log(logRequest);
			
			throw new PANDataFoundSecurityViolationException();
		}

	}

	private Map<String, String> getLabels(DLPClientRequest dlpRequest) {
		Map<String, String> labels = new HashMap<>();
		labels.put(Constants.GB_TXN_ID_KEY, dlpRequest.getGlobalTxnId());
		String srcAuthLvlStr = String.valueOf(dlpRequest.getSourceAuth());
		labels.put("Source Authorization Level", srcAuthLvlStr);
		labels.put("Target Group Id", dlpRequest.getGroupId());
		return labels;
	}

}
