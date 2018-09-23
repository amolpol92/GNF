package app.service.model;

/**
 * @author adarshsinghal
 *
 */
public class NotifyRequest {

	private int sourceAuthLevel;
	private int groupId;
	private String message;

	public NotifyRequest() {
	}

	public NotifyRequest(int sourceAuthLevel, int groupId, String message) {
		super();
		this.sourceAuthLevel = sourceAuthLevel;
		this.groupId = groupId;
		this.message = message;
	}

	public int getSourceAuthLevel() {
		return sourceAuthLevel;
	}

	public void setSourceAuthLevel(int sourceAuthLevel) {
		this.sourceAuthLevel = sourceAuthLevel;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "NotifyRequest [sourceAuthLevel=" + sourceAuthLevel + ", groupId=" + groupId + ", message=" + message
				+ "]";
	}

}
