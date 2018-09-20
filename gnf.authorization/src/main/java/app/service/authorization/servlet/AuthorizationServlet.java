package app.service.authorization.servlet;

import java.io.IOException;
import java.sql.SQLException;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.service.authorization.AuthorizationService;
import app.service.authorization.exception.ExternalUserNotAllowedException;
import app.service.authorization.exception.InsufficientAuthorizationException;
import app.service.authorization.exception.NoSuchGroupException;
import app.service.authorization.model.AuthorizationRequest;
import app.service.authorization.model.AuthorizationResponse;

/**
 * @author adarshsinghal695
 *
 */
@WebServlet("/authorize")
public class AuthorizationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AuthorizationServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Gson gson = new GsonBuilder().create();
		AuthorizationRequest sourceMessage = new AuthorizationRequest(1, "1");
		String json = gson.toJson(sourceMessage);
		response.setContentType("application/json");
		response.getWriter().println(json);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("application/json");

		String inputJson = request.getReader().lines().collect(Collectors.joining());
		Gson gson = new GsonBuilder().create();
		AuthorizationRequest sourceMessage = gson.fromJson(inputJson, AuthorizationRequest.class);

		AuthorizationService authService = new AuthorizationService();
		AuthorizationResponse authResponse = new AuthorizationResponse();
		authResponse.setAuthorized(true);
		try {
			authService.checkSourceAuthorization(sourceMessage);
		} catch (SQLException e) {
			response.setStatus(e.getErrorCode());
			authResponse.setAuthorized(false);
			authResponse.setReason(e.getMessage());
		} catch (ExternalUserNotAllowedException e) {
			authResponse.setAuthorized(false);
			authResponse.setReason(e.getMessage());
		} catch (NoSuchGroupException e) {
			authResponse.setAuthorized(false);
			authResponse.setReason(e.getMessage());
		} catch (InsufficientAuthorizationException e) {
			authResponse.setAuthorized(false);
			authResponse.setReason(e.getMessage());
		}

		String json = gson.toJson(authResponse);
		response.getWriter().println(json);

	}

}
