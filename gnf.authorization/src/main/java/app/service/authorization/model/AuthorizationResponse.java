package app.service.authorization.model;

public class AuthorizationResponse {

	private boolean isAuthorized;
	private String reason;

	public AuthorizationResponse() {
	}

	public AuthorizationResponse(boolean isAuthorized, String reason) {
		this.isAuthorized = isAuthorized;
		this.reason = reason;
	}

	public boolean isAuthorized() {
		return isAuthorized;
	}

	public void setAuthorized(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "AuthorizationResponse [isAuthorized=" + isAuthorized + ", reason=" + reason + "]";
	}
	
	

}
