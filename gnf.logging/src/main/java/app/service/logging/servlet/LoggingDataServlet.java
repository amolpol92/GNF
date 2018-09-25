package app.service.logging.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import app.service.logging.dao.LoggingDataDao;
import app.service.logging.model.DataTableWrapper;
import app.service.logging.model.LoggingDataModel;

/**
 * @author adarshsinghal
 *
 */
@WebServlet("/get-logging-data")
public class LoggingDataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<LoggingDataModel> loggingData = getLoggingData();

		if (loggingData == null || loggingData.isEmpty()) {
			prepareNoContentResponse(response);
			return;
		}

		prepareJsonResponse(response, loggingData);

	}

	private List<LoggingDataModel> getLoggingData() {
		List<LoggingDataModel> loggingData = null;

		try {
			LoggingDataDao dao = new LoggingDataDao();
			loggingData = dao.getLoggingData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return loggingData;
	}

	/**
	 * @param response
	 * @param publishers
	 * @throws JsonProcessingException
	 * @throws IOException
	 */
	protected void prepareJsonResponse(HttpServletResponse response, List<LoggingDataModel> publishers)
			throws JsonProcessingException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		DataTableWrapper wrapper = new DataTableWrapper(publishers);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String json = gson.toJson(wrapper);
		PrintWriter out = response.getWriter();
		out.print(json);
		out.flush();
	}

	/**
	 * @param response
	 * @throws IOException
	 */
	protected void prepareNoContentResponse(HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.setStatus(204); // No content found
		PrintWriter out = response.getWriter();
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("data", "[]");
		out.print(jsonObject);
		out.flush();
	}

}
