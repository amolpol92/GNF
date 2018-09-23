package app.model;

import java.util.List;

/**
 * @author AdarshSinghal
 *
 */
public class SyncPullClientResponse {

	private List<SubscriberMessage> pulledMessages;

	public SyncPullClientResponse() {
	}

	public SyncPullClientResponse(List<SubscriberMessage> pulledMessages) {
		this.pulledMessages = pulledMessages;
	}

	public List<SubscriberMessage> getPulledMessages() {
		return pulledMessages;
	}

	public void setPulledMessages(List<SubscriberMessage> pulledMessages) {
		this.pulledMessages = pulledMessages;
	}

	@Override
	public String toString() {
		return "SyncPullClientResponse [pulledMessages=" + pulledMessages + "]";
	}

}
