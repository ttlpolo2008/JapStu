package webservices;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dto.UserDto;
import services.UserService;
import util.Constants;

@Path("UserAPI")
public class UserAPI {

	@POST
	@Path("/findUserInfo.json")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchVocabulary(String requestBody) {
		// Create JsonObject to send back
		JsonParser jsonParser = new JsonParser();
		JsonObject result = new JsonObject();

		try {
			// Get parameter
			JsonObject body = jsonParser.parse(requestBody).getAsJsonObject();
			if (!body.has("userName") || !body.has("password")) {
				// If parameter is not exist, error 001
				result.addProperty("ERR001", Constants.ERR_001);

			} else {
				// Get LessonId
				String userName = body.get("userName").getAsString();
				String password = body.get("password").getAsString();

				// Search Vocabulary
				UserDto userDto = UserService.findUserInfo(userName, password);
				if (userDto == null) {
					result.addProperty("ERR002", Constants.ERR_002);
				} else {
					Gson gson = new Gson();
					String jsonData = gson.toJson(userDto);
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
