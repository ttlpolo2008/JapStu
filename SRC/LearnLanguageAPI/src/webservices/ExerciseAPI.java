package webservices;

import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dto.ExerciseDto;
import services.ExerciseService;
import util.Constants;

@Path("ExerciseAPI")
public class ExerciseAPI {

	@POST
	@Path("/searchExercise.json")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchExercise(String requestBody) {
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

				// Search Exercise
				List<ExerciseDto> exerciseDtoList = ExerciseService.searchExercise(lessonId);
				Gson gson = new Gson();
				String jsonData = gson.toJson(exerciseDtoList);
				result.add("data", jsonParser.parse(jsonData));
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			result.addProperty("ERR999", Constants.ERR_999);
		}

		return result.toString();
	}

}
