package app.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.constants.Constants;
import app.model.MessageStatus;
import app.model.MessageStatusCacheField;

/**
 * @author Aniruddha
 * @Description: This Data Access Object will be used by the Logging Table in
 *               the UI. This will simply fetch details from
 *               message_status_cache_db and show the data in the UI
 *
 */
public class MessageStatusDAO {

	private Connection connection;

	public MessageStatusDAO() throws SQLException {
		DBConnectionProvider connProvider = new DBConnectionProvider();
		connection = connProvider.getConnection();
	}

	/**
	 * @param message
	 * @throws IOException
	 * @throws SQLException
	 */
	public void insertIntoTable(MessageStatus message) throws IOException, SQLException {

		final String query = "UPDATE message_status_cache_db SET dlv_rprt = ?  WHERE glo_tran_id=?";
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(1, getDeliveryStatus(message));
		statement.setString(2, message.getMessageId());
		statement.execute();

	}

	/**
	 * @param message
	 * @return deliveryStatus:String
	 */
	private String getDeliveryStatus(MessageStatus message) {
		String deliveryFlag = message.getDeliveryFlag();
		boolean isDelivered = deliveryFlag != null && deliveryFlag.equalsIgnoreCase(Constants.TRUE);
		String deliveryStatus = Constants.IN_PROGRESS;
		if (isDelivered)
			deliveryStatus = Constants.DELIVERED;
		return deliveryStatus;
	}

	public List<MessageStatusCacheField> getAllFieldDetails() throws SQLException {
		MessageStatusCacheField messageStatusDetails;
		List<MessageStatusCacheField> messageEntryList = new ArrayList<>();
		final String sql = "SELECT * FROM message_status_db";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet rs = preparedStatement.executeQuery();
		while (rs.next()) {
			messageStatusDetails = getTableDetails(rs);
			messageEntryList.add(messageStatusDetails);
		}

		return messageEntryList;
	}

	public MessageStatusCacheField getTableDetails(ResultSet rs) throws SQLException {
		MessageStatusCacheField messageStatusDetails;
		messageStatusDetails = new MessageStatusCacheField();
		messageStatusDetails.setGlobal_txn_id(rs.getString("global_txn_id"));
		messageStatusDetails.setProvider_msg_id(rs.getString("provider_msg_id"));
		messageStatusDetails.setTimestamp(rs.getTimestamp("timestamp") + "");
		messageStatusDetails.setReceiver_id(rs.getString("receiver_id"));
		messageStatusDetails.setProvider_id(rs.getString("provider_id"));
		messageStatusDetails.setAttempt(rs.getString("attempt"));
		messageStatusDetails.setSource_id(rs.getString("source_id"));
		messageStatusDetails.setTarget_id(rs.getString("target_id"));
		messageStatusDetails.setComments(rs.getString("comments"));
		messageStatusDetails.setStatus(rs.getString("status"));
		return messageStatusDetails;

	}

}