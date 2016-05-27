package com.learning.japstu.japstu.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.ini4j.Ini;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import com.learning.japstu.japstu.utils.Constants.*;

public class APIUtils {

	public static String apiUri = Constants.API_URI;



	public static JsonObject callSearchAPI(String uri, String api, JsonObject body) {

		try {

			// Setup URI
			URL url = new URL(String.format(apiUri, uri, api));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-type", "application/json");
			conn.setRequestProperty( "Accept", "application/json" );
			conn.setDoOutput(true);

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(body.toString());
			writer.flush();

			// Call SearchAPI
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
		}
		return null;
	}

}
