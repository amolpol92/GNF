package app.servlet;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.api.client.json.JsonParser;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.pubsub.model.PubsubMessage;

import app.constants.Constants;
import app.dao.MessageStatusListenerDao;
import app.model.MessageStatusListenerSO;
import app.service.messagestatus.NotifyToMessageStatusService;

/**
 * @author Aniruddha,amol
 * @Desciption The Notify to Message status service is intended to be used as a
 *             push endpoint service from Google Pub/Sub. The service acts as an
 *             interface between pubsub and "message_status_cache_db". The
 *             servlet after receiving the data feeds the data into the
 *             respective columns of message_status_cache_db.
 * @urlPattern /notifyService
 */
@WebServlet(name = "NotifyToMessageStatusService", urlPatterns = { "/notifyServicetoStatDb", "/notifyService" })
public class NotifyToMessageStatusServlet extends HttpServlet {

	private static final long serialVersionUID = 360024119674491022L;

	@Override
	public final void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {

		ServletInputStream inputStream = req.getInputStream();
		JsonParser parser = JacksonFactory.getDefaultInstance().createJsonParser(inputStream);
		parser.skipToKey("message");
		PubsubMessage message = parser.parseAndClose(PubsubMessage.class);
		//NotifyToMessageStatusService statusService = new NotifyToMessageStatusService();
		persistInDb(resp, message);
	}

	/**
	 * @param resp
	 * @param message
	 */
	private void persistInDb(final HttpServletResponse resp, PubsubMessage message) {
		MessageStatusListenerDao dao;
		try {
			dao = new MessageStatusListenerDao();
			MessageStatusListenerSO listenerSO = new MessageStatusListenerSO(
					message.getAttributes().get(Constants.GB_TXN_ID_KEY), "PublishedInGNF", message.getPublishTime(),
					null, null);
			dao.statusListener(listenerSO);
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch (SQLException e) {
			e.printStackTrace();
			resp.setStatus(HttpServletResponse.SC_EXPECTATION_FAILED);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
