package app.service.authorization;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import app.service.authorization.constants.Constants;
import app.service.authorization.exception.NoSuchGroupException;
import app.service.authorization.model.AuthorizationRequest;
import app.service.authorization.model.LogRequest;
import app.service.authorization.model.UserGroupDetails;


/**
 * @author AdarshSinghal
 *
 */
public class UserGroupDetailsDAO {

	private Connection connection;

	public UserGroupDetailsDAO() throws SQLException {
		DBConnectionProvider connProvider = new DBConnectionProvider();
		connection = connProvider.getConnection();
	}

	@Override
	protected void finalize() throws Throwable {
		if (connection != null)
			connection.close();
	}

	/**
	 * Determine Group authorization level for the group.
	 * 
	 * @param groupId
	 * @return group auth level : int
	 * @throws SQLException
	 * @throws NoSuchGroupException
	 * @throws  
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	public int getAuthLevel(AuthorizationRequest authorizationRequest) throws SQLException, NoSuchGroupException, ClientProtocolException, IOException {
		String sql = "SELECT group_auth_level FROM user_group_details WHERE group_id = ?";

		PreparedStatement ps = connection.prepareStatement(sql);
		ps.setInt(1, Integer.parseInt(authorizationRequest.getGroupId()));

		ResultSet rs = ps.executeQuery();
		int grpAuthLevel = -1;

		if (rs.next()) {
			grpAuthLevel = rs.getInt("group_auth_level");
		} else {
			
			String message = "Terminating transaction. Invalid Group Id";
			
			LogRequest logRequest = new LogRequest(message, "WARNING", "gae_app", "AuthorizationService");
			Map<String, String> labels = getLabels(authorizationRequest);
			logRequest.setLabels(labels);
			
			
			LogServiceClient.getLogger().log(logRequest);
			throw new NoSuchGroupException();
		}
		rs.close();
		ps.close();
		return grpAuthLevel;
	}

	/**
	 * Execute select query on user_group_details table
	 * 
	 * @return List&lt;UserGroupDetails&gt;
	 * @throws SQLException
	 */
	public List<UserGroupDetails> getUserGroupDetails() throws SQLException {
		List<UserGroupDetails> userGroupDetailsList = new ArrayList<>();

		String sql = "SELECT *FROM user_group_details";

		PreparedStatement ps = connection.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			UserGroupDetails userGroupDetails = getUserGroupDetailsFromDb(rs);
			userGroupDetailsList.add(userGroupDetails);
		}

		return userGroupDetailsList;
	}

	/**
	 * Populate values into POJO from resultset
	 * 
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private UserGroupDetails getUserGroupDetailsFromDb(ResultSet rs) throws SQLException {

		UserGroupDetails userGroupDetails = new UserGroupDetails();

		userGroupDetails.setGroupId(rs.getInt("group_id"));
		userGroupDetails.setGroupName(rs.getString("group_name"));
		userGroupDetails.setGroupAuthLevel(rs.getInt("group_auth_level"));

		return userGroupDetails;
	}
	
	private Map<String, String> getLabels(AuthorizationRequest authorizationRequest) {
		Map<String, String> labels = new HashMap<>();
		labels.put(Constants.GB_TXN_ID_KEY, authorizationRequest.getGlobalTxnId());
		String srcAuthLvlStr = String.valueOf(authorizationRequest.getSourceAuthLevel());
		labels.put("Source Authorization Level", srcAuthLvlStr);
		labels.put("Target Group Id", authorizationRequest.getGroupId());
		return labels;
	}

}
