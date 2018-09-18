package app.service.logging.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
		logger.log(logRequest);
		response.getWriter().print("{\n  \"status\":\"success\"\n}");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");
		PrintWriter pw = resp.getWriter();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		LogRequest logRequest = new LogRequest("GET is not supported. Use POST method. The request body should be in this format.", "INFO", "GLOBAL", "MyLogger");
		String json = gson.toJson(logRequest);
		pw.println(json);
		pw.close();
	}

}
