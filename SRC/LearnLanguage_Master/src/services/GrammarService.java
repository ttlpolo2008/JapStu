package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.GrammarDto;
import dto.LessonCourseDto;
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

	/**
	 * Register all Grammar
	 *
	 * @param grammarDtoList
	 * @param deletedList
	 * @param lessonId
	 * @return Boolean True:Success / False:Error
	 */
	public static Boolean registerAll(List<GrammarDto> grammarDtoList,
									  List<Long> deletedList,
									  Long lessonId) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Insert LESSON_COURSE
			Long lessonCourseId = null;
			if (!grammarDtoList.isEmpty()) {
				if (LessonCourseService.countLessonCourse(lessonId, CourseType.GRAMMAR) == 0) {
					lessonCourseId =
						LessonCourseService.insertLessonCourse(
							lessonId, CourseType.GRAMMAR);
				} else {
					List<LessonCourseDto> lessonCourseDtoList =
						LessonCourseService.searchLesson(
							lessonId, CourseType.GRAMMAR);
					if (!lessonCourseDtoList.isEmpty()) {
						lessonCourseId = lessonCourseDtoList.get(0).getLessonCourseId();
					}
				}
			}

			// Register Data
			Integer orderIndex = 1;
			for (GrammarDto grammarDto : grammarDtoList) {

				if (grammarDto.getGrammarId() == null) {
					// Register new data
					grammarDto.setLessonCourseId(lessonCourseId);
					grammarDto.setOrderIndex(orderIndex);
					if (!GrammarService.insertGrammar(grammarDto)) {
						conn.rollback();
						return false;
					}

				} else if (!orderIndex.equals(grammarDto.getOrderIndex())
						|| grammarDto.getIsChange()) {
					// Update data
					grammarDto.setOrderIndex(orderIndex);
					if (!GrammarService.updateGrammar(grammarDto)) {
						conn.rollback();
						return false;
					}

				}

				orderIndex++;
			}

			// Delete data
			for (Long grammarId : deletedList) {
				// Delete data
				if (!GrammarService.deleteGrammar(grammarId)) {
					conn.rollback();
					return false;
				}
			}

			// Delete LESSON_COURSE
			if (grammarDtoList.isEmpty()) {
				LessonCourseService.deleteLessonCourse(lessonId, CourseType.GRAMMAR);
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
	 * Insert Grammar
	 *
	 * @param grammarDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean insertGrammar(GrammarDto grammarDto) {

		// if [grammarDto = null], return false
		if (grammarDto == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" INSERT INTO `T_GRAMMAR` \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     `LESSON_COURSE_ID` \n");
			sqlQuery.append("    ,`SYNTAX` \n");
			sqlQuery.append("    ,`EXPLAIN` \n");
			sqlQuery.append("    ,`EXAMPLE` \n");
			sqlQuery.append("    ,`ORDER_INDEX` \n");
			sqlQuery.append("   ) \n");
			sqlQuery.append(" VALUES \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     ? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("   ) \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, grammarDto.getLessonCourseId());
			stmt.setString(2, grammarDto.getSyntax());
			stmt.setString(3, grammarDto.getExplain());
			stmt.setString(4, grammarDto.getExample());
			stmt.setInt(5, grammarDto.getOrderIndex());

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
	 * Update Grammar
	 *
	 * @param grammarDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean updateGrammar(GrammarDto grammarDto) {

		// if [grammarDto = null] or [grammarId = null], return false
		if (grammarDto == null || grammarDto.getGrammarId() == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" UPDATE `T_GRAMMAR` \n");
			sqlQuery.append(" SET \n");
			sqlQuery.append("     `SYNTAX` = ? \n");
			sqlQuery.append("    ,`EXPLAIN` = ? \n");
			sqlQuery.append("    ,`EXAMPLE` = ? \n");
			sqlQuery.append("    ,`ORDER_INDEX` = ? \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `GRAMMAR_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setString(1, grammarDto.getSyntax());
			stmt.setString(2, grammarDto.getExplain());
			stmt.setString(3, grammarDto.getExample());
			stmt.setInt(4, grammarDto.getOrderIndex());
			stmt.setLong(5, grammarDto.getGrammarId());

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
	 * Delete Grammar
	 *
	 * @param grammarId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean deleteGrammar(Long grammarId) {

		// if [grammarId = null], return false
		if (grammarId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" DELETE FROM `T_GRAMMAR` \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `GRAMMAR_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, grammarId);

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
