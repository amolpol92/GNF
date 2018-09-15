package app.servlet;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class DLPServlet extends HttpServlet {

	private static final long serialVersionUID = 9036573446532048799L;

	/**
	 * The Json should be in format {"message":"{{message}}"}
	 * 
	 * @param inputJson
	 * @return
	 */
	protected String getInputMessage(String inputJson) {
		Gson gson = new GsonBuilder().create();
		System.out.println("Input JSON: "+inputJson);
		JsonPrimitive jsonPrimitive = (JsonPrimitive) gson.fromJson(inputJson, JsonObject.class).get("message");

		String inputMessage = jsonPrimitive.getAsString();
		return inputMessage;
	}

	protected String getInputData(HttpServletRequest req) throws IOException {
		BufferedReader d = req.getReader();
		StringBuilder inputData = new StringBuilder();

		String data = "";
		while ((data = d.readLine()) != null) {
			inputData.append(data);
		}
		return inputData.toString();
	}

}
