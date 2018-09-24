package app.service;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.pubsub.v1.PubsubMessage;

import app.dao.PublisherDao;
import app.logging.CloudLogger;
import app.model.PublisherMessage;
import app.util.DateUtility;

/**
 * Responsible for publishing PubsubMessage to Pubsub Topic
 * 
 * @author adarshs1
 *
 */
public class NotifyServiceMessagePublisher {

	private CloudLogger LOGGER = CloudLogger.getLogger();

	/**
	 * @param topics
	 * @param pubsubMessage
	 * @return messageIdList
	 */
	public List<PublisherMessage> publishMessage(List<String> topics, PubsubMessage pubsubMessage,
			Map<String, String> labels) {

		List<PublisherMessage> messageIds = new ArrayList<>();

		topics.forEach(topic -> {

			String messageId = "";
			String globalTxnId = pubsubMessage.getAttributesOrThrow("globalTransactionId");
			try {

				LOGGER.info("Inside NotifyService. Publishing message on topic: " + topic, labels);
				GenericMessagePublisher publisher = new GenericMessagePublisher();
				messageId = publisher.publishMessage(topic, pubsubMessage);

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
