package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.ini4j.Ini;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class APIUtils {

	public static String apiUri;

	/**
	 * Read Ini File
	 * @return
	 */
	private static Boolean readIniFile() {
		if (!StringUtil.isNullOrEmpty(apiUri)) {
			return true;
		}

		try {
//			Ini iniFile = new Ini(new FileInputStream("C:/LearningLanguage/config.ini"));
//			if (iniFile != null) {
//				apiUri = iniFile.get("API", "API_URI");
//			}
			apiUri = "http://localhost:8080/LearnLanguageAPI/rest/%s/%s.json";
			return true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	public static JsonObject callSearchAPI(String uri, String api, JsonObject body) {

		try {
			// Read ini file
			if (!readIniFile()) {
				return null;
			}

//			// Setup URI
//			Client client = Client.create();
//			WebResource webResource =
//					client.resource(
//							String.format(apiUri, uri, api));
//			ClientResponse response = null;
//
//			// Call SearchAPI
//			response = webResource
//					.accept(MediaType.APPLICATION_JSON)
//					.type(MediaType.APPLICATION_JSON)
//					.post(ClientResponse.class, body.toString());
//			String strJson = response.getEntity(String.class);

			URL url = new URL(String.format(apiUri, uri, api));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty( "Accept", "application/json" );
			conn.setDoOutput(true);

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(body.toString());
			writer.flush();

			String line;
			String strJson = "";
			BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			while ((line = reader.readLine()) != null) {
				strJson += line;
			}


			// Return value
			JsonParser jsonParser = new JsonParser();
			return (JsonObject) jsonParser.parse(strJson);

		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

}
