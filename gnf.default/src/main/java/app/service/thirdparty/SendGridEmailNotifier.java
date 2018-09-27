package app.service.thirdparty;

import java.io.IOException;
import java.util.Base64;

import com.google.api.services.pubsub.model.PubsubMessage;

import app.constants.Constants;
import app.model.DeliveryStatus;
import app.model.MessageStatusListenerSO;
import app.servlet.HttpClientRequestHandler;
import app.util.ExternalProperties;

public class SendGridEmailNotifier {
	
	

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

		if (null != ack && !ack.isEmpty() && !ack.equalsIgnoreCase("failed")) {
			MessageStatusListenerSO listenerSO  = statusUpdateRequest(message,ack);
			// TODO need access sendgrid api for actual delivery status
			updateDelConfirmation(listenerSO);
		} else if (ack.equalsIgnoreCase("failed")) {
			retryCount--;
			if (retryCount > 0)
			{
				notifyUserByEmail(message, retryCount);
			}
			else
			{
				// TODO need to write listener who will retry sending this
				sendToCallbackService(message, ack);
				
			}
}
	}
	/**
	 * @param message
	 * @param ack
	 */
	private void sendToCallbackService(PubsubMessage message, String ack) {
		String callbackSvcURL = ExternalProperties.getAppConfig("sendgridCallback.service.url");
		HttpClientRequestHandler httpClient = new HttpClientRequestHandler();
		DeliveryStatus deliveryStatus = getDeliveryStatusDetails(message,ack);
		try {
		httpClient.sendPostReturnStatus(deliveryStatus, callbackSvcURL);
		} catch (IOException e) {
		e.printStackTrace();
		}
	}
	private DeliveryStatus getDeliveryStatusDetails(PubsubMessage message,String ack)
	{
		//Timestamp currentTimestamp = new Timestamp(new Date().getTime());
		DeliveryStatus deliveryStatus=new DeliveryStatus();
		deliveryStatus.setEmail(message.getAttributes().get("emailId"));
		deliveryStatus.setAttempt("3");
		deliveryStatus.setResponse("500 unknown recipient");
		deliveryStatus.setEvent("failed");
		deliveryStatus.setReason("Invalid Email Id");
		deliveryStatus.setSg_message_id(ack);
		//deliveryStatus.setTimestamp(currentTimestamp);
		return deliveryStatus;
		
	}

	/**
	 * @param message
	 * @return
	 */
	private MessageStatusListenerSO statusUpdateRequest(PubsubMessage message,String ack) {
		MessageStatusListenerSO listenerSO = new MessageStatusListenerSO();
		
		listenerSO.setGlobal_txn_id(message.getAttributes().get(Constants.GB_TXN_ID_KEY));
		listenerSO.setProvider_msg_id(ack);
		listenerSO.setProvider_id("SendGrid");
		listenerSO.setStatus("ReceivedBySendGrid");
		listenerSO.setTimestamp(message.getPublishTime());
		listenerSO.setReceiver_id(message.getAttributes().get("emailId"));
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
