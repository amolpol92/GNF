package app.service.authorization.model;

/**
 * @author adarshsinghal695
 *
 */
public class GroupAuthResponse {

	private int authLevel;

	public GroupAuthResponse() {
	}

	public GroupAuthResponse(int authLevel) {
		super();
		this.authLevel = authLevel;
	}

	public int getAuthLevel() {
		return authLevel;
	}

	public void setAuthLevel(int authLevel) {
		this.authLevel = authLevel;
	}

	@Override
	public String toString() {
		return "GroupAuthResponse [authLevel=" + authLevel + "]";
	}

}
