package app.service.notifier;

import app.model.MessageStatus;
import app.model.UserDetailsSO;
import app.model.UserMessageSO;
/**
 * 
 * @author amolp
 * This class behaves as abstract Notifier to which will provide us binded data of user preferences and 
 * source message details 
 *
 */
public abstract class Notifier {
	abstract public UserMessageSO getNotificationDetails(UserDetailsSO userDet,MessageStatus req);
}
