package app.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.servlet.ServletException;

import app.dao.UserDetailsDao;
import app.dao.UserGroupDetailsDAO;
import app.exception.NoSuchGroupException;
import app.model.MessageStatus;
import app.model.SubscriberMessage;
import app.model.UserDetailsSO;
import app.service.notifier.BindUserMessageDetails;
import app.servlet.HttpClientRequestHandler;
import app.util.ExternalProperties;

/**
 * @author AmolPol This class is used to fetch user related information through
 *         UserServlet endpoint
 *
 */
public class UserService {

	/**
	 * this method being called after pulling messages from notify subscription
	 * of pubsub layer receives pulled messages from notify pull pubsub layer
	 * calls decoupled UserServlet endpoint to check user and there group
	 * details to whom we have to send message
	 * 
	 * @param messageList
	 * @throws ServletException
	 * @throws IOException
	 */
	public void sendMessagesToUser(List<SubscriberMessage> messageList) throws ServletException, IOException {

		String userSvcURL = ExternalProperties.getAppConfig("user.service.url");

		messageList.forEach(message -> passMessagetoUserService(userSvcURL, message));

	}

	/**
	 * @param userSvcURL
	 * @param message
	 */
	private void passMessagetoUserService(String userSvcURL, SubscriberMessage message) {
		HttpClientRequestHandler httpClient = new HttpClientRequestHandler();
		MessageStatus requestObject = getMessageStatus(message);
		try {
			httpClient.sendPostReturnStatus(requestObject, userSvcURL);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param subMessage
	 * @return
	 */
	private MessageStatus getMessageStatus(SubscriberMessage subMessage) {
		MessageStatus requestObject = new MessageStatus();
		requestObject.setGlobalTxnId(subMessage.getGlobalTransactionId());
		requestObject.setMessageId(subMessage.getGlobalTransactionId());
		requestObject.setMessageData(subMessage.getMessage());
		
		if (null != subMessage.getDestGroupId() && !subMessage.getDestGroupId().isEmpty())
			requestObject.setDestGroupId(subMessage.getDestGroupId());

		requestObject.setRetryMessageFlag(Boolean.valueOf(subMessage.getRetryMessageFlag()));
		requestObject.setRetryCounter(
				null != subMessage.getRetryCounter() ? Integer.valueOf(subMessage.getRetryCounter()) : 0);
		return requestObject;
	}

	/**
	 * this method listens request on user servlet endpoint receives actual
	 * messages after pulling from subscriptions prepares the userlist based on
	 * group from dao and calls notify uitility to handle and publish on pubsub
	 * 
	 * @param message
	 * @throws Exception
	 * @throws ServletException
	 * 
	 */
	public void checkAllUserPreference(MessageStatus req) throws Exception {
		List<UserDetailsSO> allUsers = null;
		UserDetailsDao userDetailsDao = new UserDetailsDao();
		if (null != req.getDestGroupId() && req.getDestGroupId() != "")
			allUsers = userDetailsDao.getAllUserDetails(req.getDestGroupId());
		else
			allUsers = userDetailsDao.getAllUserDetails();

		BindUserMessageDetails utility = new BindUserMessageDetails();
		if (null != allUsers && allUsers.size() > 0)
			utility.prepareMessagesWithPreferences(allUsers, req);
		else {
			String message = "Notification Preferences not set by any user";
			throw new Exception(message);
		}

	}

	public int getGroupAuthLevel(int groupId) throws SQLException, NoSuchGroupException {
		UserGroupDetailsDAO userGroupDetailsDAO = new UserGroupDetailsDAO();
		int grpAuthLvl = userGroupDetailsDAO.getAuthLevel(groupId);
		return grpAuthLvl;
	}

}
