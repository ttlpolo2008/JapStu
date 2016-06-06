package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.ExerciseDto;
import enums.QuestionType;
import util.StringUtil;

public class ExerciseService {

	/**
	 * Search Exercise
	 *
	 * @param lessonId
	 * @return List<ExerciseDto>
	 */
	public static List<ExerciseDto> searchExercise(Long lessonId) {

		// Result list
		List<ExerciseDto> exerciseDtoList = new ArrayList<ExerciseDto>();

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   T2.EXERCISE_ID \n");
			sqlQuery.append("  ,T2.LESSON_ID \n");
			sqlQuery.append("  ,T2.QUESTION_TYPE \n");
			sqlQuery.append("  ,T2.ANSWER_TYPE \n");
			sqlQuery.append("  ,T2.QUESTION_CONTENT \n");
			sqlQuery.append("  ,T2.QUESTION_CONTENT_FILE \n");
			sqlQuery.append("  ,T2.MARK \n");
			sqlQuery.append("  ,T2.TIME \n");
			sqlQuery.append("  ,T2.ANSWER_CHOOSE \n");
			sqlQuery.append("  ,T2.ANSWER_1 \n");
			sqlQuery.append("  ,T2.ANSWER_2 \n");
			sqlQuery.append("  ,T2.ANSWER_3 \n");
			sqlQuery.append("  ,T2.ANSWER_4 \n");
			sqlQuery.append("  ,T2.ANSWER_5 \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LESSON T1 \n");
			sqlQuery.append("  ,T_EXERCISE T2 \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       1 = 1 \n");
			sqlQuery.append("   AND T1.LESSON_ID = T2.LESSON_ID \n");
			sqlQuery.append("   AND T1.LESSON_ID = ? \n");
			sqlQuery.append(" ORDER BY \n");
			sqlQuery.append("   RAND() \n");

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Add condition
			stmt.setLong(1, lessonId);

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				ExerciseDto exerciseDto = new ExerciseDto();
				exerciseDto.setExerciseId(rs.getLong("EXERCISE_ID"));
				exerciseDto.setLessonId(rs.getLong("LESSON_ID"));
				exerciseDto.setQuestionType(rs.getString("QUESTION_TYPE"));
				exerciseDto.setAnswerType(rs.getString("ANSWER_TYPE"));
				exerciseDto.setQuestionContent(rs.getString("QUESTION_CONTENT"));
				exerciseDto.setQuestionContentFile(rs.getString("QUESTION_CONTENT_FILE"));
				exerciseDto.setMark(rs.getInt("MARK"));
				exerciseDto.setTime(rs.getInt("TIME"));
				exerciseDto.setAnswerChoose(rs.getString("ANSWER_CHOOSE"));
				exerciseDto.setAnswer1(rs.getString("ANSWER_1"));
				exerciseDto.setAnswer2(rs.getString("ANSWER_2"));
				exerciseDto.setAnswer3(rs.getString("ANSWER_3"));
				exerciseDto.setAnswer4(rs.getString("ANSWER_4"));
				exerciseDto.setAnswer5(rs.getString("ANSWER_5"));

				// Get File
				if (!StringUtil.isNullOrEmpty(exerciseDto.getQuestionContentFile())) {
					exerciseDto.setQuestionContentFileStream(
						StringUtil.readUploadFile(exerciseDto.getQuestionContentFile()));
				}

				// Add Dto to List
				exerciseDtoList.add(exerciseDto);
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception ex) {
			}
		}

		return exerciseDtoList;
	}

	/**
	 * Create test
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
	public static List<ExerciseDto> createTest(
			Long lessonId, Long vocabularyNumber, Long grammarNumber,
			Long readingNumber, Long listeningNumber, Long conversationNumber,
			Long kanjiNumber) {

		// Result list
		List<ExerciseDto> exerciseDtoList = new ArrayList<ExerciseDto>();

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();

			// vocabularyNumber
			if (vocabularyNumber != null && vocabularyNumber > 0) {
				sqlQuery.append(createTest_CreateSQL(QuestionType.VOCABULARY));
			}
			// grammarNumber
			if (grammarNumber != null && grammarNumber > 0) {
				sqlQuery.append(createTest_CreateSQL(QuestionType.GRAMMAR));
			}
			// readingNumber
			if (readingNumber != null && readingNumber > 0) {
				sqlQuery.append(createTest_CreateSQL(QuestionType.READING));
			}
			// listeningNumber
			if (listeningNumber != null && listeningNumber > 0) {
				sqlQuery.append(createTest_CreateSQL(QuestionType.LISTENING));
			}
			// conversationNumber
			if (conversationNumber != null && conversationNumber > 0) {
				sqlQuery.append(createTest_CreateSQL(QuestionType.CONVERSATION));
			}
			// kanjiNumber
			if (kanjiNumber != null && kanjiNumber > 0) {
				sqlQuery.append(createTest_CreateSQL(QuestionType.KANJI));
			}
			// Default
			sqlQuery.append(createTest_CreateSQL(null));

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Add condition
			int conditionIndex = 0;

			// vocabularyNumber
			if (vocabularyNumber != null && vocabularyNumber > 0) {
				createTest_AddCondition(stmt, conditionIndex, lessonId,
										QuestionType.VOCABULARY, vocabularyNumber);
				conditionIndex += 3;
			}
			// grammarNumber
			if (grammarNumber != null && grammarNumber > 0) {
				createTest_AddCondition(stmt, conditionIndex, lessonId,
										QuestionType.GRAMMAR, grammarNumber);
				conditionIndex += 3;
			}
			// readingNumber
			if (readingNumber != null && readingNumber > 0) {
				createTest_AddCondition(stmt, conditionIndex, lessonId,
										QuestionType.READING, readingNumber);
				conditionIndex += 3;
			}
			// listeningNumber
			if (listeningNumber != null && listeningNumber > 0) {
				createTest_AddCondition(stmt, conditionIndex, lessonId,
										QuestionType.LISTENING, listeningNumber);
				conditionIndex += 3;
			}
			// conversationNumber
			if (conversationNumber != null && conversationNumber > 0) {
				createTest_AddCondition(stmt, conditionIndex, lessonId,
										QuestionType.CONVERSATION, conversationNumber);
				conditionIndex += 3;
			}
			// kanjiNumber
			if (kanjiNumber != null && kanjiNumber > 0) {
				createTest_AddCondition(stmt, conditionIndex, lessonId,
										QuestionType.KANJI, kanjiNumber);
				conditionIndex += 3;
			}
			// Default
			stmt.setLong(conditionIndex + 1, lessonId);

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				ExerciseDto exerciseDto = new ExerciseDto();
				exerciseDto.setExerciseId(rs.getLong("EXERCISE_ID"));
				exerciseDto.setLessonId(rs.getLong("LESSON_ID"));
				exerciseDto.setQuestionType(rs.getString("QUESTION_TYPE"));
				exerciseDto.setAnswerType(rs.getString("ANSWER_TYPE"));
				exerciseDto.setQuestionContent(rs.getString("QUESTION_CONTENT"));
				exerciseDto.setQuestionContentFile(rs.getString("QUESTION_CONTENT_FILE"));
				exerciseDto.setMark(rs.getInt("MARK"));
				exerciseDto.setTime(rs.getInt("TIME"));
				exerciseDto.setAnswerChoose(rs.getString("ANSWER_CHOOSE"));
				exerciseDto.setAnswer1(rs.getString("ANSWER_1"));
				exerciseDto.setAnswer2(rs.getString("ANSWER_2"));
				exerciseDto.setAnswer3(rs.getString("ANSWER_3"));
				exerciseDto.setAnswer4(rs.getString("ANSWER_4"));
				exerciseDto.setAnswer5(rs.getString("ANSWER_5"));

				// Get File
				if (!StringUtil.isNullOrEmpty(exerciseDto.getQuestionContentFile())) {
					exerciseDto.setQuestionContentFileStream(
						StringUtil.readUploadFile(exerciseDto.getQuestionContentFile()));
				}

				// Add Dto to List
				exerciseDtoList.add(exerciseDto);
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception ex) {
			}
		}

		return exerciseDtoList;
	}

	private static String createTest_CreateSQL(QuestionType questionType) {
		StringBuffer sqlQuery = new StringBuffer();
		sqlQuery.append(" SELECT \n");
		sqlQuery.append("   T_EXERCISE.EXERCISE_ID \n");
		sqlQuery.append("  ,T_EXERCISE.LESSON_ID \n");
		sqlQuery.append("  ,T_EXERCISE.QUESTION_TYPE \n");
		sqlQuery.append("  ,T_EXERCISE.ANSWER_TYPE \n");
		sqlQuery.append("  ,T_EXERCISE.QUESTION_CONTENT \n");
		sqlQuery.append("  ,T_EXERCISE.QUESTION_CONTENT_FILE \n");
		sqlQuery.append("  ,T_EXERCISE.MARK \n");
		sqlQuery.append("  ,T_EXERCISE.TIME \n");
		sqlQuery.append("  ,T_EXERCISE.ANSWER_CHOOSE \n");
		sqlQuery.append("  ,T_EXERCISE.ANSWER_1 \n");
		sqlQuery.append("  ,T_EXERCISE.ANSWER_2 \n");
		sqlQuery.append("  ,T_EXERCISE.ANSWER_3 \n");
		sqlQuery.append("  ,T_EXERCISE.ANSWER_4 \n");
		sqlQuery.append("  ,T_EXERCISE.ANSWER_5 \n");
		sqlQuery.append(" FROM \n");
		sqlQuery.append("   T_LESSON \n");
		sqlQuery.append("  ,T_EXERCISE \n");
		sqlQuery.append(" WHERE \n");
		sqlQuery.append("       1 = 1 \n");
		sqlQuery.append("   AND T_LESSON.LESSON_ID = T_EXERCISE.LESSON_ID \n");
		sqlQuery.append("   AND T_LESSON.LESSON_NO <= \n");
		sqlQuery.append("                            ( \n");
		sqlQuery.append("                             SELECT \n");
		sqlQuery.append("                               ST.LESSON_NO \n");
		sqlQuery.append("                             FROM \n");
		sqlQuery.append("                               T_LESSON ST \n");
		sqlQuery.append("                             WHERE \n");
		sqlQuery.append("                               ST.LESSON_ID = ? \n");
		sqlQuery.append("                            ) \n");
		if (questionType != null) {
			sqlQuery.append("   AND T_EXERCISE.QUESTION_TYPE = ? \n");
			sqlQuery.append(" ORDER BY RAND() \n");
			sqlQuery.append(" LIMIT ? \n");
			sqlQuery.append(" UNION \n");
		} else {
			sqlQuery.append(" ORDER BY RAND() \n");
			sqlQuery.append(" LIMIT 50 \n");
		}

		return sqlQuery.toString();
	}

	private static void createTest_AddCondition(
			PreparedStatement stmt, int conditionIndex, Long lessonId,
			QuestionType questionType, Long number) throws Exception {

		stmt.setLong(conditionIndex + 1, lessonId);
		stmt.setString(conditionIndex + 2, questionType.getCode());
		stmt.setLong(conditionIndex + 3, number);

	}
}
