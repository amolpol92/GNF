package app.service.authorization;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.service.authorization.constants.ConstantsURL;
import app.service.authorization.exception.MessageNotLoggedException;
import app.service.authorization.model.LogRequest;

/**
 * @author AdarshSinghal
 *
 */
public class LogServiceClient {
	
	public static LogServiceClient logServiceClient;

	public static LogServiceClient getLogger() {
		if(logServiceClient==null) logServiceClient = new LogServiceClient();
		return logServiceClient;
	}

	public void info(String message) throws ClientProtocolException, IOException, MessageNotLoggedException {
		LogRequest logRequest = new LogRequest(message, "INFO", "gae_app", "AuthorizationService");
		
		log(logRequest);
	}

	public void warning(String message) throws ClientProtocolException, IOException, MessageNotLoggedException {
LogRequest logRequest = new LogRequest(message, "WRAARNING ", "gae_app", "AuthorizationService");
		
		log(logRequest);
	}

	private void log(LogRequest logRequest) throws ClientProtocolException, IOException, MessageNotLoggedException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(ConstantsURL.LOG_URL);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		StringEntity entity = new StringEntity(gson.toJson(logRequest));
		httppost.setEntity(entity);

		HttpResponse response = httpclient.execute(httppost);

		HttpEntity httpEntity = response.getEntity();

		if (response.getStatusLine().getStatusCode() != 200 || httpEntity == null || httpEntity.getContent() == null) {
			throw new MessageNotLoggedException();
		}
	}

}
