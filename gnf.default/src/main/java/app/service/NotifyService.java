package app.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

import app.exception.NoSuchGroupException;
import app.exception.PANDataFoundSecurityViolationException;
import app.exception.UserNotAuthorizedException;
import app.logging.CloudLogger;
import app.model.AuthorizationRequest;
import app.model.AuthorizationResponse;
import app.model.InspectionResultWrapper;
import app.model.PublisherMessage;
import app.model.SourceMessage;
import app.service.client.AuthServiceClient;
import app.service.client.DLPServiceClient;
import app.util.ExternalProperties;

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

	private CloudLogger LOGGER = CloudLogger.getLogger();

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
		LOGGER.info("Inside Notify Service. Publishing messages on these topics -> " + topics
				+ ". com.google.pubsub.v1.PubsubMessage:\n" + pubsubMessage);
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
	public InspectionResultWrapper getInspectionResult(String inputMessage) throws IOException {
		LOGGER.info("Inside Notify Service. Passing message to DLP Service Invoker for inspection.");
		InspectionResultWrapper inspectionResult = dlpServiceClient.getInspectionResult(inputMessage);
		LOGGER.info("Inside Notify Service. Received inspection result from DLP Service Invoker. \nInfotypes matched: "
				+ inspectionResult.getInspectResults().size());
		return inspectionResult;
	}

	/**
	 * Uses DLP Service Invoker to perform DLP Deidentification on the given string
	 * 
	 * @param inputMsg
	 * @return de-identified String
	 * @throws IOException
	 */
	public String getDeidentifiedString(String inputMsg) throws IOException {
		LOGGER.info("Inside Notify Service. Passing message to DLP Service Invoker for deidentification. "
				+ "\nMessage: " + inputMsg);
		String deidentifiedString = dlpServiceClient.getDeidentifiedString(inputMsg);
		LOGGER.info("Inside Notify Service. Received deidentified message from DLP Service Invoker \nMessage - "
				+ deidentifiedString);
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
	public List<PublisherMessage> notify(SourceMessage sourceMessage) throws SQLException, NoSuchGroupException, IOException,
			PANDataFoundSecurityViolationException, UserNotAuthorizedException {
		String message = sourceMessage.getMessage();
		LOGGER.info("Inside Notify Service. " + "Passing source message to Authorization Service for authorization.");
		// Application level Source authorization against Target Group
		AuthServiceClient client = new AuthServiceClient();
		String groupId = String.valueOf(sourceMessage.getGroupId());
		AuthorizationResponse authResp = client
				.authorize(new AuthorizationRequest(sourceMessage.getSourceAuthLevel(), groupId));

		if (!authResp.isAuthorized())
			throw new UserNotAuthorizedException(authResp.getReason());

		// Inspection & termination on violation
		dlpServiceClient.checkForSensitiveData(message);

		// DeIdentification - Redact/Mask PIIs.
		String deidentifiedStr = dlpServiceClient.getDeidentifiedString(message);

		LOGGER.info("Inside Notify Service. Received Inspection results. PAN not found. The deidentified message is \n"
				+ deidentifiedStr);

		PubsubMessage pubsubMessage = SourceToPubSubMessageConverter.convert(sourceMessage, deidentifiedStr);

		String topicNames = ExternalProperties.getAppConfig("app.gc.pubsub.topic.layer1");

		LOGGER.info("Inside Notify Service. Need to publish message on " + topicNames
				+ ". Delegating responsibility to NotifyServiceMessagePublisher.");

		NotifyServiceMessagePublisher publisher = new NotifyServiceMessagePublisher();
		List<PublisherMessage> messageIds = publisher.publishMessage(topicNames, pubsubMessage);
		return messageIds;
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
					.putAttributes("globalTransactionId", sourceMessage.getGlobalTxnId())
					.putAttributes("srcAuthLevel", srcAuthLvlStr).putAttributes("destGroupId", groupIdStr).build();
			return pubsubMessage;
		}
	}

}
