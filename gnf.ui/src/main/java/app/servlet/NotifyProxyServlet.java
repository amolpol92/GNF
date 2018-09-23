package app.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import app.service.model.NotifyRequest;
import app.service.model.NotifyResponse;
import app.service.model.PublishedMessage;
import app.service.utils.HttpPostClient;

/**
 * Servlet implementation class NotifyProxyServlet
 */
@WebServlet("/notify")
public class NotifyProxyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String jsonResponse = getHttpResponseJson(request);

		Gson gson = new Gson();
		NotifyResponse notifyResponse = gson.fromJson(jsonResponse, NotifyResponse.class);

		List<PublishedMessage> publishedMessages = notifyResponse.getPublishedMessages();

		request.setAttribute("isExceptionOccured", notifyResponse.isException());

		if (publishedMessages != null && !publishedMessages.isEmpty()) {
			request.setAttribute("gbTxnId", publishedMessages.get(0).getGlobalTransactionId());
			request.setAttribute("messageIds", publishedMessages);

			request.getRequestDispatcher("/results/success.jsp").forward(request, response);
		}

		else {
			System.out.println(notifyResponse.getMessage());
			request.setAttribute("exceptionMsg", notifyResponse.getMessage());
			request.getRequestDispatcher("/").forward(request, response);
		}

	}

	private String getHttpResponseJson(HttpServletRequest request) throws IOException {
		NotifyRequest entity = prepareHttpRequestEntity(request);
		String url = "https://possible-haven-212003.appspot.com/notify";
		String jsonResponse = HttpPostClient.send(url, entity);
		return jsonResponse;
	}

	private NotifyRequest prepareHttpRequestEntity(HttpServletRequest request) {
		int srcAuthLvl = Integer.parseInt(request.getParameter("src-auth-level"));
		int groupId = Integer.parseInt(request.getParameter("group-id"));
		String message = request.getParameter("message");

		NotifyRequest notifyRequest = new NotifyRequest(srcAuthLvl, groupId, message);
		return notifyRequest;
	}

}
