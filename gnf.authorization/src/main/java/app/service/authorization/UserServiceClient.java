package app.service.authorization;

import java.io.IOException;
import java.sql.SQLException;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import app.service.authorization.constants.ConstantsURL;

import app.service.authorization.model.GroupAuthResponse;
import app.service.authorization.model.GroupDetailsRequest;*/

import org.apache.http.client.ClientProtocolException;
import app.service.authorization.exception.NoSuchGroupException;
import app.service.authorization.model.AuthorizationRequest;
/**
 * @author AdarshSinghal
 *
 */
public class UserServiceClient {

	public int getGroupAuthLevel(AuthorizationRequest authorizationRequest) throws ClientProtocolException, IOException, SQLException, NumberFormatException, NoSuchGroupException {
		//TODO Uncomment & change following according to the user sevice when it is separated
		
/*		HttpClient httpclient = HttpClientBuilder.create().build();
		HttpPost httppost = new HttpPost(ConstantsURL.USER_SVC_URL);
		Gson gson = new GsonBuilder().setPrettyPrinting().create();

		StringEntity entity = new StringEntity(gson.toJson(new GroupDetailsRequest(groupId)));
		httppost.setEntity(entity);

		HttpResponse response = httpclient.execute(httppost);

		HttpEntity httpEntity = response.getEntity();

		if (httpEntity == null || httpEntity.getContent() == null) {
			return -1;
		}

		String jsonStr = EntityUtils.toString(httpEntity);
		GroupAuthResponse grpAuthResponse = gson.fromJson(jsonStr, GroupAuthResponse.class);

		int authLevel = grpAuthResponse.getAuthLevel();
		*/
		
		UserGroupDetailsDAO dao = new UserGroupDetailsDAO();
		
		return dao.getAuthLevel(authorizationRequest);
	}

}
