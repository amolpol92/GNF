package app.model;

public class SyncPullClientRequest {

	private int maxMessage;
	private boolean returnImmediately;

	public SyncPullClientRequest() {
	}

	public SyncPullClientRequest(int maxMessage, boolean returnImmediately) {
		this.maxMessage = maxMessage;
		this.returnImmediately = returnImmediately;
	}

	public int getMaxMessage() {
		return maxMessage;
	}

	public void setMaxMessage(int maxMessage) {
		this.maxMessage = maxMessage;
	}

	public boolean isReturnImmediately() {
		return returnImmediately;
	}

	public void setReturnImmediately(boolean returnImmediately) {
		this.returnImmediately = returnImmediately;
	}

	@Override
	public String toString() {
		return "SyncPullClientRequest [maxMessage=" + maxMessage + ", returnImmediately=" + returnImmediately + "]";
	}

}
