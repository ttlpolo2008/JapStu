package services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import dto.ConversationDto;
import dto.ExerciseDto;
import dto.GrammarDto;
import dto.KanjiDto;
import dto.LearnDto;
import dto.LessonDto;
import dto.ListeningDto;
import dto.ReadingDto;
import dto.UserDto;
import dto.VocabularyDto;
import json.DateTypeAdapter;
import util.APIUtils;
import util.Constants;

public class APIService {

	/**
	 * Find User Info
	 *
	 * @param userName
	 * @param password
	 * @return UserDto
	 */
	public static UserDto findUserInfo(String userName, String password) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("userName", userName);
			body.addProperty("password", password);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_USER, Constants.API_FIND_USER_INFO, body);
			if (result.has("data")) {
				Gson gson = new Gson();

				JsonElement data = result.get("data");
				return gson.fromJson(data, UserDto.class);
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Search all study history
	 *
	 * @param userId
	 * @return Map<Long, LearnDto>
	 */
	public static Map<Long, LearnDto> searchStudyHistoryAll(Long userId) {

		Map<Long, LearnDto> studyMap = new HashMap<Long, LearnDto>();
		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("userId", userId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LEARN, Constants.API_SEARCH_STUDY_HIS_ALL, body);
			if (result.has("data")) {
				Gson gson =
						new GsonBuilder()
						.registerTypeAdapter(Date.class, new DateTypeAdapter())
						.registerTypeAdapter(java.sql.Date.class, new DateTypeAdapter())
						.create();

				JsonElement data = result.get("data");
				studyMap = gson.fromJson(data, new TypeToken<Map<Long, LearnDto>>() {}.getType());
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return studyMap;
	}

	/**
	 * Search last study history
	 *
	 * @param userId
	 * @return LearnDto
	 */
	public static LearnDto searchStudyHistoryLast(Long userId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("userId", userId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LEARN, Constants.API_SEARCH_STUDY_HIS_LAST, body);
			if (result.has("data")) {
				Gson gson =
						new GsonBuilder()
						.registerTypeAdapter(Date.class, new DateTypeAdapter())
						.registerTypeAdapter(java.sql.Date.class, new DateTypeAdapter())
						.create();

				JsonElement data = result.get("data");
				return gson.fromJson(data, LearnDto.class);
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Find Lesson
	 *
	 * @param lessonId
	 * @return LessonDto
	 */
	public static LessonDto findLesson(Long lessonId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LESSON, Constants.API_FIND_LESSON, body);
			if (result.has("data")) {
				Gson gson = new Gson();

				JsonElement data = result.get("data");
				return gson.fromJson(data, LessonDto.class);
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Find next Lesson
	 *
	 * @param lessonId
	 * @return LessonDto
	 */
	public static LessonDto findNextLesson(Long lessonId) {

		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LESSON, Constants.API_FIND_NEXT_LESSON, body);
			if (result.has("data")) {
				Gson gson = new Gson();

				JsonElement data = result.get("data");
				return gson.fromJson(data, LessonDto.class);
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * Search Vocabulary
	 *
	 * @param lessonId
	 * @return List<VocabularyDto>
	 */
	public static List<VocabularyDto> searchVocabulary(Long lessonId) {

		List<VocabularyDto> vocabularyDtoList = new ArrayList<VocabularyDto>();
		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_VOCABULARY, Constants.API_SEARCH_VOCABULARY, body);
			if (result.has("data")) {
				Gson gson = new Gson();

				JsonElement data = result.get("data");
				vocabularyDtoList = gson.fromJson(data, new TypeToken<List<VocabularyDto>>() {}.getType());
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return vocabularyDtoList;
	}

	/**
	 * Search Grammar
	 *
	 * @param lessonId
	 * @return List<GrammarDto>
	 */
	public static List<GrammarDto> searchGrammar(Long lessonId) {

		List<GrammarDto> grammarDtoList = new ArrayList<GrammarDto>();
		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_GRAMMAR, Constants.API_SEARCH_GRAMMAR, body);
			if (result.has("data")) {
				Gson gson = new Gson();

				JsonElement data = result.get("data");
				grammarDtoList = gson.fromJson(data, new TypeToken<List<GrammarDto>>() {}.getType());
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return grammarDtoList;
	}

	/**
	 * Search Reading
	 *
	 * @param lessonId
	 * @return List<ReadingDto>
	 */
	public static List<ReadingDto> searchReading(Long lessonId) {

		List<ReadingDto> readingDtoList = new ArrayList<ReadingDto>();
		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_READING, Constants.API_SEARCH_READING, body);
			if (result.has("data")) {
				Gson gson = new Gson();

				JsonElement data = result.get("data");
				readingDtoList = gson.fromJson(data, new TypeToken<List<ReadingDto>>() {}.getType());
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return readingDtoList;
	}

	/**
	 * Search Listening
	 *
	 * @param lessonId
	 * @return List<ListeningDto>
	 */
	public static List<ListeningDto> searchListening(Long lessonId) {

		List<ListeningDto> listeningDtoList = new ArrayList<ListeningDto>();
		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_LISTENING, Constants.API_SEARCH_LISTENING, body);
			if (result.has("data")) {
				Gson gson = new Gson();

				JsonElement data = result.get("data");
				listeningDtoList = gson.fromJson(data, new TypeToken<List<ListeningDto>>() {}.getType());
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return listeningDtoList;
	}

	/**
	 * Search Conversation
	 *
	 * @param lessonId
	 * @return List<ConversationDto>
	 */
	public static List<ConversationDto> searchConversation(Long lessonId) {

		List<ConversationDto> conversationDtoList = new ArrayList<ConversationDto>();
		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_CONVERSATION, Constants.API_SEARCH_CONVERSATION, body);
			if (result.has("data")) {
				Gson gson = new Gson();

				JsonElement data = result.get("data");
				conversationDtoList = gson.fromJson(data, new TypeToken<List<ConversationDto>>() {}.getType());
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return conversationDtoList;
	}

	/**
	 * Search Kanji
	 *
	 * @param lessonId
	 * @return List<KanjiDto>
	 */
	public static List<KanjiDto> searchKanji(Long lessonId) {

		List<KanjiDto> kanjiDtoList = new ArrayList<KanjiDto>();
		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_KANJI, Constants.API_SEARCH_KANJI, body);
			if (result.has("data")) {
				Gson gson = new Gson();

				JsonElement data = result.get("data");
				kanjiDtoList = gson.fromJson(data, new TypeToken<List<KanjiDto>>() {}.getType());
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return kanjiDtoList;
	}

	/**
	 * Search Exercise
	 *
	 * @param lessonId
	 * @return List<ExerciseDto>
	 */
	public static List<ExerciseDto> searchExercise(Long lessonId) {

		List<ExerciseDto> exerciseDtoList = new ArrayList<ExerciseDto>();
		try {

			// Create body
			JsonObject body = new JsonObject();
			body.addProperty("lessonId", lessonId);

			// Call SearchAPI
			JsonObject result =
					APIUtils.callSearchAPI(
							Constants.URI_EXERCISE, Constants.API_SEARCH_EXERCISE, body);
			if (result.has("data")) {
				Gson gson = new Gson();

				JsonElement data = result.get("data");
				exerciseDtoList = gson.fromJson(data, new TypeToken<List<ExerciseDto>>() {}.getType());
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return exerciseDtoList;
	}

	/**
	 * Create Test
	 *
	 * @param lessonId
	 * @param vocabularyNumber
	 * @param grammarNumber
	 * @param readingNumber
	 * @param listeningNumber
	 * @param conversationNumber
	 * @param kanjiNumber
	 * @return List<ExerciseDto>
	 */
	public static List<ExerciseDto> createTest(Long lessonId, Long vocabularyNumber, Long grammarNumber,
											   Long readingNumber, Long listeningNumber, Long conversationNumber,
											   Long kanjiNumber) {

		List<ExerciseDto> exerciseDtoList = new ArrayList<ExerciseDto>();
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
				Gson gson = new Gson();

				JsonElement data = result.get("data");
				exerciseDtoList = gson.fromJson(data, new TypeToken<List<ExerciseDto>>() {}.getType());
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return exerciseDtoList;
	}
}
