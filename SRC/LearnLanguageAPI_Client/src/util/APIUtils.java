package util;

import javax.ws.rs.core.MediaType;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class APIUtils {

	public static JsonObject callSearchAPI(String uri, String api, JsonObject body) {

		try {
			// Setup URI
			Client client = Client.create();
			WebResource webResource =
					client.resource(
							String.format(Constants.API_URI, uri, api));
			ClientResponse response = null;

			// Call SearchAPI
			response = webResource
					.accept(MediaType.APPLICATION_JSON)
					.type(MediaType.APPLICATION_JSON)
					.post(ClientResponse.class, body.toString());
			String strJson = response.getEntity(String.class);

			// Return value
			JsonParser jsonParser = new JsonParser();
			return (JsonObject) jsonParser.parse(strJson);

		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
