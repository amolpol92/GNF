package app.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

import app.constants.Constants;
import app.exception.NoSuchGroupException;
import app.exception.PANDataFoundSecurityViolationException;
import app.exception.UserNotAuthorizedException;
import app.model.AuthorizationRequest;
import app.model.DLPClientRequest;
import app.model.InspectionResultWrapper;
import app.model.LogRequest;
import app.model.PublisherMessage;
import app.model.SourceMessage;
import app.service.client.AuthServiceClient;
import app.service.client.DLPServiceClient;
import app.util.ExternalProperties;
import app.util.ListUtils;

/**
 * Responsible for Publishing messages for PubSub Layer 1 which contains three
 * topics. <br>
 * <br>
 * Delegates DLP specific responsibility to DLPService. <br>
 * Delegates Authorization specific responsibility to AuthorizationService. <br>
 * <br>
 * Service class should be 'As Simple As Possible'. If you're modifying this
 * class, add operation &amp; delegate logics to other class <br>
 * <br>
 * 
 * @author AdarshSinghal
 *
 */
public class NotifyService {

	// TODO Remove dependencies from here. Use HTTPClient API to hit POST URL. Check
	// similar implementation for reference at
	// updateDelConfirmation() of NotifyUtility
	private DLPServiceClient dlpServiceClient;

	public NotifyService() {
		dlpServiceClient = new DLPServiceClient();
	}

	/**
	 * Publish PubsubMessage on provided list of topics.
	 * 
	 * @param topics
	 * @param pubsubMessage
	 * @return list of message ids
	 */
	public List<PublisherMessage> publishMessage(List<String> topics, PubsubMessage pubsubMessage) {

		NotifyServiceMessagePublisher publisher = new NotifyServiceMessagePublisher();
		List<PublisherMessage> messageIds = publisher.publishMessage(topics, pubsubMessage);
		return messageIds;
	}

	/**
	 * Uses DLPService to perform DLP Inspection on the given string
	 * 
	 * @param inputMessage
	 * @return list of inspection results
	 * @throws IOException
	 */
	public InspectionResultWrapper getInspectionResult(DLPClientRequest dlpRequest) throws IOException {
		InspectionResultWrapper inspectionResult = dlpServiceClient.getInspectionResult(dlpRequest);
		return inspectionResult;
	}

	/**
	 * Uses DLP Service Invoker to perform DLP Deidentification on the given string
	 * 
	 * @param inputMsg
	 * @return de-identified String
	 * @throws IOException
	 */
	public String getDeidentifiedString(DLPClientRequest dlpRequest) throws IOException {
		String deidentifiedString = dlpServiceClient.getDeidentifiedString(dlpRequest);
		return deidentifiedString;
	}

	/**
	 * @param sourceMessage
	 * @return
	 * @throws SQLException
	 * @throws NoSuchGroupException
	 * @throws IOException
	 * @throws PANDataFoundSecurityViolationException
	 * @throws UserNotAuthorizedException
	 */
	public List<PublisherMessage> notify(SourceMessage sourceMessage) throws SQLException, NoSuchGroupException,
			IOException, PANDataFoundSecurityViolationException, UserNotAuthorizedException {

		String globalTxnId = sourceMessage.getGlobalTxnId();
		String groupId = String.valueOf(sourceMessage.getGroupId());
		int sourceAuthLevel = sourceMessage.getSourceAuthLevel();

		logMessage(sourceMessage, "Passing source message to Authorization Service", "INFO");
		AuthorizationRequest authorizeRequest = new AuthorizationRequest(sourceAuthLevel, groupId, globalTxnId);
		AuthServiceClient.authorize(authorizeRequest);

		DLPClientRequest dlpRequest = new DLPClientRequest(sourceMessage.getMessage(), sourceAuthLevel, groupId,
				globalTxnId);
		// Inspection & termination on violation
		dlpServiceClient.checkForSensitiveData(dlpRequest);

		// DeIdentification - Redact/Mask PIIs.
		String deidentifiedStr = dlpServiceClient.getDeidentifiedString(dlpRequest);

		PubsubMessage pubsubMessage = SourceToPubSubMessageConverter.convert(sourceMessage, deidentifiedStr);

		String topicNames = ExternalProperties.getAppConfig("app.gc.pubsub.topic.layer1");

		NotifyServiceMessagePublisher publisher = new NotifyServiceMessagePublisher();

		List<String> topics = ListUtils.getListFromCSV(topicNames);

		
		List<PublisherMessage> messageIds = publisher.publishMessage(topics, pubsubMessage);

		return messageIds;
	}

	private void logMessage(SourceMessage dlpRequest, String message, String severity)
			throws ClientProtocolException, IOException {
		LogRequest logRequest = new LogRequest(message, severity, "gae_app", "NotifyService");
		Map<String, String> labels = new HashMap<>();
		labels.put(Constants.GB_TXN_ID_KEY, dlpRequest.getGlobalTxnId());
		labels.put("Source Authorization Level", String.valueOf(dlpRequest.getSourceAuthLevel()));
		labels.put("Target Group Id", String.valueOf(dlpRequest.getGroupId()));
		logRequest.setLabels(labels);
		LogServiceClient.getLogger().log(logRequest);
	}

	/**
	 * This class is responsible for creating new PubSubMessage with deidentified
	 * string as messsage data. PubsubMessage is immutable, hence, we're handling it
	 * here. Creating new Pubsub message will change messageId<br>
	 * 
	 * @param sourceMessage
	 * @param deidentifiedStr
	 * @return
	 * @throws IOException
	 * 
	 * @author AdarshSinghal
	 */
	private static class SourceToPubSubMessageConverter {

		private static PubsubMessage convert(SourceMessage sourceMessage, String deidentifiedStr) throws IOException {
			ByteString data = ByteString.copyFromUtf8(deidentifiedStr);
			String srcAuthLvlStr = String.valueOf(sourceMessage.getSourceAuthLevel());
			String groupIdStr = String.valueOf(sourceMessage.getGroupId());

			PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data)
					.putAttributes(Constants.GB_TXN_ID_KEY, sourceMessage.getGlobalTxnId())
					.putAttributes(Constants.SOURCE_AUTH_LEVEL, srcAuthLvlStr)
					.putAttributes(Constants.TARGET_GROUP_ID, groupIdStr)
					.putAttributes(Constants.RETRY_FLAG, "false")
					.putAttributes(Constants.RETRY_COUNTER, "0")
					.build();
			return pubsubMessage;
		}
	}

}
