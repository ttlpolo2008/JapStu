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
	 * Count Exercise by lessonId
	 *
	 * @param lessonId
	 * @return Long
	 */
	public static Long countExercise(Long lessonId) {

		// if [lessonId = null], return 0
		if (lessonId == null) {
			return new Long(0);
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   COUNT(*) AS CNT \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_EXERCISE \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       LESSON_ID = ? \n");

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Add condition
			stmt.setLong(1, lessonId);

			// Execute query
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getLong("CNT");
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

		return new Long(0);
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
		sqlQuery.append("   AND T_LESSON.LESSON_ID = ? \n");
		if (questionType != null) {
			sqlQuery.append("   AND T_EXERCISE.QUESTION_TYPE = ? \n");
			sqlQuery.append(" LIMIT ? \n");
			sqlQuery.append(" UNION \n");
		} else {
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
			sqlQuery.append("   T2.EXERCISE_ID \n");

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
	 * Register all Exercise
	 *
	 * @param exerciseDtoList
	 * @param deletedList
	 * @param lessonId
	 * @return Boolean True:Success / False:Error
	 */
	public static Boolean registerAll(List<ExerciseDto> exerciseDtoList,
									  List<Long> deletedList,
									  Long lessonId) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Register Data
			Integer orderIndex = 1;
			for (ExerciseDto exerciseDto : exerciseDtoList) {

				if (exerciseDto.getExerciseId() == null) {
					// Register new data
					exerciseDto.setLessonId(lessonId);
					if (!ExerciseService.insertExercise(exerciseDto)) {
						conn.rollback();
						return false;
					}

				} else if (exerciseDto.getIsChange()) {
					// Update data
					if (!ExerciseService.updateExercise(exerciseDto)) {
						conn.rollback();
						return false;
					}

				}

				orderIndex++;
			}

			// Delete data
			for (Long exerciseId : deletedList) {
				// Delete data
				if (!ExerciseService.deleteExercise(exerciseId)) {
					conn.rollback();
					return false;
				}
			}

			// Commit Transaction
			conn.commit();

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				// Rollback Transaction
				if (conn != null) {
					conn.rollback();
				}
			} catch(Exception ex1) {
			}
		}

		return false;
	}

	/**
	 * Insert Exercise
	 *
	 * @param exerciseDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean insertExercise(ExerciseDto exerciseDto) {

		// if [exerciseDto = null], return false
		if (exerciseDto == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" INSERT INTO `T_EXERCISE` \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     `LESSON_ID` \n");
			sqlQuery.append("    ,`QUESTION_TYPE` \n");
			sqlQuery.append("    ,`ANSWER_TYPE` \n");
			sqlQuery.append("    ,`QUESTION_CONTENT` \n");
			sqlQuery.append("    ,`QUESTION_CONTENT_FILE` \n");
			sqlQuery.append("    ,`MARK` \n");
			sqlQuery.append("    ,`TIME` \n");
			sqlQuery.append("    ,`ANSWER_CHOOSE` \n");
			sqlQuery.append("    ,`ANSWER_1` \n");
			sqlQuery.append("    ,`ANSWER_2` \n");
			sqlQuery.append("    ,`ANSWER_3` \n");
			sqlQuery.append("    ,`ANSWER_4` \n");
			sqlQuery.append("    ,`ANSWER_5` \n");
			sqlQuery.append("   ) \n");
			sqlQuery.append(" VALUES \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     ? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("   ) \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, exerciseDto.getLessonId());
			stmt.setString(2, exerciseDto.getQuestionType());
			stmt.setString(3, exerciseDto.getAnswerType());
			stmt.setString(4, exerciseDto.getQuestionContent());
			stmt.setString(5, exerciseDto.getQuestionContentFile());
			if (exerciseDto.getMark() == null) {
				stmt.setNull(6, java.sql.Types.INTEGER);
			} else {
				stmt.setInt(6, exerciseDto.getMark());
			}
			if (exerciseDto.getTime() == null) {
				stmt.setNull(7, java.sql.Types.INTEGER);
			} else {
				stmt.setInt(7, exerciseDto.getTime());
			}
			stmt.setString(8, exerciseDto.getAnswerChoose());
			stmt.setString(9, exerciseDto.getAnswer1());
			stmt.setString(10, exerciseDto.getAnswer2());
			stmt.setString(11, exerciseDto.getAnswer3());
			stmt.setString(12, exerciseDto.getAnswer4());
			stmt.setString(13, exerciseDto.getAnswer5());

			// Execute SQL
			int cnt = stmt.executeUpdate();
			if (cnt > 0) {
				return true;
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * Update Exercise
	 *
	 * @param exerciseDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean updateExercise(ExerciseDto exerciseDto) {

		// if [exerciseDto = null] or [exerciseId = null], return false
		if (exerciseDto == null || exerciseDto.getExerciseId() == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" UPDATE `T_EXERCISE` \n");
			sqlQuery.append(" SET \n");
			sqlQuery.append("     `QUESTION_TYPE` = ? \n");
			sqlQuery.append("    ,`ANSWER_TYPE` = ? \n");
			sqlQuery.append("    ,`QUESTION_CONTENT` = ? \n");
			sqlQuery.append("    ,`QUESTION_CONTENT_FILE` = ? \n");
			sqlQuery.append("    ,`MARK` = ? \n");
			sqlQuery.append("    ,`TIME` = ? \n");
			sqlQuery.append("    ,`ANSWER_CHOOSE` = ? \n");
			sqlQuery.append("    ,`ANSWER_1` = ? \n");
			sqlQuery.append("    ,`ANSWER_2` = ? \n");
			sqlQuery.append("    ,`ANSWER_3` = ? \n");
			sqlQuery.append("    ,`ANSWER_4` = ? \n");
			sqlQuery.append("    ,`ANSWER_5` = ? \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `EXERCISE_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setString(1, exerciseDto.getQuestionType());
			stmt.setString(2, exerciseDto.getAnswerType());
			stmt.setString(3, exerciseDto.getQuestionContent());
			stmt.setString(4, exerciseDto.getQuestionContentFile());
			if (exerciseDto.getMark() == null) {
				stmt.setNull(5, java.sql.Types.INTEGER);
			} else {
				stmt.setInt(5, exerciseDto.getMark());
			}
			if (exerciseDto.getTime() == null) {
				stmt.setNull(6, java.sql.Types.INTEGER);
			} else {
				stmt.setInt(6, exerciseDto.getTime());
			}
			stmt.setString(7, exerciseDto.getAnswerChoose());
			stmt.setString(8, exerciseDto.getAnswer1());
			stmt.setString(9, exerciseDto.getAnswer2());
			stmt.setString(10, exerciseDto.getAnswer3());
			stmt.setString(11, exerciseDto.getAnswer4());
			stmt.setString(12, exerciseDto.getAnswer5());
			stmt.setLong(13, exerciseDto.getExerciseId());

			// Execute SQL
			int cnt = stmt.executeUpdate();
			if (cnt > 0) {
				return true;
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * Delete Exercise
	 *
	 * @param exerciseId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean deleteExercise(Long exerciseId) {

		// if [exerciseId = null], return false
		if (exerciseId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" DELETE FROM `T_EXERCISE` \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `EXERCISE_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, exerciseId);

			// Execute SQL
			int cnt = stmt.executeUpdate();
			if (cnt > 0) {
				return true;
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
