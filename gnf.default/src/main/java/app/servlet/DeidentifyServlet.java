package app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Response;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.model.MessageWrapper;
import app.service.dlp.DLPServiceInvoker;

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

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		MessageWrapper messageWrapper;
		try {
			messageWrapper = gson.fromJson(inputJson, MessageWrapper.class);
		} catch (Exception e) {
			LOGGER.severe(e.getMessage());
			response.setStatus(Response.SC_BAD_REQUEST);
			return;
		}

		DLPServiceInvoker serviceInvoker = new DLPServiceInvoker();
		String deidentifiedRes = serviceInvoker.getDeidentifiedString(messageWrapper.getMessage());
		String json = gson.toJson(new MessageWrapper(deidentifiedRes));

		printJsonResponse(response, json);

	}

	private void printJsonResponse(HttpServletResponse response, String json) throws IOException {
		PrintWriter out = response.getWriter();
		out.println(json);
		out.flush();
	}

}
