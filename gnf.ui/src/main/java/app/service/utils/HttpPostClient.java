package app.service.utils;

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

/**
 * @author adarshsinghal
 *
 */
public class HttpPostClient {

	public static String send(String url, Object entity) throws ParseException, IOException {
		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(url);

		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		StringEntity stringEntity = new StringEntity(gson.toJson(entity));
		httppost.setEntity(stringEntity);

		HttpResponse response = httpclient.execute(httppost);

		HttpEntity httpEntity = response.getEntity();
		if (httpEntity == null || httpEntity.getContent() == null) {
			return "";
		}

		String jsonStr = EntityUtils.toString(httpEntity);
		return jsonStr;
	}

}
