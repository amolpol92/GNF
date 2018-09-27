package app.service.thirdparty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.google.api.services.pubsub.model.PubsubMessage;

import app.model.MessageStatus;
import app.model.MessageStatusListenerSO;
import app.servlet.HttpClientRequestHandler;
import app.util.ExternalProperties;

public class TwilioSmsNotifier {
	static List<PubsubMessage> smsOutstandingQueue = new ArrayList<>();

	/**
	 * @param userDetails
	 * @param message
	 * @throws IOException
	 */
	public void notifyUserBySMS(PubsubMessage message, int retryCount) throws IOException {
		String ack = null;

		TwilioSmsClient sms = new TwilioSmsClient();

		byte[] decodedMessageData = Base64.getMimeDecoder().decode(message.getData().getBytes());
		String decodedMessage = new String(decodedMessageData);

		ack = sms.sendSms(message.getAttributes().get("mobileNumber"), decodedMessage);

		if (null != ack && ack != "" && !ack.equalsIgnoreCase("failed")) {
			MessageStatusListenerSO listenerSO= statusUpdateRequest(message,ack);
			// TODO need access twilio api for actual delivery status
			updateDelConfirmation(listenerSO);
		} else if (ack.equalsIgnoreCase("failed")) {
			retryCount--;
			if (retryCount > 0)
				notifyUserBySMS(message, retryCount);
			else
				// TODO need to write listener who will retry sending this
				// message queue
				smsOutstandingQueue.add(message);
		}

	}

	/**
	 * @param message
	 * @return
	 */
	private MessageStatusListenerSO statusUpdateRequest(PubsubMessage message , String ack) {
		MessageStatusListenerSO listenerSO = new MessageStatusListenerSO();
		
		listenerSO.setGlobal_txn_id(message.getAttributes().get("globalTransactionId"));
		listenerSO.setProvider_msg_id(ack);
		listenerSO.setProvider_id("Twilio");
		listenerSO.setStatus("ReceivedByTwilio");
		listenerSO.setTimestamp(message.getPublishTime());
		listenerSO.setReceiver_id(message.getAttributes().get("mobileNumber"));
		listenerSO.setSource_id(null);
		String attempt=(null!=message.getAttributes().get("attempt")? Long.valueOf(message.getAttributes().get("attempt")):0)+1+"";
		listenerSO.setAttempt(attempt);
		listenerSO.setComments(null);
		listenerSO.setTarget_id(null);
		return listenerSO;
	}

	/**
	 * @param req
	 * @param delivered
	 * @throws IOException
	 */
	private void updateDelConfirmation(MessageStatusListenerSO listenerSO) throws IOException {

		HttpClientRequestHandler client = new HttpClientRequestHandler();

		String updateStatusSvcURL = ExternalProperties.getAppConfig("updatestatus.service.url");
		client.sendPostReturnStatus(listenerSO, updateStatusSvcURL);

	}

}
