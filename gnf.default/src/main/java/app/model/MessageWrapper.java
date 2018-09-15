package app.model;

public class MessageWrapper {

	private String message;
	
	public MessageWrapper() {}

	public MessageWrapper(String message) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "MessageWrapper [message=" + message + "]";
	}

}
