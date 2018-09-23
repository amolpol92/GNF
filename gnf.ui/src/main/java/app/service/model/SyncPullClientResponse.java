package app.service.model;

import java.util.List;

/**
 * @author AdarshSinghal
 *
 */
public class SyncPullClientResponse {

	private List<PulledMessage> pulledMessages;

	public SyncPullClientResponse() {
	}

	public SyncPullClientResponse(List<PulledMessage> pulledMessages) {
		this.pulledMessages = pulledMessages;
	}

	public List<PulledMessage> getPulledMessages() {
		return pulledMessages;
	}

	public void setPulledMessages(List<PulledMessage> pulledMessages) {
		this.pulledMessages = pulledMessages;
	}

	@Override
	public String toString() {
		return "SyncPullClientResponse [pulledMessages=" + pulledMessages + "]";
	}

}
