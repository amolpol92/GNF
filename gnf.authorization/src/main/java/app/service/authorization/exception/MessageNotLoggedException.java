package app.service.authorization.exception;

/**
 * @author AdarshSinghal
 *
 */
public class MessageNotLoggedException extends Exception {

	private static final long serialVersionUID = 8356678267939771870L;

	public MessageNotLoggedException() {
		super("Unable to log the message");
	}

}
