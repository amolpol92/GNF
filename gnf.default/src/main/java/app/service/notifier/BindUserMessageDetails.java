package app.service.notifier;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.constants.Constants;
import app.model.LogRequest;
import app.model.MessageStatus;
import app.model.UserDetailsSO;
import app.model.UserMessageSO;
import app.service.client.LogServiceClient;

/**
 * @author AmolPol
 *
 *         this class is responsible for activities related to notifications and
 *         notifiers
 */
public class BindUserMessageDetails {


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
	 * @throws IOException 
	 */
	public void prepareMessagesWithPreferences(List<UserDetailsSO> receiverUserList, MessageStatus req) throws IOException {
		NotifierFactory factory = new NotifierFactory();
		List<UserMessageSO> emailPrefered = new ArrayList<>();
		List<UserMessageSO> smsPrefered = new ArrayList<>();

		Map<String,String> labels = new HashMap<>();
		labels.put(Constants.GB_TXN_ID_KEY, req.getGlobalTxnId());
		LogRequest logRequest = new LogRequest("Preparing to send messages based on preference.", "INFO", "gae_app", "UserService");
		logRequest.setLabels(labels);
		LogServiceClient.getLogger().log(logRequest);
		
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


		// message with user preferences will be published on respective topic
		PublishOnNotifier publish=new PublishOnNotifier();
		publish.publishOnSpecifcTopic(emailPrefered);
		publish.publishOnSpecifcTopic(smsPrefered);
	}



}