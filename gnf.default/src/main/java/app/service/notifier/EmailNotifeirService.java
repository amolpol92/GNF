package app.service.notifier;

import app.model.MessageStatus;
import app.model.UserDetailsSO;
import app.model.UserMessageSO;

/**
 * 
 * @author amolp
 * This class is a implementation of abstract Notifier which will provide us binded data of user preferences and 
 * source message details with Email as topic
 *
 */
public class EmailNotifeirService extends Notifier {

	@Override
	public UserMessageSO getNotificationDetails(UserDetailsSO userDet, MessageStatus req) {
		// TODO Auto-generated method stub
		UserMessageSO userMessageSO = setEmailMsgDetails(req, userDet);
		return userMessageSO;
	}

	private UserMessageSO setEmailMsgDetails(MessageStatus req, UserDetailsSO userDet) {
		UserMessageSO emailPrefUser = new UserMessageSO();
		emailPrefUser.setMessage(req.getMessageData());
		emailPrefUser.setUserId(userDet.getUserId());
		emailPrefUser.setGlobalTransactionId(req.getMessageId());
		emailPrefUser.setTopicName(null!=req.getEmailNotifierType()?req.getEmailNotifierType():"SendGridEmail");
		emailPrefUser.setEmailId(userDet.getEmailId());
		emailPrefUser.setRetryMessageFlag(req.isRetryMessageFlag());
		emailPrefUser.setRetryCounter(req.getRetryCounter());
		return emailPrefUser;
	}

}
