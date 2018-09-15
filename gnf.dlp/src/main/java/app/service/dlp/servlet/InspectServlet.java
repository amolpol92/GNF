package app.service.dlp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import app.service.dlp.DLPService;
import app.service.dlp.model.InspectResult;

@WebServlet(name = "Scan Data", urlPatterns = { "/inspect" })
public class InspectServlet extends DLPServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		response.setContentType("application/json");
		String inputJson = getInputData(request);
		String inputMessage = getInputMessage(inputJson);

		DLPService DLPService = new DLPService();
		List<InspectResult> inspectResList = DLPService.getInspectionResult(inputMessage);

		if (inspectResList == null || inspectResList.isEmpty()) {
			prepareNoDataResponse(response);
			return;
		}

		else {
			returnResponseJson(response, inspectResList);
		}
	}

	private void prepareNoDataResponse(HttpServletResponse response) throws IOException {
		response.setCharacterEncoding("UTF-8");
		response.setStatus(204);
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty("data", "[]");
		PrintWriter out = response.getWriter();
		out.print(jsonObject);
		out.flush();
	}

	private void returnResponseJson(HttpServletResponse response, List<InspectResult> inspectResList)
			throws IOException {
		PrintWriter out = response.getWriter();
		Gson gson = new GsonBuilder().create();
		JsonArray inspectResults = gson.toJsonTree(inspectResList).getAsJsonArray();
		out.println(inspectResults);
	}
}