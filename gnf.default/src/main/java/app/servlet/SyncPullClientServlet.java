package app.servlet;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.model.SubscriberMessage;
import app.model.SyncPullClientRequest;
import app.model.SyncPullClientResponse;
import app.service.NotificationService;

/**
 * @author AdarshSinghal
 *
 */
@WebServlet({ "/pullmessage", "/api/pullmessage", "/pullMessages", "/pull-messages" })
public class SyncPullClientServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SyncPullClientServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String inputJson = request.getReader().lines().collect(Collectors.joining());

		Gson gson = new Gson();
		SyncPullClientRequest reqObj = gson.fromJson(inputJson, SyncPullClientRequest.class);

		if (reqObj.getMaxMessage() < 1) {
			response.sendError(400);
			return;
		}

		NotificationService notificationSvc = new NotificationService();
		List<SubscriberMessage> messageList = notificationSvc.pullMessages(reqObj.getMaxMessage(),
				reqObj.isReturnImmediately());

		notificationSvc.sendMessagesToUser(messageList);

		prepareJsonResponse(response, messageList);

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		SyncPullClientRequest request = new SyncPullClientRequest(1, true);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(request);
		resp.getWriter().print(json);

	}

	private void prepareJsonResponse(HttpServletResponse response, List<SubscriberMessage> messageList)
			throws IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		response.setContentType("application/json");
		SyncPullClientResponse syncPullClientResponse = new SyncPullClientResponse(messageList);
		String json = gson.toJson(syncPullClientResponse);
		response.getWriter().print(json);
	}

}
