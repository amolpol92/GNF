package app.service.model;

import java.util.List;

public class PublishResponse {

	private String globalTxnId;
	private List<PublishedMessage> publishedMessage;

	public PublishResponse() {
	}

	public PublishResponse(String globalTxnId, List<PublishedMessage> publishedMessage) {
		this.globalTxnId = globalTxnId;
		this.publishedMessage = publishedMessage;
	}

	public String getGlobalTxnId() {
		return globalTxnId;
	}

	public void setGlobalTxnId(String globalTxnId) {
		this.globalTxnId = globalTxnId;
	}

	public List<PublishedMessage> getPublishedMessage() {
		return publishedMessage;
	}

	public void setPublishedMessage(List<PublishedMessage> publishedMessage) {
		this.publishedMessage = publishedMessage;
	}

	@Override
	public String toString() {
		return "PublishResponse [globalTxnId=" + globalTxnId + ", publishedMessage=" + publishedMessage + "]";
	}

}
