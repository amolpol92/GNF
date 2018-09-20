package app.exception;

public class UserNotAuthorizedException extends Exception {

	private static final long serialVersionUID = -6971211231364112834L;

	public UserNotAuthorizedException() {
		super("Insufficient Authorization.");
	}

	public UserNotAuthorizedException(String reason) {
		super(reason);
	}

}
