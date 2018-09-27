package app.model;

public class MessageStatusListenerSO {
	
private String global_txn_id;
private String provider_msg_id;
private String status;
private String timestamp;
private String receiver_id;
private String attempt;
private String provider_id;
private String source_id;
private String comments;
private String message_data;
private String reason;
private String target_id;
private String retry_counter;

public MessageStatusListenerSO(String global_txn_id, String status, String timestamp, String source_id,
		String comments) {
	super();
	this.global_txn_id = global_txn_id;
	this.status = status;
	this.timestamp = timestamp;
	this.source_id = source_id;
	this.comments = comments;
}
public MessageStatusListenerSO() {
	// TODO Auto-generated constructor stub
}



public String getRetry_counter() {
	return retry_counter;
}
public void setRetry_counter(String retry_counter) {
	this.retry_counter = retry_counter;
}
public String getTarget_id() {
	return target_id;
}
public void setTarget_id(String target_id) {
	this.target_id = target_id;
}
public String getMessage_data() {
	return message_data;
}
public void setMessage_data(String message_data) {
	this.message_data = message_data;
}
public String getReason() {
	return reason;
}
public void setReason(String reason) {
	this.reason = reason;
}
public String getGlobal_txn_id() {
	return global_txn_id;
}
public void setGlobal_txn_id(String global_txn_id) {
	this.global_txn_id = global_txn_id;
}
public String getProvider_msg_id() {
	return provider_msg_id;
}
public void setProvider_msg_id(String provider_msg_id) {
	this.provider_msg_id = provider_msg_id;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getTimestamp() {
	return timestamp;
}
public void setTimestamp(String timestamp) {
	this.timestamp = timestamp;
}
public String getReceiver_id() {
	return receiver_id;
}
public void setReceiver_id(String receiver_id) {
	this.receiver_id = receiver_id;
}
public String getAttempt() {
	return attempt;
}
public void setAttempt(String attempt) {
	this.attempt = attempt;
}
public String getProvider_id() {
	return provider_id;
}
public void setProvider_id(String provider_id) {
	this.provider_id = provider_id;
}
public String getSource_id() {
	return source_id;
}
public void setSource_id(String source_id) {
	this.source_id = source_id;
}
public String getComments() {
	return comments;
}
public void setComments(String comments) {
	this.comments = comments;
}
@Override
public String toString() {
	return "MessageStatusListenerSO [global_txn_id=" + global_txn_id + ", provider_msg_id=" + provider_msg_id
			+ ", status=" + status + ", timestamp=" + timestamp + ", receiver_id=" + receiver_id + ", attempt="
			+ attempt + ", provider_id=" + provider_id + ", source_id=" + source_id + ", comments=" + comments + "]";
}
		
}