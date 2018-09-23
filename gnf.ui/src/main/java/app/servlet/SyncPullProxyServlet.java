package app.servlet;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import app.service.model.PulledMessage;
import app.service.model.SyncPullClientRequest;
import app.service.model.SyncPullClientResponse;
import app.service.utils.HttpPostClient;

/**
 * Servlet implementation class SyncPullProxyServlet
 */
@WebServlet({ "/pullmessage", "/api/pullmessage", "/pullMessages", "/pull-messages" })
public class SyncPullProxyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String JSP_PAGE = "/pages/syncpullclient.jsp";

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String maxMessageStr = request.getParameter("max-message");
		String returnImmediatelyStr = request.getParameter("return-immediately");
		int maxMessage = Integer.parseInt(maxMessageStr);
		boolean returnImmediately = Boolean.valueOf(returnImmediatelyStr);

		SyncPullClientRequest entity = new SyncPullClientRequest(maxMessage, returnImmediately);
		String url = "https://possible-haven-212003.appspot.com/pull-messages";
		String json = HttpPostClient.send(url, entity);

		Gson gson = new Gson();
		SyncPullClientResponse syncPullResponse = gson.fromJson(json, SyncPullClientResponse.class);

		List<PulledMessage> messageList = syncPullResponse.getPulledMessages();

		if (messageList.isEmpty())
			request.setAttribute("noMsg", true);
		else {
			request.setAttribute("messageList", messageList);
		}

		request.getRequestDispatcher(JSP_PAGE).forward(request, response);

	}

}
