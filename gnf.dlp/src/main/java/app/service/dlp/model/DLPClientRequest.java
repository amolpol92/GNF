package app.service.dlp.model;

/**
 * @author AdarshSinghal
 *
 */
public class DLPClientRequest {

	private String message;
	private int sourceAuth;
	private String groupId;
	private String globalTxnId;

	public DLPClientRequest() {
	}

	public DLPClientRequest(String message, int sourceAuth, String groupId, String globalTxnId) {
		this.message = message;
		this.sourceAuth = sourceAuth;
		this.groupId = groupId;
		this.globalTxnId = globalTxnId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getSourceAuth() {
		return sourceAuth;
	}

	public void setSourceAuth(int sourceAuth) {
		this.sourceAuth = sourceAuth;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGlobalTxnId() {
		return globalTxnId;
	}

	public void setGlobalTxnId(String globalTxnId) {
		this.globalTxnId = globalTxnId;
	}

	@Override
	public String toString() {
		return "DLPClientRequest [message=" + message + ", sourceAuth=" + sourceAuth + ", groupId=" + groupId
				+ ", globalTxnId=" + globalTxnId + "]";
	}

}
