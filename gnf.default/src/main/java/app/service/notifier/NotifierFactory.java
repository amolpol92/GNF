package app.service.notifier;

/**
 * 
 * @author amolp This class behaves as Notifier factory classs which will be
 *         responsible to provide Notifier details
 *
 */
public class NotifierFactory {

	public Notifier getNotifier(String notiferType) {
		if (null == notiferType) {
			return null;
		}
		if (notiferType.equalsIgnoreCase("Email")) {
			return new EmailNotifeirService();
		}
		if (notiferType.equalsIgnoreCase("Sms")) {
			return new SmsNotifierService();
		}
		return null;
	}

	/*
	 * public Notifier getNotifierType(String notiferType) {
	 * if(null==notiferType) { return null; }
	 * if(notiferType.equalsIgnoreCase("SendGridEmail")) { return new
	 * EmailNotifeirService(); }
	 * if(notiferType.equalsIgnoreCase("JangoMailEmail")) { return new
	 * EmailNotifeirService(); } return null; }
	 */
}
