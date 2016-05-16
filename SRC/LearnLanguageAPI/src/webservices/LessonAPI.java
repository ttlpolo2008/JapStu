package webservices;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dto.LessonDto;
import services.LessonService;
import util.Constants;

@Path("LessonAPI")
public class LessonAPI {

	@POST
	@Path("/findLesson.json")
	@Produces(MediaType.APPLICATION_JSON)
	public String findLesson(String requestBody) {
		// Create JsonObject to send back
		JsonParser jsonParser = new JsonParser();
		JsonObject result = new JsonObject();

		try {
			// Get parameter
			JsonObject body = jsonParser.parse(requestBody).getAsJsonObject();
			if (!body.has("lessonId")) {
				// If parameter is not exist, error 001
				result.addProperty("ERR001", Constants.ERR_001);

			} else {
				// Get LessonId
				Long lessonId = body.get("lessonId").getAsLong();

				// Find Lesson
				LessonDto lessonDto = LessonService.findLesson(lessonId);
				if (lessonDto == null) {
					// If can not find lesson, error 002
					result.addProperty("ERR002", Constants.ERR_002);

				} else {
					// Edit selected data
					Gson gson = new Gson();
					String jsonData = gson.toJson(lessonDto);
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
	@Path("/findNextLesson.json")
	@Produces(MediaType.APPLICATION_JSON)
	public String findNextLesson(String requestBody) {
		// Create JsonObject to send back
		JsonParser jsonParser = new JsonParser();
		JsonObject result = new JsonObject();

		try {
			// Get parameter
			JsonObject body = jsonParser.parse(requestBody).getAsJsonObject();

			// Get LessonId
			Long lessonId = null;
			if (body.has("lessonId")) {
				lessonId = body.get("lessonId").getAsLong();
			}

			// Find Lesson
			LessonDto lessonDto = LessonService.findNextLesson(lessonId);
			if (lessonDto == null) {
				// If can not find lesson, error 002
				result.addProperty("ERR002", Constants.ERR_002);

			} else {
				// Edit selected data
				Gson gson = new Gson();
				String jsonData = gson.toJson(lessonDto);
				result.add("data", jsonParser.parse(jsonData));
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			result.addProperty("ERR999", Constants.ERR_999);
		}

		return result.toString();
	}
}
