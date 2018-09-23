package app.model;

import java.util.List;

public class PublishResponse {

	private String globalTxnId;
	private List<PublisherMessage> publishedMessage;

	public PublishResponse() {
	}

	public PublishResponse(String globalTxnId, List<PublisherMessage> publishedMessage) {
		this.globalTxnId = globalTxnId;
		this.publishedMessage = publishedMessage;
	}

	public String getGlobalTxnId() {
		return globalTxnId;
	}

	public void setGlobalTxnId(String globalTxnId) {
		this.globalTxnId = globalTxnId;
	}

	public List<PublisherMessage> getPublishedMessage() {
		return publishedMessage;
	}

	public void setPublishedMessage(List<PublisherMessage> publishedMessage) {
		this.publishedMessage = publishedMessage;
	}

	@Override
	public String toString() {
		return "PublishResponse [globalTxnId=" + globalTxnId + ", publishedMessage=" + publishedMessage + "]";
	}

}
