package app.service;

import java.sql.SQLException;

import app.exception.ExternalUserNotAllowedException;
import app.exception.InsufficientAuthorizationException;
import app.exception.NoSuchGroupException;
import app.logging.CloudLogger;
import app.model.SourceMessage;

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

	private CloudLogger LOGGER = CloudLogger.getLogger();

	/**
	 * @param sourceMessage
	 * @return boolean
	 * @throws SQLException
	 * @throws ExternalUserNotAllowedException
	 * @throws NoSuchGroupException
	 * @throws InsufficientAuthorizationException
	 */
	public void checkSourceAuthorization(SourceMessage sourceMessage) throws SQLException,
			ExternalUserNotAllowedException, NoSuchGroupException, InsufficientAuthorizationException {
		LOGGER.info("Inside Authorization Service. Checking for External user.");
		checkForExternalUser(sourceMessage);
		LOGGER.info("Inside Authorization Service. Internal User verified. \n"
				+ "Checking if source has sufficient authorization to post on group. Group Id: "
				+ sourceMessage.getGroupId());
		checkSourceToGroupAuthorization(sourceMessage);
		LOGGER.info("Inside Authorization Service. Source has sufficient authorization.");
	}

	/**
	 * The application should be accessible by Internal Users only. Terminate if
	 * External User.
	 * 
	 * @param srcMessage
	 * @throws ExternalUserNotAllowedException
	 */
	private void checkForExternalUser(SourceMessage srcMessage) throws ExternalUserNotAllowedException {
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
	 */
	private void checkSourceToGroupAuthorization(SourceMessage sourceMessage)
			throws SQLException, NoSuchGroupException, InsufficientAuthorizationException {

		UserService userService = new UserService();
		int groupAuthLevel = userService.getGroupAuthLevel(sourceMessage.getGroupId());

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
