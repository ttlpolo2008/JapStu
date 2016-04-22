package webservices;

import java.util.Date;
import java.util.Map;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dto.LearnDto;
import json.DateTypeAdapter;
import services.LearnService;
import util.Constants;

@Path("LearnAPI")
public class LearnAPI {

	@POST
	@Path("/searchStudyHistoryAll.json")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchStudyHistoryAll(String requestBody) {
		// Create JsonObject to send back
		JsonParser jsonParser = new JsonParser();
		JsonObject result = new JsonObject();

		try {
			// Get parameter
			JsonObject body = jsonParser.parse(requestBody).getAsJsonObject();
			if (!body.has("userId")) {
				// If parameter is not exist, error 001
				result.addProperty("ERR001", Constants.ERR_001);

			} else {
				// Get userId
				Long userId = body.get("userId").getAsLong();

				// Search studyMap
				Map<Long, LearnDto> studyMap = LearnService.searchStudyHistoryAll(userId);
				if (studyMap == null) {
					result.addProperty("ERR002", Constants.ERR_002);
				} else {
					Gson gson =
							new GsonBuilder()
							.registerTypeAdapter(Date.class, new DateTypeAdapter())
							.registerTypeAdapter(java.sql.Date.class, new DateTypeAdapter())
							.create();
					String jsonData = gson.toJson(studyMap);
					result.add("data", jsonParser.parse(jsonData));
				}
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			result.addProperty("ERR999", Constants.ERR_999);
		}

		return result.toString();
	}

	@POST
	@Path("/searchStudyHistoryLast.json")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchStudyHistoryLast(String requestBody) {
		// Create JsonObject to send back
		JsonParser jsonParser = new JsonParser();
		JsonObject result = new JsonObject();

		try {
			// Get parameter
			JsonObject body = jsonParser.parse(requestBody).getAsJsonObject();
			if (!body.has("userId")) {
				// If parameter is not exist, error 001
				result.addProperty("ERR001", Constants.ERR_001);

			} else {
				// Get userId
				Long userId = body.get("userId").getAsLong();

				// Search studyMap
				LearnDto learnDto = LearnService.searchStudyHistoryLast(userId);
				if (learnDto == null) {
					result.addProperty("ERR002", Constants.ERR_002);
				} else {
					Gson gson =
							new GsonBuilder()
							.registerTypeAdapter(Date.class, new DateTypeAdapter())
							.registerTypeAdapter(java.sql.Date.class, new DateTypeAdapter())
							.create();
					String jsonData = gson.toJson(learnDto);
					result.add("data", jsonParser.parse(jsonData));
				}
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			result.addProperty("ERR999", Constants.ERR_999);
		}

		return result.toString();
	}
}
