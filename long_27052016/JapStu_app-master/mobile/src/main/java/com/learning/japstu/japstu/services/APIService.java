package com.learning.japstu.japstu.services;

import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.learning.japstu.japstu.utils.*;

public class APIService {


	public static JsonObject findUserInfo(String userName, String password) {

		try {


			JsonObject body = new JsonObject();
			body.addProperty("userName", userName);
			body.addProperty("password", password);


			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_USER, Constants.API_FIND_USER_INFO, body);
			if (result.has("data")) {
				return result.get("data").getAsJsonObject();
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	public static JsonElement searchStudyHistoryAll(Long userId) {

		try {


			JsonObject body = new JsonObject();
			body.addProperty("userId", userId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LEARN, Constants.API_SEARCH_STUDY_HIS_ALL, body);
			if (result.has("data")) {
				return result.get("data");
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static JsonObject searchStudyHistoryLast(Long userId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("userId", userId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LEARN, Constants.API_SEARCH_STUDY_HIS_LAST, body);
			if (result.has("data")) {
				return result.get("data").getAsJsonObject();
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static JsonObject findLesson(Long lessonId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LESSON, Constants.API_FIND_LESSON, body);
			if (result.has("data")) {
				return result.get("data").getAsJsonObject();
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static JsonObject findNextLesson(Long lessonId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			if (lessonId != null) {
				body.addProperty("lessonId", lessonId);
			}

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LESSON, Constants.API_FIND_NEXT_LESSON, body);
			if (result.has("data")) {
				return result.get("data").getAsJsonObject();
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static JsonElement searchVocabulary(Long lessonId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_VOCABULARY, Constants.API_SEARCH_VOCABULARY, body);
			if (result.has("data")) {
				return result.get("data");
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static JsonElement searchGrammar(Long lessonId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_GRAMMAR, Constants.API_SEARCH_GRAMMAR, body);
			if (result.has("data")) {
				return result.get("data");
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static JsonElement searchReading(Long lessonId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_READING, Constants.API_SEARCH_READING, body);
			if (result.has("data")) {
				return result.get("data");
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static JsonElement searchListening(Long lessonId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LISTENING, Constants.API_SEARCH_LISTENING, body);
			if (result.has("data")) {
				return result.get("data");
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static JsonElement searchConversation(Long lessonId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_CONVERSATION, Constants.API_SEARCH_CONVERSATION, body);
			if (result.has("data")) {
				return result.get("data");
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static JsonElement searchKanji(Long lessonId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_KANJI, Constants.API_SEARCH_KANJI, body);
			if (result.has("data")) {
				return result.get("data");
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static JsonElement searchExercise(Long lessonId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_EXERCISE, Constants.API_SEARCH_EXERCISE, body);
			if (result.has("data")) {
				return result.get("data");
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static JsonElement createTest(Long lessonId, Long vocabularyNumber, Long grammarNumber,
										 Long readingNumber, Long listeningNumber, Long conversationNumber,
										 Long kanjiNumber) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);
			body.addProperty("vocabularyNumber", vocabularyNumber);
			body.addProperty("grammarNumber", grammarNumber);
			body.addProperty("readingNumber", readingNumber);
			body.addProperty("listeningNumber", listeningNumber);
			body.addProperty("conversationNumber", conversationNumber);
			body.addProperty("kanjiNumber", kanjiNumber);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_EXERCISE, Constants.API_CREATE_TEST, body);
			if (result.has("data")) {
				return result.get("data");
			}
			return result;

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}


	public static Boolean registerAccount(String userName, String password,
										  String nickName, String email) {
		return registerAccount(userName, password, nickName, email, null, null);
	}

	public static Boolean registerAccount(String userName, String password,
										  String nickName, String email, Integer age,
										  byte[] profileImage) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("userName", userName);
			body.addProperty("password", password);
			if (nickName != null) {
				body.addProperty("nickName", nickName);
			}
			if (email != null) {
				body.addProperty("email", nickName);
			}
			if (age != null) {
				body.addProperty("age", age);
			}
			if (profileImage != null) {
				body.addProperty("profileImage", new String(profileImage));
			}

			// Call RegisterAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_USER, Constants.API_REGISTER_ACCOUNT, body);
			if (result.has("result")) {
				return result.get("result").getAsBoolean();
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}


	public static Boolean registerUsersLesson(Long userId, Long lessonId,
											  Date startDate, Date endDate,
											  Boolean status, String courseStatus,
											  Long examMark) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("userId", userId);
			body.addProperty("lessonId", lessonId);
			if (startDate != null) {
				body.addProperty("startDate", startDate.getTime());
			}
			if (endDate != null) {
				body.addProperty("endDate", endDate.getTime());
			}
			if (status != null) {
				body.addProperty("status", status);
			}
			if (courseStatus != null) {
				body.addProperty("courseStatus", courseStatus);
			}
			if (examMark != null) {
				body.addProperty("examMark", examMark);
			}

			// Call RegisterAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LEARN, Constants.API_REGISTER_USERS_LESSON, body);
			if (result.has("result")) {
				return result.get("result").getAsBoolean();
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return false;
	}


	public static Double selectAvgMark(Long userId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("userId", userId);

			// Call SelectAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LEARN, Constants.API_SELECT_AVG_MARK, body);
			if (result.has("result")) {
				return result.get("result").getAsDouble();
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
}
