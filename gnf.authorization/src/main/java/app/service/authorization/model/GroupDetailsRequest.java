package app.service.authorization.model;

/**
 * @author adarshsinghal695
 *
 */
public class GroupDetailsRequest {

	private String groupId;
	
	public GroupDetailsRequest() {}

	public GroupDetailsRequest(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "GroupDetailsRequest [groupId=" + groupId + "]";
	}
	
	
	
	
}
