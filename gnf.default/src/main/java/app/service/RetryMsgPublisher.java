package app.service;

import java.util.List;

import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

import app.constants.Constants;
import app.model.MessageStatusListenerSO;
import app.util.ExternalProperties;
import app.util.ListUtils;

/**
 * 
 * Responsible for publishing message on Sendgrid and twilio topics based on
 * preferences
 * 
 * @author amolp
 *
 */
public class RetryMsgPublisher {

	public void publishMessageOnFailure(MessageStatusListenerSO publishMessage) {

		String message = publishMessage.getMessage_data();

		String topicNames = ExternalProperties.getAppConfig("app.gc.pubsub.topic.layer1");
		List<String> topics = ListUtils.getListFromCSV(topicNames);

		ByteString data = ByteString.copyFromUtf8(message);
		PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data)
				.putAttributes(Constants.GB_TXN_ID_KEY, publishMessage.getGlobal_txn_id())
				.putAttributes(Constants.RETRY_FLAG, "true")
				.putAttributes(Constants.RETRY_COUNTER, publishMessage.getRetry_counter()).build();
		NotifyService notifyService = new NotifyService();
		notifyService.publishMessage(topics, pubsubMessage);
	}

}
