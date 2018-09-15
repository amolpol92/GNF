package app.service.dlp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import app.service.dlp.DLPService;
import app.service.dlp.model.MessageWrapper;

/**
 * Servlet implementation class DeidentifyServlet
 */
@WebServlet("/deidentify")
public class DeidentifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER = Logger.getLogger(DeidentifyServlet.class.getName());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeidentifyServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");
		String inputJson = request.getReader().lines().collect(Collectors.joining());
		Gson gson = new GsonBuilder().create();

		MessageWrapper messageWrapper = new MessageWrapper();
		try {
			messageWrapper = gson.fromJson(inputJson, MessageWrapper.class);
		} catch (Exception e) {
			LOGGER.info("Input Json -> " + inputJson + " \n" + e.getMessage());
		}
		String inputMessage = messageWrapper.getMessage();

		if (inputMessage.isEmpty()) {
			setResponse(response, "");
			return;
		}

		DLPService DLPService = new DLPService();
		String deidentifiedRes = DLPService.getDeidentifiedString(inputMessage);

		setResponse(response, deidentifiedRes);

	}

	private void setResponse(HttpServletResponse response, String deidentifiedRes) throws IOException {
		PrintWriter out = response.getWriter();
		JsonObject responseJsonObj = new JsonObject();
		responseJsonObj.addProperty("message", deidentifiedRes);
		out.println(responseJsonObj);
		out.flush();
	}

}
