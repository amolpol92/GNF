package app.service.logging.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import app.service.logging.model.LoggingDataModel;
import app.service.logging.utils.DBConnectionProvider;

public class LoggingDataDao {

	private Connection connection;

	public LoggingDataDao() throws SQLException {
		DBConnectionProvider connProvider = new DBConnectionProvider();
		this.connection = connProvider.getConnection();
	}

	public List<LoggingDataModel> getLoggingData() throws SQLException {
		List<LoggingDataModel> loggingData = new ArrayList<>();

		String sql = "SELECT *FROM log_details";
		PreparedStatement ps = connection.prepareStatement(sql);

		try (ResultSet rs = ps.executeQuery()) {
			while (rs.next()) {

				LoggingDataModel logDataModel = getLoggingDataModel(rs);

				loggingData.add(logDataModel);
			}
		}
		return loggingData;
	}

	private LoggingDataModel getLoggingDataModel(ResultSet resultSet) throws SQLException {

		String insertId = String.valueOf(resultSet.getInt("id"));
		String globalTxnId = resultSet.getString("glo_tran_id");
		String message = resultSet.getString("log_msg");
		String severity = resultSet.getString("severity");
		String monitoredResource = resultSet.getString("mntrd_res_typ");
		String loggerName = resultSet.getString("log_name");

		LoggingDataModel logDataModel = new LoggingDataModel();
		logDataModel.setInsertId(insertId);
		logDataModel.setGlobalTxnId(globalTxnId==null?"NA":globalTxnId);
		logDataModel.setMessage(message);
		logDataModel.setMonitoredResource(monitoredResource);
		logDataModel.setSeverity(severity);
		logDataModel.setLoggerName(loggerName);

		return logDataModel;
	}

}
