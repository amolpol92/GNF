package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import app.model.MessageStatusListenerSO;

public class MessageStatusListenerDao {

	private Connection connection;

	public MessageStatusListenerDao() throws SQLException, ClassNotFoundException {
		DBConnectionProvider connProvider = new DBConnectionProvider();
		this.connection = connProvider.getConnection();
	}

	public void statusListener(MessageStatusListenerSO messageDetails) throws SQLException {
		String sql = "INSERT INTO message_status_db "
				+ "(global_txn_id,provider_msg_id,status,timestamp,receiver_id,attempt,provider_id,source_id,comments,target_id)"
				+ "VALUES (?,?,?,?,?,?,?,?,?,?)";
	
		Timestamp currentTimestamp = new Timestamp(new Date().getTime());
	
		try (PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setString(1, messageDetails.getGlobal_txn_id());
			ps.setString(2, messageDetails.getProvider_msg_id());
			ps.setString(3, messageDetails.getStatus());
			ps.setTimestamp(4, currentTimestamp);
			ps.setString(5, messageDetails.getReceiver_id());
			ps.setString(6, messageDetails.getAttempt());
			ps.setString(7, messageDetails.getProvider_id());
			ps.setString(8, messageDetails.getSource_id());
			ps.setString(9, messageDetails.getComments());
			ps.setString(10, messageDetails.getTarget_id());

			ps.executeUpdate();
		}
	}

	public String getGlobalTranId(String providerMessageId) throws SQLException {
		String sql = "select global_txn_id from message_status_db where provider_msg_id =?";
		String global_txn_id = null;
		try (PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setString(1, providerMessageId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				global_txn_id = rs.getString("global_txn_id");
			} else {
				throw new SQLException("No global_txn_id found");
			}
		}
		return global_txn_id;
	}

	public MessageStatusListenerSO getMessageDetailsFromStore(String globalTranId) throws SQLException {
		String sql = "select global_txn_id,message_data,store_timestamp,source_id,provider_id,target_id,retry_counter from message_cache where global_txn_id =?";

		MessageStatusListenerSO messageCacheDetails = null;
		try (PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setString(1, globalTranId);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				messageCacheDetails = new MessageStatusListenerSO();
				messageCacheDetails.setGlobal_txn_id(rs.getString("global_txn_id"));
				messageCacheDetails.setMessage_data(rs.getString("message_data"));
				messageCacheDetails.setTimestamp(rs.getTimestamp("store_timestamp") + "");
				messageCacheDetails.setReceiver_id(rs.getString("source_id"));
				messageCacheDetails.setProvider_id(rs.getString("provider_id"));
				messageCacheDetails.setTarget_id(rs.getString("target_id"));
				messageCacheDetails.setRetry_counter(rs.getString("retry_counter"));
				// UserMessageSO userMessage
				// global_txn_id = rs.getString("group_auth_level");
			} else {
				throw new SQLException("No details found for global_txn_id ");
			}
		}
		return messageCacheDetails;
	}

	public void deleteFromMessageStore(String globalTranId) throws SQLException {
		String sql = "delete from message_cache where global_txn_id =?";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setString(1, globalTranId);

			ps.executeUpdate();
		}
	}

	public void statusCacheListener(MessageStatusListenerSO messageDetails) throws SQLException {
		String sql = "INSERT INTO message_cache "
				+ "(global_txn_id,message_data,store_timestamp,source_id,provider_id,target_id,retry_counter)"
				+ "VALUES (?,?,?,?,?,?,?)";
		Timestamp currentTimestamp = new Timestamp(new Date().getTime());
		/*
		 * try { Timestamp timestamp =
		 * DateUtility.getTimestamp(messageDetails.getTimestamp()); ts = timestamp !=
		 * null ? timestamp : currentTimestamp; } catch (ParseException e) {
		 * 
		 * e.printStackTrace(); }
		 */

		try (PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setString(1, messageDetails.getGlobal_txn_id());
			ps.setString(2, messageDetails.getMessage_data());
			ps.setTimestamp(3, currentTimestamp);
			ps.setString(4, messageDetails.getSource_id());
			ps.setString(5, messageDetails.getProvider_id());
			ps.setString(6, messageDetails.getTarget_id());
			ps.setInt(7, Integer.valueOf(messageDetails.getRetry_counter()));
			ps.executeUpdate();
		}
	}

	public void updateRetryCounter(String globalTranId) throws SQLException {
		String sql = "update  message_cache set retry_counter= retry_counter+1 where global_txn_id=?";

		try (PreparedStatement ps = connection.prepareStatement(sql)) {

			ps.setString(1, globalTranId);
			ps.executeUpdate();
		}
	}
}
