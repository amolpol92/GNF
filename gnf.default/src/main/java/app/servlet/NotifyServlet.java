package app.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.exception.NoSuchGroupException;
import app.exception.PANDataFoundSecurityViolationException;
import app.exception.UserNotAuthorizedException;
import app.model.NotifyResponse;
import app.model.PublisherMessage;
import app.model.SourceMessage;
import app.service.NotifyService;

/**
 * @author adarshsinghal
 *
 */
@WebServlet(name = "NotifyServlet", urlPatterns = { "/notify" })
public class NotifyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	//private static final CloudLogger LOGGER = CloudLogger.getLogger();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		SourceMessage sourceMessage = new SourceMessage(1, 1, "A sample message");

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String json = gson.toJson(sourceMessage);
		response.getWriter().println(json);

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		SourceMessage sourceMessage = getSourceMessage(request);

		NotifyService notifyService = new NotifyService();

		response.setContentType("application/json");
		List<PublisherMessage> publishedMessages = null;
		try {
			publishedMessages = notifyService.notify(sourceMessage);
		} catch (SQLException | NoSuchGroupException | PANDataFoundSecurityViolationException
				| UserNotAuthorizedException e) {
			if (isNotAuthorized(e)) {

				response.setStatus(4030);
			}

			NotifyResponse notifyRes = new NotifyResponse(e.getMessage());
			response.getWriter().println(gson.toJson(notifyRes));

			return;
		}

		if (publishedMessages != null && publishedMessages.size() != 0) {
			NotifyResponse notifyRes = new NotifyResponse(publishedMessages);
			String json = gson.toJson(notifyRes);
			response.getWriter().println(json);
		}

	}

	private boolean isNotAuthorized(Exception exception) {
		return (exception instanceof NoSuchGroupException || exception instanceof PANDataFoundSecurityViolationException
				|| exception instanceof UserNotAuthorizedException);
	}

	private SourceMessage getSourceMessage(HttpServletRequest request) throws IOException {
		Gson gson = new Gson();

		String inputJson = request.getReader().lines().collect(Collectors.joining());
		SourceMessage sourceMessage = gson.fromJson(inputJson, SourceMessage.class);

		// Generate Global transaction id if not provided from source
		String gbTxnId = sourceMessage.getGlobalTxnId();
		if (gbTxnId == null || gbTxnId.isEmpty()) {
			gbTxnId = "g" + new Date().getTime() + "r" + (int) (Math.random() * 100);
		}

		sourceMessage.setGlobalTxnId(gbTxnId);
		return sourceMessage;
	}
}
