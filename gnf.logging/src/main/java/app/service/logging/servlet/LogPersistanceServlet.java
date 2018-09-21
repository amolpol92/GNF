package app.service.logging.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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
		String inputJson = req.getReader().lines().collect(Collectors.joining());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		LogRequest logRequest = gson.fromJson(inputJson, LogRequest.class);

		// call the code here to enter into Database
		new DatabasePersistOp(logRequest).persistIntoDatabase();
	}
}
