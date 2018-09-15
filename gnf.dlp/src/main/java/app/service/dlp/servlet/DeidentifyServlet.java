package app.service.dlp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;

import app.service.dlp.DLPService;

/**
 * Servlet implementation class DeidentifyServlet
 */
@WebServlet("/deidentify")
public class DeidentifyServlet extends DLPServlet {
	private static final long serialVersionUID = 1L;

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
		String inputJson = getInputData(request);
		String inputMessage = getInputMessage(inputJson);

		DLPService DLPService = new DLPService();
		String deidentifiedRes = DLPService.getDeidentifiedString(inputMessage);

		PrintWriter out = response.getWriter();
		JsonObject responseJsonObj = new JsonObject();
		responseJsonObj.addProperty("message", deidentifiedRes);
		out.println(responseJsonObj);

	}

}
