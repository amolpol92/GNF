package app.service.notifier;

import java.util.List;

import app.logging.CloudLogger;
import app.model.UserMessageSO;
import app.service.ProviderMsgPublisher;

public class PublishOnNotifier {
	private CloudLogger LOGGER = CloudLogger.getLogger();

	/**
	 * @param Prefered
	 */
	public void publishOnSpecifcTopic(List<UserMessageSO> preferedNotification) {
		ProviderMsgPublisher publisher = new ProviderMsgPublisher();
		if (null != preferedNotification && preferedNotification.size() > 0)
			preferedNotification.forEach(publishMessage -> {
				try {
					publisher.publishMessage(publishMessage);
				} catch (Exception e) {
					LOGGER.error(e.getMessage());
				}
			});
	}
}