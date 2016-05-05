import java.util.Date;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import services.APIService;

public class Test_CallSearchAPI {

	public static void main(String []args) {

		JsonObject userDto = APIService.findUserInfo("trungln", "123");

		JsonElement result = APIService.searchStudyHistoryAll(new Long(1));
		if (result.isJsonArray()) {
			JsonArray studyList = result.getAsJsonArray();
		}

		JsonObject studyLast = APIService.searchStudyHistoryLast(new Long(1));
		JsonObject lessonDto = APIService.findNextLesson(new Long(7));

		result = APIService.searchVocabulary(new Long(7));
		if (result.isJsonArray()) {
			JsonArray vocabularyDtoList = result.getAsJsonArray();
		}

		result = APIService.searchGrammar(new Long(7));
		if (result.isJsonArray()) {
			JsonArray grammarDtoList = result.getAsJsonArray();
		}

		result = APIService.searchReading(new Long(7));
		if (result.isJsonArray()) {
			JsonArray readingDtoList = result.getAsJsonArray();
		}

		result = APIService.searchListening(new Long(7));
		if (result.isJsonArray()) {
			JsonArray listeningDtoList = result.getAsJsonArray();
		}

		result = APIService.searchConversation(new Long(7));
		if (result.isJsonArray()) {
			JsonArray conversationDtoList = result.getAsJsonArray();
		}

		result = APIService.searchKanji(new Long(7));
		if (result.isJsonArray()) {
			JsonArray kanjiDtoList = result.getAsJsonArray();
		}

		result = APIService.searchExercise(new Long(7));
		if (result.isJsonArray()) {
			JsonArray exerciseDtoList = result.getAsJsonArray();
		}

		result =
			APIService.createTest(
				new Long(7), new Long(1), new Long(2),
				new Long(3), new Long(4), new Long(5),
				new Long(6));
		if (result.isJsonArray()) {
			JsonArray testList = result.getAsJsonArray();
		}

		// registerAccount
		Boolean result0 = APIService.registerAccount("trungln", "123", "torung");
		Boolean result1 = APIService.registerAccount("trungln", "123", "torung", 27, new byte[] {1, 2, 3, 4});
		Boolean result2 = APIService.registerAccount("trungln123", "123", "torung", 27, new byte[] {1, 2, 3, 4});

		JsonObject userDto1 = APIService.findUserInfo("trungln123", "123");
		userDto1.get("profileImage").getAsString().getBytes();


		// registerUsersLesson
		Boolean result3 =
			APIService.registerUsersLesson(
				new Long(3), new Long(7), new Date(), null,
				null, "000000", null);
		Boolean result4 =
			APIService.registerUsersLesson(
				new Long(3), new Long(7), null, new Date(),
				true, "123456", new Long(100));

		// selectAvgMark
		Double avgMark1 = APIService.selectAvgMark(new Long(1));
		Double avgMark2 = APIService.selectAvgMark(new Long(3));

		System.out.println();

	}

}
