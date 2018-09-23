package app.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import app.util.ExternalProperties;

/**
 * @author adarshsinghal
 *
 */
@WebServlet("/api/topic/list")
public class GetTopicListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String topics = ExternalProperties.getAppConfig("app.gc.pubsub.topic.layer1");

		Gson gson = new GsonBuilder().create();
		JsonElement topicJsonArr = gson.toJsonTree(topics.split(","));
		JsonObject topicListJson = new JsonObject();
		topicListJson.add("topics", topicJsonArr);

		resp.setContentType("application/json");
		PrintWriter pw = resp.getWriter();
		pw.print(topicListJson);
	}

}
