package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.StringUtil;
import dao.CommonDAO;
import dto.ExerciseDto;
import enums.QuestionType;

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
}
