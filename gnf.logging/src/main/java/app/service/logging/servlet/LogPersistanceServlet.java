package app.service.logging.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.pubsub.model.PubsubMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.service.logging.CloudLogger;
import app.service.logging.model.LogRequest;
import app.service.logging.model.SourceLocationModel;
import app.service.logging.utils.DatabasePersistOp;

/**
 * 
 * @author AniruddhaSinha
 * @description persisting log information in a DB
 *
 */

@WebServlet(name = "LogPersistanceServlet", urlPatterns = { "/logAndPersist", "/persistlog" })
public class LogPersistanceServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8882941744063757864L;

	public LogPersistanceServlet() {
		super();
	}

	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setContentType("application/json");
		PrintWriter pw = resp.getWriter();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		SourceLocationModel source = new SourceLocationModel("LoggingServlet.java", 20l,
				"app.service.logging.servlet.LoggingServlet.doGet(...)");

		String messsage = "GET is not supported. Use POST method. The request body should be in this format.";
		LogRequest logRequest = new LogRequest(messsage, "INFO", "global", "MyLogger");
		logRequest.setSourceLocation(source);

		Map<String, String> labelFromKey = new HashMap<>();
		labelFromKey.put("GlobalTxnId", "gb12345rnd67");

		logRequest.setLabels(labelFromKey);
		logRequest.setSourceLocation(new SourceLocationModel("LoggingServlet.java", 20l,
				"app.service.logging.servlet.LoggingServlet.doGet(...)"));

		String json = gson.toJson(logRequest);
		pw.println(json);
		pw.close();

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		ServletInputStream inputStream = req.getInputStream();
		JsonParser parser = JacksonFactory.getDefaultInstance().createJsonParser(inputStream);
		parser.skipToKey("message");	
		PubsubMessage message = parser.parseAndClose(PubsubMessage.class);
		byte[] decodedMessageData = Base64.getMimeDecoder().decode(message.getData().getBytes());
		String decodedMessage = new String(decodedMessageData);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		LogRequest logRequest = gson.fromJson(decodedMessage, LogRequest.class);

		CloudLogger logger = CloudLogger.getLogger();
		try {
			logger.log(logRequest);
		} catch (Exception e) {
			resp.setStatus(400);
			resp.getWriter().print("{\n  \"status\":\"" + e.getMessage() + "\"\n}");
			return;
		}		
		// call the code here to enter into Database
		persistInDb(logRequest);
		
	}
	
	public void persistInDb(LogRequest logRequest) {
		try {
			DatabasePersistOp dao=new DatabasePersistOp();
			dao.persistIntoDatabase(logRequest);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
