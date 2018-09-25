package app.service.logging.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import app.service.logging.Constants;
import app.service.logging.model.LogRequest;

public class DatabasePersistOp {
	private Connection connection;

	public DatabasePersistOp() throws SQLException {
		DBConnectionProvider connProvider = new DBConnectionProvider();
		this.connection = connProvider.getConnection();
			}

	public void persistIntoDatabase(LogRequest logRequest) {
		//try with resources; Connection object
		String sqlQueryString = "insert into log_details (glo_tran_id, severity, mntrd_res_typ, log_msg, log_name) VALUES (?, ?, ?, ?, ?)";
		try(PreparedStatement statement = connection.prepareStatement(sqlQueryString)) {
			
			statement.setString(1, logRequest.getLabels().get(Constants.GB_TXN_ID_KEY));
			statement.setString(2, logRequest.getSeverity());
			statement.setString(3, logRequest.getMonitoredResource());
			statement.setString(4, logRequest.getMessage());
			statement.setString(5, logRequest.getLogName());
			statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
