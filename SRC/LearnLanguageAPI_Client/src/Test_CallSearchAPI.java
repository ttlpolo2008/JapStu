import java.util.List;
import java.util.Map;

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
import services.APIService;

public class Test_CallSearchAPI {

	public static void main(String []args) {

		UserDto userDto = APIService.findUserInfo("trungln", "123");
		Map<Long, LearnDto> studyMap = APIService.searchStudyHistoryAll(new Long(1));
		LearnDto studyLast = APIService.searchStudyHistoryLast(new Long(1));
		LessonDto lessonDto = APIService.findNextLesson(new Long(7));
		List<VocabularyDto> vocabularyDtoList = APIService.searchVocabulary(new Long(7));
		List<GrammarDto> grammarDtoList = APIService.searchGrammar(new Long(7));
		List<ReadingDto> readingDtoList = APIService.searchReading(new Long(7));
		List<ListeningDto> listeningDtoList = APIService.searchListening(new Long(7));
		List<ConversationDto> conversationDtoList = APIService.searchConversation(new Long(7));
		List<KanjiDto> kanjiDtoList = APIService.searchKanji(new Long(7));
		List<ExerciseDto> exerciseDtoList = APIService.searchExercise(new Long(7));
		List<ExerciseDto> testList =
				APIService.createTest(
						new Long(7), new Long(1), new Long(2),
						new Long(3), new Long(4), new Long(5),
						new Long(6));

		System.out.println();

	}

}
