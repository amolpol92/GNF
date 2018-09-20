package app.model;

/**
 * @author AdarshSinghal
 *
 */
public class AuthorizationRequest {

	private int sourceAuthLevel;
	private String groupId;

	public AuthorizationRequest() {
	}

	public AuthorizationRequest(int sourceauthLevel, String groupId) {
		this.sourceAuthLevel = sourceauthLevel;
		this.groupId = groupId;
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

	@Override
	public String toString() {
		return "SourceMessage [sourceAuthLevel=" + sourceAuthLevel + ", groupId=" + groupId + "]";
	}

}
