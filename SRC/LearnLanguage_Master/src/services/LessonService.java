package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.LessonDto;
import util.StringUtil;

public class LessonService {

	/**
	 * Search Lesson
	 *
	 * @param conditionDto
	 * @return List<LessonDto>
	 */
	public static List<LessonDto> searchLesson(LessonDto conditionDto) {

		// Result list
		List<LessonDto> lessonDtoList = new ArrayList<LessonDto>();

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   LESSON_ID \n");
			sqlQuery.append("  ,LESSON_NO \n");
			sqlQuery.append("  ,LESSON_NAME \n");
			sqlQuery.append("  ,PASS_SCORE \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LESSON \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       1 = 1 \n");

			// Add condition SQL
			if (conditionDto != null) {
				if (!StringUtil.isNullOrEmpty(conditionDto.getLessonId())) {
					sqlQuery.append("  AND LESSON_ID = ? \n");
				}
				if (!StringUtil.isNullOrEmpty(conditionDto.getLessonNo())) {
					sqlQuery.append("  AND LESSON_NO = ? \n");
				}
				if (!StringUtil.isNullOrEmpty(conditionDto.getLessonName())) {
					sqlQuery.append("  AND LESSON_NAME LIKE ? \n");
				}
			}

			// Order
			sqlQuery.append(" ORDER BY \n");
			sqlQuery.append("   LESSON_NO \n");

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Add condition
			if (conditionDto != null) {
				int conditionIndex = 1;
				if (!StringUtil.isNullOrEmpty(conditionDto.getLessonId())) {
					stmt.setLong(conditionIndex, conditionDto.getLessonId());
					conditionIndex++;
				}
				if (!StringUtil.isNullOrEmpty(conditionDto.getLessonNo())) {
					stmt.setInt(conditionIndex, conditionDto.getLessonNo());
					conditionIndex++;
				}
				if (!StringUtil.isNullOrEmpty(conditionDto.getLessonName())) {
					stmt.setString(conditionIndex, "%" + conditionDto.getLessonName() + "%");
					conditionIndex++;
				}
			}

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				LessonDto lessonDto = new LessonDto();
				lessonDto.setLessonId(rs.getLong("LESSON_ID"));
				lessonDto.setLessonNo(rs.getInt("LESSON_NO"));
				lessonDto.setLessonName(rs.getString("LESSON_NAME"));
				lessonDto.setPassScore(rs.getInt("PASS_SCORE"));

				// Add Dto to List
				lessonDtoList.add(lessonDto);
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

		return lessonDtoList;
	}

	/**
	 * Register all Lesson
	 *
	 * @param lessonDtoList
	 * @param deletedList
	 * @return Boolean True:Success / False:Error
	 */
	public static Boolean registerAll(List<LessonDto> lessonDtoList,
									  List<Long> deletedList) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Register Data
			Integer lessonNo = 1;
			for (LessonDto lessonDto : lessonDtoList) {

				if (lessonDto.getLessonId() == null) {
					// Register new data
					lessonDto.setLessonNo(lessonNo);
					if (!LessonService.insertLesson(lessonDto)) {
						conn.rollback();
						return false;
					}

				} else if (!lessonNo.equals(lessonDto.getLessonNo())
						|| lessonDto.getIsChange()) {
					// Update data
					lessonDto.setLessonNo(lessonNo);
					if (!LessonService.updateLesson(lessonDto)) {
						conn.rollback();
						return false;
					}

				}

				lessonNo++;
			}

			// Delete data
			for (Long lessonId : deletedList) {
				// Delete data
				if (!LessonService.deleteLesson(lessonId)) {
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
	 * Insert Lesson
	 *
	 * @param lessonDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean insertLesson(LessonDto lessonDto) {

		// if [lessonDto = null], return false
		if (lessonDto == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" INSERT INTO T_LESSON \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     LESSON_NO \n");
			sqlQuery.append("    ,LESSON_NAME \n");
			sqlQuery.append("    ,PASS_SCORE \n");
			sqlQuery.append("   ) \n");
			sqlQuery.append(" VALUES \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     ? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("   ) \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setInt(1, lessonDto.getLessonNo());
			stmt.setString(2, lessonDto.getLessonName());
			if (lessonDto.getPassScore() == null) {
				stmt.setNull(3, java.sql.Types.INTEGER);
			} else {
				stmt.setInt(3, lessonDto.getPassScore());
			}

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
	 * Update Lesson
	 *
	 * @param lessonDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean updateLesson(LessonDto lessonDto) {

		// if [lessonDto = null] or [lessonNo = null], return false
		if (lessonDto == null || lessonDto.getLessonId() == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" UPDATE T_LESSON \n");
			sqlQuery.append(" SET \n");
			sqlQuery.append("     LESSON_NO = ? \n");
			sqlQuery.append("    ,LESSON_NAME = ? \n");
			sqlQuery.append("    ,PASS_SCORE = ? \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       LESSON_ID = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setInt(1, lessonDto.getLessonNo());
			stmt.setString(2, lessonDto.getLessonName());
			if (lessonDto.getPassScore() == null) {
				stmt.setNull(3, java.sql.Types.INTEGER);
			} else {
				stmt.setInt(3, lessonDto.getPassScore());
			}
			stmt.setLong(4, lessonDto.getLessonId());

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
	 * Delete Lesson
	 *
	 * @param lessonId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean deleteLesson(Long lessonId) {

		// if [lessonId = null], return false
		if (lessonId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" DELETE FROM T_LESSON \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       LESSON_ID = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, lessonId);

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
