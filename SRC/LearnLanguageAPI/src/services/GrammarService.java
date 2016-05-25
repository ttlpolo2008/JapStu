package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.GrammarDto;
import enums.CourseType;

public class GrammarService {

	/**
	 * Search Grammar
	 *
	 * @param lessonId
	 * @return List<GrammarDto>
	 */
	public static List<GrammarDto> searchGrammar(Long lessonId) {

		// Result list
		List<GrammarDto> grammarDtoList = new ArrayList<GrammarDto>();

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   T3.GRAMMAR_ID \n");
			sqlQuery.append("  ,T3.LESSON_COURSE_ID \n");
			sqlQuery.append("  ,T3.SYNTAX \n");
			sqlQuery.append("  ,T3.EXPLAIN \n");
			sqlQuery.append("  ,T3.EXAMPLE \n");
			sqlQuery.append("  ,T3.ORDER_INDEX \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LESSON T1 \n");
			sqlQuery.append("  ,T_LESSON_COURSE T2 \n");
			sqlQuery.append("  ,T_GRAMMAR T3 \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       1 = 1 \n");
			sqlQuery.append("   AND T1.LESSON_ID = T2.LESSON_ID \n");
			sqlQuery.append("   AND T2.LESSON_COURSE_ID = T3.LESSON_COURSE_ID \n");
			sqlQuery.append("   AND T1.LESSON_ID = ? \n");
			sqlQuery.append("   AND T2.COURSE_TYPE = ? \n");
			sqlQuery.append(" ORDER BY \n");
			sqlQuery.append("   T3.ORDER_INDEX \n");

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Add condition
			stmt.setLong(1, lessonId);
			stmt.setString(2, CourseType.GRAMMAR.getCode());

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				GrammarDto grammarDto = new GrammarDto();
				grammarDto.setGrammarId(rs.getLong("GRAMMAR_ID"));
				grammarDto.setLessonCourseId(rs.getLong("LESSON_COURSE_ID"));
				grammarDto.setSyntax(rs.getString("SYNTAX"));
				grammarDto.setExplain(rs.getString("EXPLAIN"));
				grammarDto.setExample(rs.getString("EXAMPLE"));
				grammarDto.setOrderIndex(rs.getInt("ORDER_INDEX"));

				// Add Dto to List
				grammarDtoList.add(grammarDto);
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

		return grammarDtoList;
	}
}
