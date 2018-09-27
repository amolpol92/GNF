package app.model;

/**
 * @author AmolPol @Description; to be used by the User Service contains the
 *         information regarding the message content
 *
 */
public class MessageStatus {

	private String messageData;
	private String messageId;
	private String deliveryFlag;
	private String destGroupId;
	private String emailNotifierType;
	private String smsNotifeirType;
	private String globalTxnId;
	private int retryCounter;
	private boolean retryMessageFlag;

	
	

	public int getRetryCounter() {
		return retryCounter;
	}

	public void setRetryCounter(int retryCounter) {
		this.retryCounter = retryCounter;
	}

	public boolean isRetryMessageFlag() {
		return retryMessageFlag;
	}

	public void setRetryMessageFlag(boolean retryMessageFlag) {
		this.retryMessageFlag = retryMessageFlag;
	}

	public String getEmailNotifierType() {
		return emailNotifierType;
	}

	public void setEmailNotifierType(String emailNotifierType) {
		this.emailNotifierType = emailNotifierType;
	}

	public String getSmsNotifeirType() {
		return smsNotifeirType;
	}

	public void setSmsNotifeirType(String smsNotifeirType) {
		this.smsNotifeirType = smsNotifeirType;
	}

	public String getDestGroupId() {
		return destGroupId;
	}

	public void setDestGroupId(String destGroupId) {
		this.destGroupId = destGroupId;
	}

	public String getDeliveryFlag() {
		return deliveryFlag;
	}

	public void setDeliveryFlag(String deliveryFlag) {
		this.deliveryFlag = deliveryFlag;
	}

	public String getMessageData() {
		return messageData;
	}

	public void setMessageData(String messageData) {
		this.messageData = messageData;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getGlobalTxnId() {
		return globalTxnId;
	}

	public void setGlobalTxnId(String globalTxnId) {
		this.globalTxnId = globalTxnId;
	}

	@Override
	public String toString() {
		return "MessageStatus [messageData=" + messageData + ", messageId=" + messageId + ", deliveryFlag="
				+ deliveryFlag + ", destGroupId=" + destGroupId + ", emailNotifierType=" + emailNotifierType
				+ ", smsNotifeirType=" + smsNotifeirType + ", globalTxnId=" + globalTxnId + "]";
	}

}
