package app.service.authorization.model;

/**
 * @author AdarshSinghal
 *
 */
public class AuthorizationRequest {

	private int sourceAuthLevel;
	private String groupId;
	private String globalTxnId;

	public AuthorizationRequest() {
	}

	public AuthorizationRequest(int sourceAuthLevel, String groupId, String globalTxnId) {
		this.sourceAuthLevel = sourceAuthLevel;
		this.groupId = groupId;
		this.globalTxnId = globalTxnId;
	}

	public int getSourceAuthLevel() {
		return sourceAuthLevel;
	}

	public void setSourceAuthLevel(int sourceAuthLevel) {
		this.sourceAuthLevel = sourceAuthLevel;
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
		return "AuthorizationRequest [sourceAuthLevel=" + sourceAuthLevel + ", groupId=" + groupId + ", globalTxnId="
				+ globalTxnId + "]";
	}

}
