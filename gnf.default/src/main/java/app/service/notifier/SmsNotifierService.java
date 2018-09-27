package app.service.notifier;

import app.model.MessageStatus;
import app.model.UserDetailsSO;
import app.model.UserMessageSO;

/**
 * 
 * @author amolp This class is a implementation of abstract Notifier which will
 *         provide us binded data of user preferences and source message details
 *         with SMS as topic
 *
 */
public class SmsNotifierService extends Notifier {

	@Override
	public UserMessageSO getNotificationDetails(UserDetailsSO userDet, MessageStatus req) {
		return setSmsMsgDetails(req, userDet);
	}

	private UserMessageSO setSmsMsgDetails(MessageStatus req, UserDetailsSO userDet) {
		UserMessageSO smsPrefUser = new UserMessageSO();
		smsPrefUser.setMessage(req.getMessageData());
		smsPrefUser.setUserId(userDet.getUserId());
		smsPrefUser.setGlobalTransactionId(req.getMessageId());
		smsPrefUser.setTopicName(null != req.getSmsNotifeirType() ? req.getSmsNotifeirType() : "TwilioSMS");
		smsPrefUser.setMobileNumber(userDet.getMobileNumber());
		smsPrefUser.setRetryMessageFlag(req.isRetryMessageFlag());
		smsPrefUser.setRetryCounter(req.getRetryCounter());
		return smsPrefUser;
	}
}
