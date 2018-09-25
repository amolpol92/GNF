package app.service.authorization;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import app.service.authorization.constants.Constants;
import app.service.authorization.exception.ExternalUserNotAllowedException;
import app.service.authorization.exception.InsufficientAuthorizationException;
import app.service.authorization.exception.NoSuchGroupException;
import app.service.authorization.model.AuthorizationRequest;
import app.service.authorization.model.LogRequest;

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
	/**
	 * @param authorizationRequest
	 * @return boolean
	 * @throws SQLException
	 * @throws ExternalUserNotAllowedException
	 * @throws NoSuchGroupException
	 * @throws InsufficientAuthorizationException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws MessageNotLoggedException
	 */
	public void checkSourceAuthorization(AuthorizationRequest authorizationRequest)
			throws SQLException, ExternalUserNotAllowedException, NoSuchGroupException,
			InsufficientAuthorizationException, ClientProtocolException, IOException {
		checkForExternalUser(authorizationRequest);
		checkSourceToGroupAuthorization(authorizationRequest);

		String message = "Source has sufficient authorization.";
		LogRequest logRequest = new LogRequest(message, "INFO");
		Map<String, String> labels = getLabels(authorizationRequest);
		logRequest.setLabels(labels);
		LogServiceClient.getLogger().log(logRequest);
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
	private void checkForExternalUser(AuthorizationRequest authorizationRequest)
			throws ExternalUserNotAllowedException, ClientProtocolException, IOException {
		if (authorizationRequest.getSourceAuthLevel() == 0) {

			String message = "Terminating transaction. Reason:- External User tried to send message.";

			LogRequest logRequest = new LogRequest(message, "WARNING");

			Map<String, String> labels = getLabels(authorizationRequest);
			logRequest.setLabels(labels);
			LogServiceClient.getLogger().log(logRequest);

			throw new ExternalUserNotAllowedException();
		}
	}

	/**
	 * Source Authorization Level must be equal or greater than required
	 * authorization level for targeting a group for messaging.
	 * 
	 * @param authorizationRequest
	 * @throws SQLException
	 * @throws NoSuchGroupException
	 * @throws InsufficientAuthorizationException
	 * @throws IOException
	 * @throws ClientProtocolException
	 * @throws MessageNotLoggedException
	 */
	private void checkSourceToGroupAuthorization(AuthorizationRequest authorizationRequest) throws SQLException,
			NoSuchGroupException, InsufficientAuthorizationException, ClientProtocolException, IOException {

		UserServiceClient userService = new UserServiceClient();
		int groupAuthLevel = userService.getGroupAuthLevel(authorizationRequest);

		if (authorizationRequest.getSourceAuthLevel() < groupAuthLevel) {
			String message = "Transaction terminated. Reason:- Insufficient authorization.";
			LogRequest logRequest = new LogRequest(message, "WARNING");

			Map<String, String> labels = getLabels(authorizationRequest);

			logRequest.setLabels(labels);

			LogServiceClient.getLogger().log(logRequest);
			throw new InsufficientAuthorizationException();
		}
	}

	private Map<String, String> getLabels(AuthorizationRequest authorizationRequest) {
		Map<String, String> labels = new HashMap<>();
		labels.put(Constants.GB_TXN_ID_KEY, authorizationRequest.getGlobalTxnId());
		String srcAuthLvlStr = String.valueOf(authorizationRequest.getSourceAuthLevel());
		labels.put("Source Authorization Level", srcAuthLvlStr);
		labels.put("Target Group Id", authorizationRequest.getGroupId());
		return labels;
	}

}
