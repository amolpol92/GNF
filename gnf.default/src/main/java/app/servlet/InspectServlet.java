package app.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import app.constants.ConstantsURL;
import app.model.InspectResult;
import app.model.MessageWrapper;

@WebServlet(name = "Scan Data", urlPatterns = { "/inspect" })
public class InspectServlet extends DLPServlet {

	private static final long serialVersionUID = 1L;

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse res) throws IOException, ServletException {

		res.setContentType("application/json");
		String inputJson = getInputData(request);
		String inputMessage = getInputMessage(inputJson);

		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(ConstantsURL.DLP_INSPECT_URL);

		ObjectMapper mapper = new ObjectMapper();
		String requestJson = mapper.writeValueAsString(new MessageWrapper(inputMessage));
		StringEntity entity = new StringEntity(requestJson);
		httppost.setEntity(entity);

		HttpResponse response = httpclient.execute(httppost);

		HttpEntity httpEntity = response.getEntity();
		if (httpEntity == null || httpEntity.getContent() == null) {
			res.getWriter().print(Collections.emptyList());
		}

		InputStream content = httpEntity.getContent();
		List<InspectResult> inspectionResult = mapper.readValue(content, new TypeReference<List<InspectResult>>() {
		});

		if (inspectionResult == null || inspectionResult.isEmpty()) {
			prepareNoDataResponse(res);
			return;
		}

		else {
			returnResponseJson(res, inspectionResult);
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