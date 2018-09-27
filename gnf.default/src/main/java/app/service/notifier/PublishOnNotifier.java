package app.service.notifier;

import java.sql.SQLException;
import java.util.List;

import app.dao.MessageStatusListenerDao;
import app.model.MessageStatusListenerSO;
import app.model.UserMessageSO;
import app.service.ProviderMsgPublisher;

public class PublishOnNotifier {

	/**
	 * @param Prefered
	 */
	public void publishOnSpecifcTopic(List<UserMessageSO> preferedNotification) {
		ProviderMsgPublisher publisher = new ProviderMsgPublisher();
		// TODO add details to messsageStatus cache

		if (null != preferedNotification && preferedNotification.size() > 0)
			preferedNotification.forEach(publishMessage -> {
				try {
					publisher.publishMessage(publishMessage);
					if(!publishMessage.isRetryMessageFlag())
					persistInDb(prepareStatusListenerMessage(publishMessage));
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
	}

	private MessageStatusListenerSO prepareStatusListenerMessage(UserMessageSO userMessageSO) {
		MessageStatusListenerSO listenerSO = new MessageStatusListenerSO();

		listenerSO.setGlobal_txn_id(userMessageSO.getGlobalTransactionId());

		listenerSO.setProvider_id(userMessageSO.getTopicName());

		listenerSO.setTimestamp(userMessageSO.getPublishTime());
		listenerSO.setReceiver_id(
				null != userMessageSO.getEmailId() ? userMessageSO.getEmailId() : userMessageSO.getMobileNumber());
		listenerSO.setSource_id(userMessageSO.getSourceId());

		listenerSO.setTarget_id(userMessageSO.getTargetId());
		listenerSO.setRetry_counter(userMessageSO.getRetryCounter()+"");
		return listenerSO;
	}

	private void persistInDb(MessageStatusListenerSO listenerSO) {
		MessageStatusListenerDao dao;
		try {
			dao = new MessageStatusListenerDao();
			dao.statusCacheListener(listenerSO);
		} catch (SQLException e) {
			e.printStackTrace();

		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}