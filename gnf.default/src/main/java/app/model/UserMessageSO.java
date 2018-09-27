package app.model;
/**
 * 
 * @author amolp
 *
 */
public class UserMessageSO {
	private String userId;
	private String emailId;
	private String mobileNumber;
	private String faxNumber;
	private String message;
	private String topicName;
	private String globalTransactionId;
	private String messageId;
	private String publishTime;
	private int retryCounter;
	private boolean retryMessageFlag;
	private String sourceId;
	private String targetId;
	
	
	
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
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
	public String getPublishTime() {
		return publishTime;
	}
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public String getGlobalTransactionId() {
		return globalTransactionId;
	}
	public void setGlobalTransactionId(String globalTransactionId) {
		this.globalTransactionId = globalTransactionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getEmailId() {
		return emailId;
	}
	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
	public String getMobileNumber() {
		return mobileNumber;
	}
	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}
	public String getFaxNumber() {
		return faxNumber;
	}
	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	@Override
	public String toString() {
		return "UserMessageSO [userId=" + userId + ", emailId=" + emailId + ", mobileNumber=" + mobileNumber
				+ ", faxNumber=" + faxNumber + ", message=" + message + ", topicName=" + topicName
				+ ", globalTransactionId=" + globalTransactionId + ", messageId=" + messageId + ", publishTime="
				+ publishTime + "]";
	}
	
	
	
	
}
