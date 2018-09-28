package app.servlet.tabledata;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import app.dao.MessageStatusListenerDao;
import app.model.MessageStatusListenerSO;

/**
 * This servlet is used to get message status cache data
 * 
 * @author AdarshSinghal
 *
 */
@WebServlet("/api/getMsgCacheData")
public class MsgCacheDataServlet extends TableDataParentServlet<MessageStatusListenerSO> {

	private static final long serialVersionUID = 6587925302210756509L;
	private static final Logger LOGGER = Logger.getLogger(MsgCacheDataServlet.class.getName());

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		List<MessageStatusListenerSO> messageList = getMessageCacheDetailsList();

		if (messageList == null || messageList.isEmpty()) {
			prepareNoContentResponse(response);
			return;
		}

		prepareJsonResponse(response, messageList);
	}

	private List<MessageStatusListenerSO> getMessageCacheDetailsList() {
		List<MessageStatusListenerSO> messageList = null;
		MessageStatusListenerDao dao;
		try {
			dao = new MessageStatusListenerDao();
			messageList = dao.getMessageDetailsFromStore();
		} catch (SQLException | ClassNotFoundException e) {
			LOGGER.severe(e.getMessage());
		}
		return messageList;
	}

}
