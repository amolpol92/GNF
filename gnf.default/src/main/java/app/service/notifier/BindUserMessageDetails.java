package app.service.notifier;

import java.util.ArrayList;
import java.util.List;

import app.logging.CloudLogger;
import app.model.MessageStatus;
import app.model.UserDetailsSO;
import app.model.UserMessageSO;

/**
 * @author AmolPol
 *
 *         this class is responsible for activities related to notifications and
 *         notifiers
 */
public class BindUserMessageDetails {

	private CloudLogger LOGGER = CloudLogger.getLogger();

	private static final String YES = "Yes";

	/**
	 * this method prepares seperate queues for providers TODO 1.need to modify
	 * this method completely with Strategy approach for each notifier as number
	 * of notifier API's will be much more such as sendgrid and twilio.
	 * 
	 * 
	 * @param allUsers
	 * @param req
	 * @return boolean
	 */
	public void prepareMessagesWithPreferences(List<UserDetailsSO> receiverUserList, MessageStatus req) {
		LOGGER.info("Preparing messages with preferences.");
		NotifierFactory factory = new NotifierFactory();
		List<UserMessageSO> emailPrefered = new ArrayList<>();
		List<UserMessageSO> smsPrefered = new ArrayList<>();

		for (UserDetailsSO userDet : receiverUserList) {

			if (userDet.getEmailFlag().equalsIgnoreCase(YES)) {
				Notifier notifier = factory.getNotifier("Email");
				emailPrefered.add(notifier.getNotificationDetails(userDet, req));
			}
			if (userDet.getSmsFlag().equalsIgnoreCase(YES)) {
				Notifier notifier = factory.getNotifier("Sms");
				smsPrefered.add(notifier.getNotificationDetails(userDet, req));
			}
		}

		LOGGER.info("Publishing on Notifier specific topics. Topics -> " + "SendGridEmail,TwilioSMS");

		// message with user preferences will be published on respective topic
		PublishOnNotifier publish=new PublishOnNotifier();
		publish.publishOnSpecifcTopic(emailPrefered);
		publish.publishOnSpecifcTopic(smsPrefered);
	}



}