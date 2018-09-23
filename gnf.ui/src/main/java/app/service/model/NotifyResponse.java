package app.service.model;

import java.util.List;

public class NotifyResponse {

	private String message;
	private List<PublishedMessage> publishedMessages;
	private boolean isException;

	public boolean isException() {
		return isException;
	}

	public void setException(boolean isException) {
		this.isException = isException;
	}

	public NotifyResponse(String message) {
		this.isException = true;
		this.message = message;
	}

	public NotifyResponse(List<PublishedMessage> publishedMessages) {
		super();
		this.publishedMessages = publishedMessages;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<PublishedMessage> getPublishedMessages() {
		return publishedMessages;
	}

	public void setPublishedMessages(List<PublishedMessage> publishedMessages) {
		this.publishedMessages = publishedMessages;
	}

	@Override
	public String toString() {
		return "NotifyResponse [message=" + message + ", publishedMessages=" + publishedMessages + ", isException="
				+ isException + "]";
	}

}
