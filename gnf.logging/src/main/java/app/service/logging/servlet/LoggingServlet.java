package app.service.logging.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.service.logging.CloudLogger;
import app.service.logging.model.LogRequest;
import app.service.logging.model.MonitoredResourceModel;
import app.service.logging.model.SourceLocationModel;

/**
 * Servlet implementation class LoggingServlet
 */
@WebServlet("/log")
public class LoggingServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoggingServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String inputJson = request.getReader().lines().collect(Collectors.joining());

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		LogRequest logRequest = gson.fromJson(inputJson, LogRequest.class);
		CloudLogger logger = CloudLogger.getLogger();
		try {
			logger.log(logRequest);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(400);
			response.getWriter().print("{\n  \"status\":\""+e.getMessage()+"\"\n}");
			return;
		}
		response.getWriter().print("{\n  \"status\":\"success\"\n}");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		PrintWriter pw = resp.getWriter();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		Map<String, String> labels = new HashMap<String, String>() {
			private static final long serialVersionUID = -7636907369426480716L;

			{
				put("module_id", "default-service");
				put("project_id", "project-212003");
				put("version_id", "1.0.0");
			}
		};

		MonitoredResourceModel monitoredResource = new MonitoredResourceModel("global", labels);

		SourceLocationModel source = new SourceLocationModel("LoggingServlet.java", 20l,
				"app.service.logging.servlet.LoggingServlet.doGet(...)");

		String messsage = "GET is not supported. Use POST method. The request body should be in this format.";
		LogRequest logRequest = new LogRequest(messsage, "INFO", monitoredResource, "MyLogger");
		logRequest.setSourceLocation(source);

		Map<String, String> labelFromKey = new HashMap<String, String>() {

			private static final long serialVersionUID = -1684323781189821272L;

			{
				put("User defined key", "User defined value");
			}

		};

		logRequest.setLabels(labelFromKey);
		logRequest.setSourceLocation(new SourceLocationModel("LoggingServlet.java", 20l,"app.service.logging.servlet.LoggingServlet.doGet(...)"));

		String json = gson.toJson(logRequest);
		pw.println(json);
		pw.close();
	}

}
