package app.service.client;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;

import app.model.LogRequest;
import app.service.GenericMessagePublisher;

/**
 * @author AdarshSinghal
 *
 */
public class LogServiceClient {

	public static LogServiceClient logServiceClient;

	public static LogServiceClient getLogger() {
		if (logServiceClient == null)
			logServiceClient = new LogServiceClient();
		return logServiceClient;
	}

	public String log(LogRequest logRequest) throws IOException {

		Gson gson = new GsonBuilder().create();
		String json = gson.toJson(logRequest);

		ByteString byteString = ByteString.copyFrom(json, "UTF-8");

		PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(byteString).build();
		GenericMessagePublisher publisher = new GenericMessagePublisher();
		String messageId = "";
		try {
			messageId = publisher.publishMessage("logMsg", pubsubMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return messageId;
	}

}
