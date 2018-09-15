package app.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import app.constants.ConstantsURL;
import app.model.InspectionResultWrapper;

@WebServlet(name = "Scan Data", urlPatterns = { "/inspect" })
public class InspectServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		response.setContentType("application/json");
		String inputJson = request.getReader().lines().collect(Collectors.joining());

		HttpClient httpClient = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost(ConstantsURL.DLP_INSPECT_URL);

		StringEntity entity = new StringEntity(inputJson);
		httpPost.setEntity(entity);

		HttpResponse httpResponse = httpClient.execute(httpPost);
		HttpEntity httpEntity = httpResponse.getEntity();

		if (httpEntity == null) {
			prepareNoDataResponse(response);
			return;
		}

		Gson gson = new GsonBuilder().create();
		String json = EntityUtils.toString(httpEntity);
		InspectionResultWrapper inspectionResult = gson.fromJson(json, InspectionResultWrapper.class);

		returnResponseJson(response, inspectionResult);
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

	private void returnResponseJson(HttpServletResponse response, InspectionResultWrapper inspectionResult)
			throws IOException {
		PrintWriter out = response.getWriter();
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(inspectionResult);
		out.println(json);
	}
}