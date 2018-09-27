package app.service;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.pubsub.v1.PubsubMessage;

import app.constants.Constants;
import app.dao.PublisherDao;
import app.model.LogRequest;
import app.model.PublisherMessage;
import app.service.client.LogServiceClient;
import app.util.DateUtility;

/**
 * Responsible for publishing PubsubMessage to Pubsub Topic
 * 
 * @author adarshs1
 *
 */
public class NotifyServiceMessagePublisher {

	/**
	 * @param topics
	 * @param pubsubMessage
	 * @return messageIdList
	 */
	public List<PublisherMessage> publishMessage(List<String> topics, PubsubMessage pubsubMessage) {

		List<PublisherMessage> messageIds = new ArrayList<>();

		topics.forEach(topic -> {

			String messageId = "";
			String globalTxnId = pubsubMessage.getAttributesOrDefault(Constants.GB_TXN_ID_KEY, "NA");
			try {

				GenericMessagePublisher publisher = new GenericMessagePublisher();
				Map<String,String> labels = pubsubMessage.getAttributesMap();
				String logMessage = "Requested to Publish message on topic: " + topic;
				if (topic.equalsIgnoreCase("logMsg")) {
					messageId = logPublishRequest(logMessage, labels);
				} else {
					logPublishRequest(logMessage, labels);
					messageId = publisher.publishMessage(topic, pubsubMessage);
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}

			String stringUtf8 = pubsubMessage.getData().toStringUtf8();

			PublisherMessage publishedMessage = new PublisherMessage(stringUtf8, topic);
			publishedMessage.setGlobalTransactionId(globalTxnId);
			publishedMessage.setPublishTime(DateUtility.getCurrentTimestamp());

			if (messageId != null && !messageId.isEmpty()) {
				publishedMessage.setMessageId(messageId);
				messageIds.add(publishedMessage);
			}

			persistInDB(publishedMessage);

		});

		return messageIds;

	}

	private String logPublishRequest(String message, Map<String, String> labels) throws IOException {
		LogRequest topicLogReq = new LogRequest(message, "INFO", "gae_app", "NotifyService");
		topicLogReq.setLabels(labels);
		return LogServiceClient.getLogger().log(topicLogReq);
	}

	/**
	 * Insert into Publisher table
	 * 
	 * @param publisherMessage
	 * @throws ParseException
	 * @throws SQLException
	 */
	private void persistInDB(PublisherMessage publisherMessage) {
		PublisherDao publisherDao;
		try {
			publisherDao = new PublisherDao();
			publisherDao.insertPublishMessage(publisherMessage);
		} catch (SQLException | ParseException e1) {
			e1.printStackTrace();
		}
	}

}
