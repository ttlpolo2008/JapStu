package webservices;

import java.util.Date;
import java.util.List;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dao.CommonDAO;
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
				List<LearnDto> studyList = LearnService.searchStudyHistoryAll(userId);
				if (studyList == null) {
					result.addProperty("ERR002", Constants.ERR_002);
				} else {
					Gson gson =
							new GsonBuilder()
							.registerTypeAdapter(Date.class, new DateTypeAdapter())
							.registerTypeAdapter(java.sql.Date.class, new DateTypeAdapter())
							.create();
					String jsonData = gson.toJson(studyList);
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

	@POST
	@Path("/registerUsersLesson.json")
	@Produces(MediaType.APPLICATION_JSON)
	public String registerUsersLesson(String requestBody) {
		// Create JsonObject to send back
		JsonParser jsonParser = new JsonParser();
		JsonObject result = new JsonObject();

		try {
			// Get parameter
			JsonObject body = jsonParser.parse(requestBody).getAsJsonObject();
			if (!body.has("userId") || !body.has("lessonId")) {
				// If parameter is not exist, error 001
				result.addProperty("ERR001", Constants.ERR_001);

			} else {
				Long userId = body.get("userId").getAsLong();

				// Create learnDto
				LearnDto learnDto = new LearnDto();
				learnDto.setLessonId(body.get("lessonId").getAsLong());
				if (body.has("startDate")) {
					learnDto.setStartDate(new Date(body.get("startDate").getAsLong()));
				}
				if (body.has("endDate")) {
					learnDto.setEndDate(new Date(body.get("endDate").getAsLong()));
				}
				learnDto.setStatus(false);
				if (body.has("status")) {
					learnDto.setStatus(body.get("status").getAsBoolean());
				}
				if (body.has("courseStatus")) {
					learnDto.setCourseStatus(body.get("courseStatus").getAsString());
				}
				if (body.has("examMark")) {
					learnDto.setExamMark(body.get("examMark").getAsLong());
				}

				// Check Learn existed
				Boolean execResult = false;
				if (LearnService.findLessonByLessonId(userId, learnDto.getLessonId()) == null) {
					// Insert Learn
					execResult = LearnService.insertLearn(learnDto, userId);
				} else {
					// Update Learn
					execResult = LearnService.updateLearn(learnDto, userId);
				}

				// Commit/ Rollback data
				if (execResult) {
					CommonDAO.getDAO().commit();
					result.addProperty("result", true);
				} else {
					CommonDAO.getDAO().rollback();
					result.addProperty("result", false);
				}
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			result.addProperty("ERR999", Constants.ERR_999);
		}

		return result.toString();
	}

	@POST
	@Path("/selectAvgMark.json")
	@Produces(MediaType.APPLICATION_JSON)
	public String selectAvgMark(String requestBody) {
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

				// Select AVG mark
				Double avgMark = LearnService.selectAvgMark(userId);
				result.addProperty("result", avgMark);
			}

		} catch(Exception ex) {
			ex.printStackTrace();
			result.addProperty("ERR999", Constants.ERR_999);
		}

		return result.toString();
	}
}
