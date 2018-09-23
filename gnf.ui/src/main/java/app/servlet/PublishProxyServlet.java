package app.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.service.model.PublishRequest;
import app.service.model.PublishResponse;
import app.service.model.PublishedMessage;
import app.service.utils.HttpPostClient;

/**
 * Servlet implementation class PublishProxyServlet
 */
@WebServlet("/topic/publish")
public class PublishProxyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String topicName = request.getParameter("topic-name");
		String message = request.getParameter("message");

		PublishRequest entity = new PublishRequest(topicName, message);
		String url = "http://possible-haven-212003.appspot.com/topic/publish";
		String json = HttpPostClient.send(url, entity);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		PublishResponse publishResponse = gson.fromJson(json, PublishResponse.class);

		List<PublishedMessage> publishedMessages = publishResponse.getPublishedMessage();
		if (publishedMessages != null && !publishedMessages.isEmpty()) {
			request.setAttribute("gbTxnId", publishedMessages.get(0).getGlobalTransactionId());
			request.setAttribute("messageIds", publishedMessages);

			request.getRequestDispatcher("/results/success.jsp").forward(request, response);
		}

		else {
			request.getRequestDispatcher("results/failure.jsp").forward(request, response);
		}

	}

}
