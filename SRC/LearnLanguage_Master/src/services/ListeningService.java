package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.LessonCourseDto;
import dto.ListeningDto;
import enums.CourseType;
import util.StringUtil;

public class ListeningService {

	/**
	 * Search Listening
	 *
	 * @param lessonId
	 * @return List<ListeningDto>
	 */
	public static List<ListeningDto> searchListening(Long lessonId) {

		// Result list
		List<ListeningDto> listeningDtoList = new ArrayList<ListeningDto>();

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   T3.LISTENING_ID \n");
			sqlQuery.append("  ,T3.LESSON_COURSE_ID \n");
			sqlQuery.append("  ,T3.CONTENT_FILE \n");
			sqlQuery.append("  ,T3.CONTENT_TRANSLATE \n");
			sqlQuery.append("  ,T3.ORDER_INDEX \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LESSON T1 \n");
			sqlQuery.append("  ,T_LESSON_COURSE T2 \n");
			sqlQuery.append("  ,T_LISTENING T3 \n");
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
			stmt.setString(2, CourseType.LISTENING.getCode());

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				ListeningDto listeningDto = new ListeningDto();
				listeningDto.setListeningId(rs.getLong("LISTENING_ID"));
				listeningDto.setLessonCourseId(rs.getLong("LESSON_COURSE_ID"));
				listeningDto.setContentFile(rs.getString("CONTENT_FILE"));
				listeningDto.setContentTranslate(rs.getString("CONTENT_TRANSLATE"));
				listeningDto.setOrderIndex(rs.getInt("ORDER_INDEX"));

				// Get File
				if (!StringUtil.isNullOrEmpty(listeningDto.getContentFile())) {
					listeningDto.setContentFileStream(
						StringUtil.readUploadFile(listeningDto.getContentFile()));
				}

				// Add Dto to List
				listeningDtoList.add(listeningDto);
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

		return listeningDtoList;
	}

	/**
	 * Register all Listening
	 *
	 * @param listeningDtoList
	 * @param deletedList
	 * @param lessonId
	 * @return Boolean True:Success / False:Error
	 */
	public static Boolean registerAll(List<ListeningDto> listeningDtoList,
									  List<Long> deletedList,
									  Long lessonId) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Insert LESSON_COURSE
			Long lessonCourseId = null;
			if (!listeningDtoList.isEmpty()) {
				if (LessonCourseService.countLessonCourse(lessonId, CourseType.LISTENING) == 0) {
					lessonCourseId =
						LessonCourseService.insertLessonCourse(
							lessonId, CourseType.LISTENING);
				} else {
					List<LessonCourseDto> lessonCourseDtoList =
						LessonCourseService.searchLesson(
							lessonId, CourseType.LISTENING);
					if (!lessonCourseDtoList.isEmpty()) {
						lessonCourseId = lessonCourseDtoList.get(0).getLessonCourseId();
					}
				}
			}

			// Register Data
			Integer orderIndex = 1;
			for (ListeningDto listeningDto : listeningDtoList) {

				if (listeningDto.getListeningId() == null) {
					// Register new data
					listeningDto.setLessonCourseId(lessonCourseId);
					listeningDto.setOrderIndex(orderIndex);
					if (!ListeningService.insertListening(listeningDto)) {
						conn.rollback();
						return false;
					}

				} else if (!orderIndex.equals(listeningDto.getOrderIndex())
						|| listeningDto.getIsChange()) {
					// Update data
					listeningDto.setOrderIndex(orderIndex);
					if (!ListeningService.updateListening(listeningDto)) {
						conn.rollback();
						return false;
					}

				}

				orderIndex++;
			}

			// Delete data
			for (Long listeningId : deletedList) {
				// Delete data
				if (!ListeningService.deleteListening(listeningId)) {
					conn.rollback();
					return false;
				}
			}

			// Delete LESSON_COURSE
			if (listeningDtoList.isEmpty()) {
				LessonCourseService.deleteLessonCourse(lessonId, CourseType.LISTENING);
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
	 * Insert Listening
	 *
	 * @param listeningDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean insertListening(ListeningDto listeningDto) {

		// if [listeningDto = null], return false
		if (listeningDto == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" INSERT INTO `T_LISTENING` \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     `LESSON_COURSE_ID` \n");
			sqlQuery.append("    ,`CONTENT_FILE` \n");
			sqlQuery.append("    ,`CONTENT_TRANSLATE` \n");
			sqlQuery.append("    ,`ORDER_INDEX` \n");
			sqlQuery.append("   ) \n");
			sqlQuery.append(" VALUES \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     ? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("   ) \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, listeningDto.getLessonCourseId());
			stmt.setString(2, listeningDto.getContentFile());
			stmt.setString(3, listeningDto.getContentTranslate());
			stmt.setInt(4, listeningDto.getOrderIndex());

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
	 * Update Listening
	 *
	 * @param listeningDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean updateListening(ListeningDto listeningDto) {

		// if [listeningDto = null] or [listeningId = null], return false
		if (listeningDto == null || listeningDto.getListeningId() == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" UPDATE `T_LISTENING` \n");
			sqlQuery.append(" SET \n");
			sqlQuery.append("     `CONTENT_FILE` = ? \n");
			sqlQuery.append("    ,`CONTENT_TRANSLATE` = ? \n");
			sqlQuery.append("    ,`ORDER_INDEX` = ? \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `LISTENING_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setString(1, listeningDto.getContentFile());
			stmt.setString(2, listeningDto.getContentTranslate());
			stmt.setInt(3, listeningDto.getOrderIndex());
			stmt.setLong(4, listeningDto.getListeningId());

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
	 * Delete Listening
	 *
	 * @param listeningId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean deleteListening(Long listeningId) {

		// if [listeningId = null], return false
		if (listeningId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" DELETE FROM `T_LISTENING` \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `LISTENING_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, listeningId);

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
