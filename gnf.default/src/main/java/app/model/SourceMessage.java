package app.model;

/**
 * @author AdarshSinghal
 *
 */
public class SourceMessage {

	private int sourceAuthLevel;
	private int groupId;
	private String message;
	private String globalTxnId;

	public SourceMessage(int sourceauthLevel, int groupId, String message) {
		super();
		this.sourceAuthLevel = sourceauthLevel;
		this.groupId = groupId;
		this.message = message;
	}

	public int getSourceAuthLevel() {
		return sourceAuthLevel;
	}

	public void setSourceAuthLevel(int sourceAuthLevel) {
		this.sourceAuthLevel = sourceAuthLevel;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getGlobalTxnId() {
		return globalTxnId;
	}

	public void setGlobalTxnId(String globalTxnId) {
		this.globalTxnId = globalTxnId;
	}

	@Override
	public String toString() {
		return "SourceMessage [sourceAuthLevel=" + sourceAuthLevel + ", groupId=" + groupId + ", message=" + message
				+ ", globalTxnId=" + globalTxnId + "]";
	}

}
