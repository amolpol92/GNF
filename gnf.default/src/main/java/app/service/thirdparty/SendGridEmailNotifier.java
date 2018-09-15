package app.service.thirdparty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.google.api.services.pubsub.model.PubsubMessage;

import app.model.MessageStatus;
import app.servlet.HttpClientRequestHandler;
import app.util.ExternalProperties;

public class SendGridEmailNotifier {
	
	static List<PubsubMessage> emailOutstandingQueue = new ArrayList<>();

	/**
	 * @param userDetails
	 * @param message
	 * @throws IOException
	 */
	public void notifyUserByEmail(PubsubMessage message, int retryCount) throws IOException {
		String ack = null;

		SendGridEmailClient mail = new SendGridEmailClient();
		byte[] decodedMessageData = Base64.getMimeDecoder().decode(message.getData().getBytes());
		String decodedMessage = new String(decodedMessageData);

		ack = mail.sendEmail(message.getAttributes().get("emailId"), decodedMessage);

		if (null != ack && ack != "" && ack.equalsIgnoreCase("ReceivedBySendgrid")) {
			MessageStatus req = statusUpdateRequest(message);
			// TODO need access sendgrid api for actual delivery status
			updateDelConfirmation(req);
		} else if (ack.equalsIgnoreCase("failed")) {
			retryCount--;
			if (retryCount > 0)
				notifyUserByEmail(message, retryCount);
			else
				// TODO need to write listener who will retry sending this
				// message queue
				emailOutstandingQueue.add(message);
		}
	}

	/**
	 * @param message
	 * @return
	 */
	private MessageStatus statusUpdateRequest(PubsubMessage message) {
		MessageStatus req = new MessageStatus();
		req.setDeliveryFlag("true");
		req.setMessageData(message.getData());
		req.setMessageId(message.getAttributes().get("globalTransactionId"));
		return req;
	}

	/**
	 * @param req
	 * @param delivered
	 * @throws IOException
	 */
	private void updateDelConfirmation(MessageStatus req) throws IOException {

		HttpClientRequestHandler client = new HttpClientRequestHandler();

		String updateStatusSvcURL = ExternalProperties.getAppConfig("updatestatus.service.url");
		client.sendPostReturnStatus(req, updateStatusSvcURL);

	}
}
