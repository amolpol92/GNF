package app.model;

import java.util.List;

public class NotifyResponse {

	private String message;
	private List<PublisherMessage> publishedMessages;
	private boolean isException;

	public boolean isException() {
		return isException;
	}

	public void setException(boolean isException) {
		this.isException = isException;
	}

	public NotifyResponse(String message) {
		this.isException =true;
		this.message = message;
	}

	public NotifyResponse(List<PublisherMessage> publishedMessages) {
		this.publishedMessages = publishedMessages;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<PublisherMessage> getPublishedMessages() {
		return publishedMessages;
	}

	public void setPublishedMessages(List<PublisherMessage> publishedMessages) {
		this.publishedMessages = publishedMessages;
	}

}
