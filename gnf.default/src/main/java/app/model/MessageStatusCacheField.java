package app.model;

/**
 * 
 * @author Aniruddha Sinha
 * @Description :This table contains the fields which are in message status
 *              cache database
 *
 */
public class MessageStatusCacheField {
	private String global_txn_id;
	private String provider_msg_id;
	private String status;
	private String timestamp;
	private String receiver_id;
	private String attempt;
	private String provider_id;
	private String source_id;
	private String comments;
	private String target_id;
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
	public String getTarget_id() {
		return target_id;
	}
	public void setTarget_id(String target_id) {
		this.target_id = target_id;
	}


	
	
}
