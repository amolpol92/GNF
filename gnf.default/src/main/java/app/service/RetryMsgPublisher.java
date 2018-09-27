package app.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
//import com.google.api.services.pubsub.model.PubsubMessage;
import com.google.pubsub.v1.PubsubMessage.Builder;

import app.constants.Constants;
import app.dao.PublisherDao;
import app.model.MessageStatusListenerSO;
import app.model.PublisherMessage;
import app.model.UserMessageSO;
import app.util.DateUtility;
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

	/**
	 * this method receives messages and publish them on topics and logs details
	 * in publisher table
	 * 
	 * @param publishMessage
	 * @throws Exception
	 */
/*	public void publishMessage(MessageStatusListenerSO publishMessage) throws Exception {

		ByteString data = ByteString.copyFromUtf8(publishMessage.getMessage_data());
		Builder builder = PubsubMessage.newBuilder().setData(data);
		builder.putAttributes(Constants.GB_TXN_ID_KEY, publishMessage.getGlobal_txn_id());

		if (null != publishMessage.getReceiver_id() && "" != publishMessage.getReceiver_id()) {

			builder.putAttributes("recieverId", publishMessage.getReceiver_id());

		}

		PubsubMessage pubsubMessage = builder.build();

		String messageId = "";

		GenericMessagePublisher publisher = new GenericMessagePublisher();
		messageId = publisher.publishMessage(publishMessage.getProvider_id(), pubsubMessage);

		PublisherMessage publisherMessage = new PublisherMessage(publishMessage.getMessage_data(),
				publishMessage.getProvider_id());
		publisherMessage.setGlobalTransactionId(publishMessage.getGlobal_txn_id());

		publisherMessage.setPublishTime(DateUtility.getCurrentTimestamp());

		if (messageId != null && !messageId.isEmpty()) {
			publisherMessage.setMessageId(messageId);
		}
		// persist published details in publisher table
		persistInDB(publisherMessage);

	}

	private void persistInDB(PublisherMessage publisherMessage){

		PublisherDao publisherDao;
		try {
			publisherDao = new PublisherDao();
			publisherDao.insertPublishMessage(publisherMessage);
		} catch (SQLException | ParseException e) {
			e.printStackTrace();
		}
		

	}*/
	
	public void publishMessageOnFailure(MessageStatusListenerSO publishMessage)
	{
		
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
