package app.model;

/**
 * @author AdarshSinghal
 *
 */
public class SubscriberMessage {

	private String id;
	private String messageId;
	private String message;
	private String subscriptionId;
	private String publishTime;
	private String pullTime;
	private String globalTransactionId;
	private String ackId;
	private String destGroupId;
	private String retryCounter;
	private String retryMessageFlag;

	public SubscriberMessage(String messageId, String message, String publishTime, String ackId, String globalTxnId) {
		super();
		this.messageId = messageId;
		this.message = message;
		this.publishTime = publishTime;
		this.ackId = ackId;
		this.globalTransactionId = globalTxnId;
	}

	public String getRetryCounter() {
		return retryCounter;
	}

	public void setRetryCounter(String retryCounter) {
		this.retryCounter = retryCounter;
	}

	public String getRetryMessageFlag() {
		return retryMessageFlag;
	}

	public void setRetryMessageFlag(String retryMessageFlag) {
		this.retryMessageFlag = retryMessageFlag;
	}

	public String getDestGroupId() {
		return destGroupId;
	}

	public void setDestGroupId(String destGroupId) {
		this.destGroupId = destGroupId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getAckId() {
		return ackId;
	}

	public void setAckId(String ackId) {
		this.ackId = ackId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getPullTime() {
		return pullTime;
	}

	public void setPullTime(String pullTime) {
		this.pullTime = pullTime;
	}

	public String getGlobalTransactionId() {
		return globalTransactionId;
	}

	public void setGlobalTransactionId(String globalTransactionId) {
		this.globalTransactionId = globalTransactionId;
	}

	@Override
	public String toString() {
		return "SubscriberMessage [id=" + id + ", messageId=" + messageId + ", message=" + message + ", subscriptionId="
				+ subscriptionId + ", publishTime=" + publishTime + ", pullTime=" + pullTime + ", globalTransactionId="
				+ globalTransactionId + ", ackId=" + ackId + "]";
	}

}
