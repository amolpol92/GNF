package app.servlet;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.pubsub.model.PubsubMessage;

import app.model.LogRequest;
import app.service.client.LogServiceClient;
import app.service.thirdparty.TwilioSmsNotifier;

/**
 * @author Amol this servlet responsible for listening messages pushed on twilio
 *         topic and invoke twilio notifier
 */
@WebServlet(name = "TwilioEndpointService", urlPatterns = { "/twilioService" })
public class TwilioEndpointService extends HttpServlet {

	private static final long serialVersionUID = 360024119674491022L;

	@Override
	public final void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		ServletInputStream inputStream = req.getInputStream();
		JsonParser parser = JacksonFactory.getDefaultInstance().createJsonParser(inputStream);
		parser.skipToKey("message");
		PubsubMessage message = parser.parseAndClose(PubsubMessage.class);

		invokeNotifier(message);

		LogRequest logRequest = new LogRequest("Sent SMS through Twilio", "INFO", "gae_app", "NotificationService");
		logRequest.setLabels(message.getAttributes());
		LogServiceClient.getLogger().log(logRequest);
	}

	/**
	 * @param message
	 * @throws IOException
	 */
	private void invokeNotifier(PubsubMessage message) throws IOException {
		TwilioSmsNotifier notifier = new TwilioSmsNotifier();
		int maxRetryCount=3;
		notifier.notifyUserBySMS(message,maxRetryCount);
	}
}
