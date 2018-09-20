package app.service.authorization;

import java.io.IOException;
import java.sql.SQLException;

import org.apache.http.client.ClientProtocolException;

import app.service.authorization.exception.ExternalUserNotAllowedException;
import app.service.authorization.exception.InsufficientAuthorizationException;
import app.service.authorization.exception.MessageNotLoggedException;
import app.service.authorization.exception.NoSuchGroupException;
import app.service.authorization.model.AuthorizationRequest;

/**
 * Authorization Service is responsible for handling authorization for both
 * Notiy as well as Notification Service.
 * 
 * <br>
 * <br>
 * Service class should be 'As Simple As Possible'. If you're modifying this
 * class, add operation &amp; delegate logics to other class
 * 
 * @author AdarshSinghal
 *
 */
public class AuthorizationService {

	private LogServiceClient LOGGER = LogServiceClient.getLogger();

	/**
	 * @param sourceMessage
	 * @return boolean
	 * @throws SQLException
	 * @throws ExternalUserNotAllowedException
	 * @throws NoSuchGroupException
	 * @throws InsufficientAuthorizationException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws MessageNotLoggedException
	 */
	public void checkSourceAuthorization(AuthorizationRequest sourceMessage)
			throws SQLException, ExternalUserNotAllowedException, NoSuchGroupException,
			InsufficientAuthorizationException, ClientProtocolException, IOException, MessageNotLoggedException {
		checkForExternalUser(sourceMessage);
		checkSourceToGroupAuthorization(sourceMessage);
		LOGGER.info("Inside Authorization Service. Source has sufficient authorization.");
	}

	/**
	 * The application should be accessible by Internal Users only. Terminate if
	 * External User.
	 * 
	 * @param srcMessage
	 * @throws ExternalUserNotAllowedException
	 * @throws MessageNotLoggedException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	private void checkForExternalUser(AuthorizationRequest srcMessage)
			throws ExternalUserNotAllowedException, ClientProtocolException, IOException, MessageNotLoggedException {
		if (srcMessage.getSourceAuthLevel() == 0) {
			LOGGER.warning("Inside Authorization Service. Throwing ExternalUserNotAllowedException. "
					+ "\nTransaction terminated. " + "Reason:- External User tried to send message.");
			throw new ExternalUserNotAllowedException();
		}
	}

	/**
	 * Source Authorization Level must be equal or greater than required
	 * authorization level for targeting a group for messaging.
	 * 
	 * @param sourceMessage
	 * @throws SQLException
	 * @throws NoSuchGroupException
	 * @throws InsufficientAuthorizationException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws MessageNotLoggedException
	 */
	private void checkSourceToGroupAuthorization(AuthorizationRequest sourceMessage) throws SQLException, NoSuchGroupException,
			InsufficientAuthorizationException, ClientProtocolException, IOException, MessageNotLoggedException {

		UserServiceClient userService = new UserServiceClient();
		int groupAuthLevel = userService.getGroupAuthLevel(String.valueOf(sourceMessage.getGroupId()));

		LOGGER.info(
				"Inside Authorization Service. Retrieved Target Group's required authorization level from User Service. Group id: "
						+ sourceMessage.getGroupId() + ", Minimum Required auth level=" + groupAuthLevel);

		if (sourceMessage.getSourceAuthLevel() < groupAuthLevel) {
			LOGGER.warning(
					"Inside Authorization Service." + " Transaction terminated. Reason:- Insufficient authorization.");
			throw new InsufficientAuthorizationException();
		}
	}

}
