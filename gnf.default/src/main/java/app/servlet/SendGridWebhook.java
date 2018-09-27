package app.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.Collection;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import app.dao.MessageStatusListenerDao;
import app.model.DeliveryStatus;
import app.model.MessageStatusListenerSO;
import app.service.ProviderMsgPublisher;
import app.service.RetryMsgPublisher;

/**
 * @author Amol this servlet responsible for listening messages pushed on
 *         Sendgrid topic and invoke sendgrid notifier
 */
@WebServlet(name = "SendGridWebhook", urlPatterns = { "/sendgridwebhook" })
public class SendGridWebhook extends HttpServlet {

	private static final long serialVersionUID = 360024119674491022L;

	@Override
	public final void doPost(final HttpServletRequest req, final HttpServletResponse resp) throws IOException {
		resp.setStatus(HttpServletResponse.SC_OK);
		String inputJson = getInputData(req);
		Gson g = new Gson();
		System.out.println("inputJson " + inputJson);
		// DeliveryStatus DeliveryStatus = g.fromJson(inputJson,
		// DeliveryStatus.class);
		Type collectionType = new TypeToken<Collection<DeliveryStatus>>() {
		}.getType();
		Collection<DeliveryStatus> enums = g.fromJson(inputJson, collectionType);

		System.out.println(enums);

		enums.forEach((statusData) -> checkStatusAndPersistInDb(statusData));

	}

	public void checkStatusAndPersistInDb(DeliveryStatus message) {
		MessageStatusListenerDao dao;
		String globalTxnId = null;
		String sendgridMessageId = null;
		try {
			/*
			 * EmailNotifeirStatusLogDAO dao; dao = new
			 * EmailNotifeirStatusLogDAO(); dao.insertWebhookDetails(message);
			 */
			sendgridMessageId = message.getSg_message_id().split(".")[0];
			dao = new MessageStatusListenerDao();
			globalTxnId = dao.getGlobalTranId(sendgridMessageId);

			MessageStatusListenerSO listenerSO = prepareStatusListenerMessage(message, sendgridMessageId,
					globalTxnId);

			dao.statusListener(listenerSO);

		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		retryHandler(message, globalTxnId);
		successHandler(message, globalTxnId);
	}

	/**
	 * @param message
	 * @param globalTxnId
	 */
	private void successHandler(DeliveryStatus message, String globalTxnId) {
		MessageStatusListenerDao dao;
		if (message.getEvent().equalsIgnoreCase("delivered")) {

			try {
				dao = new MessageStatusListenerDao();
				dao.deleteFromMessageStore(globalTxnId);
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// TODO Remove delivered message from message store
		}
	}

	/**
	 * @param message
	 * @param globl_tran_id
	 */
	private void retryHandler(DeliveryStatus message, String globl_tran_id) {
		MessageStatusListenerDao dao;
		if (message.getEvent().equalsIgnoreCase("bounce") || message.getEvent().equalsIgnoreCase("dropped")) {

			try {
				dao = new MessageStatusListenerDao();
				dao.updateRetryCounter(globl_tran_id);
				MessageStatusListenerSO messageCacheDetails=dao.getMessageDetailsFromStore(globl_tran_id);

				RetryMsgPublisher publisher = new RetryMsgPublisher();
				publisher.publishMessageOnFailure(messageCacheDetails);
				// publisher.publishMessage(publishMessage);
			} catch (SQLException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param message
	 * @param sendgridMessageId
	 * @param globl_tran_id
	 * @return
	 */
	private MessageStatusListenerSO prepareStatusListenerMessage(DeliveryStatus message, String sendgridMessageId,
			String globl_tran_id) {
		MessageStatusListenerSO listenerSO = new MessageStatusListenerSO();

		listenerSO.setGlobal_txn_id(globl_tran_id);
		listenerSO.setProvider_msg_id(sendgridMessageId);
		listenerSO.setProvider_id("SendGrid");
		listenerSO.setStatus(message.getEvent());
		listenerSO.setTimestamp(message.getTimestamp().toString());
		listenerSO.setReceiver_id(message.getEmail());
		listenerSO.setSource_id(null);
		listenerSO.setAttempt(message.getAttempt());
		listenerSO.setComments(null);
		return listenerSO;
	}

	private String getInputData(HttpServletRequest req) throws IOException {
		BufferedReader d = req.getReader();
		StringBuilder inputData = new StringBuilder();

		String data = "";
		while ((data = d.readLine()) != null) {
			inputData.append(data);
		}
		return inputData.toString();
	}

}
