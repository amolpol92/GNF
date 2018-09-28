package app.servlet.tabledata;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.dao.MessageStatusDAO;
import app.model.MessageStatusCacheField;

/**
 * This servlet is used to get message status cache data
 * 
 * @author AdarshSinghal
 *
 */
@WebServlet("/api/getMsgStatusDbdata")
public class MsgStatusDbDataServlet extends TableDataParentServlet<MessageStatusCacheField> {

	private static final long serialVersionUID = 6587925302210756509L;
	private static final Logger LOGGER = Logger.getLogger(MsgStatusDbDataServlet.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<MessageStatusCacheField> messageStatusDetails = getMessageStatusDetailsList();

		if (messageStatusDetails == null || messageStatusDetails.isEmpty()) {
			prepareNoContentResponse(response);
			return;
		}

		prepareJsonResponse(response, messageStatusDetails);
	}

	private List<MessageStatusCacheField> getMessageStatusDetailsList() {
		List<MessageStatusCacheField> messageStatusDetails = null;
		MessageStatusDAO dao;
		try {
			dao = new MessageStatusDAO();
			messageStatusDetails = dao.getAllFieldDetails();
		} catch (SQLException e) {
			LOGGER.severe(e.getMessage());
		}
		return messageStatusDetails;
	}

}
