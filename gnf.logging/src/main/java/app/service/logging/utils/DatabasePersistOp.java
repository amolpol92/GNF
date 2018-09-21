package app.service.logging.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import app.service.logging.model.LogRequest;
import app.service.logging.utils.ConfigParams;
import app.service.logging.utils.PropertyParserAndConfigAdapter;

public class DatabasePersistOp {
	private PropertyParserAndConfigAdapter adapter;
	private ConfigParams params;
	private LogRequest logDataContainer;

	public DatabasePersistOp(LogRequest logRequest) {
		// TODO Auto-generated constructor stub
		this.logDataContainer = logRequest;
		this.adapter = new PropertyParserAndConfigAdapter("logging_configuration.properties");
		this.params = this.adapter.readPropertiesAndSetParameters();
	}

	public void persistIntoDatabase() {
		//try with resources; Connection object
		try(Connection connection = this.params.getConn()) {
			System.out.println(connection.getAutoCommit());
			String sqlQueryString = "insert into log_details (glo_tran_id, severity, mntrd_res_typ, log_msg, log_name) VALUES (?, ?, ?, ?, ?)";
			PreparedStatement statement = connection.prepareStatement(sqlQueryString);
			statement.setString(1, this.logDataContainer.getLabels().get("GlobalTxnId"));
			statement.setString(2, this.logDataContainer.getSeverity());
			statement.setString(3, this.logDataContainer.getMonitoredResource());
			statement.setString(4, this.logDataContainer.getMessage());
			statement.setString(5, this.logDataContainer.getLogName());
			statement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
