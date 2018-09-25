package app.servlet;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

import app.constants.Constants;
import app.model.PublishRequest;
import app.model.PublishResponse;
import app.model.PublisherMessage;
import app.service.NotifyService;
import app.util.ListUtils;

/**
 * Responsible for publishing messages on topics
 * 
 * @author AdarshSinghal
 *
 */
@WebServlet(name = "publish", urlPatterns = { "/topic/publish" })

public class PublishServlet extends HttpServlet {

	private static final long serialVersionUID = -9098430818560246450L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		String inputJson = req.getReader().lines().collect(Collectors.joining());

		PublishRequest publishRequest = gson.fromJson(inputJson, PublishRequest.class);

		String topicName = publishRequest.getTopic();
		String message = publishRequest.getMessage();

		if (topicName == null || message == null) {
			resp.sendError(400);
			return;
		}

		List<String> topics = ListUtils.getListFromCSV(topicName);

		String gbTxnId = "g" + new Date().getTime() + "r" + (int) (Math.random() * 100);

		ByteString data = ByteString.copyFromUtf8(message);
		PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data)
				.putAttributes(Constants.GB_TXN_ID_KEY, gbTxnId).build();
		NotifyService notifyService = new NotifyService();

		Map<String, String> labels = new HashMap<>();
		labels.put(Constants.GB_TXN_ID_KEY, gbTxnId);

		List<PublisherMessage> messageIds = notifyService.publishMessage(topics, pubsubMessage, labels);

		PublishResponse publishResponse = new PublishResponse(gbTxnId, messageIds);
		String json = gson.toJson(publishResponse);

		resp.setContentType("application/json");
		resp.getWriter().println(json);

	}

}
