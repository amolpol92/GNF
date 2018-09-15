package app.service.dlp.servlet;

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

import app.service.dlp.DLPService;
import app.service.dlp.model.InspectionResultWrapper;
import app.service.dlp.model.MessageWrapper;

@WebServlet(name = "Scan Data", urlPatterns = { "/inspect" })
public class InspectServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		response.setContentType("application/json");

		String inputJson = request.getReader().lines().collect(Collectors.joining());
		Gson gson = new GsonBuilder().create();
		MessageWrapper messageWrapper = gson.fromJson(inputJson, MessageWrapper.class);
		String inputMessage = messageWrapper.getMessage();

		if (inputMessage.isEmpty()) {
			setResponseJson(response, new InspectionResultWrapper());
			return;
		}

		DLPService dlpService = new DLPService();
		InspectionResultWrapper inspectionResult = dlpService.inspect(inputMessage);

		if (inspectionResult.getInspectResults().isEmpty()) {
			setResponseJson(response, new InspectionResultWrapper());
			return;
		}

		else {
			setResponseJson(response, inspectionResult);
		}
	}

	private void setResponseJson(HttpServletResponse response, InspectionResultWrapper inspectResult)
			throws IOException {
		PrintWriter out = response.getWriter();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(inspectResult);
		out.println(json);
	}
}