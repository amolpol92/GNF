package app.service.client;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.constants.ConstantsURL;
import app.model.AuthorizationRequest;
import app.model.AuthorizationResponse;

public class AuthServiceClient {

	public AuthorizationResponse authorize(AuthorizationRequest authRequest) throws ParseException, IOException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(ConstantsURL.AUTHORIZE_URL);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		StringEntity entity = new StringEntity(gson.toJson(authRequest));
		httppost.setEntity(entity);

		HttpResponse response = httpclient.execute(httppost);

		HttpEntity httpEntity = response.getEntity();
		if (httpEntity == null || httpEntity.getContent() == null) {
			return new AuthorizationResponse();
		}

		String jsonStr = EntityUtils.toString(httpEntity);
		AuthorizationResponse results = gson.fromJson(jsonStr, AuthorizationResponse.class);
		
		return results;
	}

}
