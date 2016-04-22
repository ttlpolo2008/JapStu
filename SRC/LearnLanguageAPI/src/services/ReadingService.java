package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.LessonCourseDto;
import dto.ReadingDto;
import enums.CourseType;

public class ReadingService {

	/**
	 * Search Reading
	 *
	 * @param lessonId
	 * @return List<ReadingDto>
	 */
	public static List<ReadingDto> searchReading(Long lessonId) {

		// Result list
		List<ReadingDto> readingDtoList = new ArrayList<ReadingDto>();

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   T3.READING_ID \n");
			sqlQuery.append("  ,T3.LESSON_COURSE_ID \n");
			sqlQuery.append("  ,T3.CONTENT \n");
			sqlQuery.append("  ,T3.CONTENT_PRON \n");
			sqlQuery.append("  ,T3.CONTENT_TRANSLATE \n");
			sqlQuery.append("  ,T3.ORDER_INDEX \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LESSON T1 \n");
			sqlQuery.append("  ,T_LESSON_COURSE T2 \n");
			sqlQuery.append("  ,T_READING T3 \n");
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
			stmt.setString(2, CourseType.READING.getCode());

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				ReadingDto readingDto = new ReadingDto();
				readingDto.setReadingId(rs.getLong("READING_ID"));
				readingDto.setLessonCourseId(rs.getLong("LESSON_COURSE_ID"));
				readingDto.setContent(rs.getString("CONTENT"));
				readingDto.setContentPron(rs.getString("CONTENT_PRON"));
				readingDto.setContentTranslate(rs.getString("CONTENT_TRANSLATE"));
				readingDto.setOrderIndex(rs.getInt("ORDER_INDEX"));

				// Add Dto to List
				readingDtoList.add(readingDto);
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

		return readingDtoList;
	}

	/**
	 * Register all Reading
	 *
	 * @param readingDtoList
	 * @param deletedList
	 * @param lessonId
	 * @return Boolean True:Success / False:Error
	 */
	public static Boolean registerAll(List<ReadingDto> readingDtoList,
									  List<Long> deletedList,
									  Long lessonId) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Insert LESSON_COURSE
			Long lessonCourseId = null;
			if (!readingDtoList.isEmpty()) {
				if (LessonCourseService.countLessonCourse(lessonId, CourseType.READING) == 0) {
					lessonCourseId =
						LessonCourseService.insertLessonCourse(
							lessonId, CourseType.READING);
				} else {
					List<LessonCourseDto> lessonCourseDtoList =
						LessonCourseService.searchLesson(
							lessonId, CourseType.READING);
					if (!lessonCourseDtoList.isEmpty()) {
						lessonCourseId = lessonCourseDtoList.get(0).getLessonCourseId();
					}
				}
			}

			// Register Data
			Integer orderIndex = 1;
			for (ReadingDto readingDto : readingDtoList) {

				if (readingDto.getReadingId() == null) {
					// Register new data
					readingDto.setLessonCourseId(lessonCourseId);
					readingDto.setOrderIndex(orderIndex);
					if (!ReadingService.insertReading(readingDto)) {
						conn.rollback();
						return false;
					}

				} else if (!orderIndex.equals(readingDto.getOrderIndex())
						|| readingDto.getIsChange()) {
					// Update data
					readingDto.setOrderIndex(orderIndex);
					if (!ReadingService.updateReading(readingDto)) {
						conn.rollback();
						return false;
					}

				}

				orderIndex++;
			}

			// Delete data
			for (Long readingId : deletedList) {
				// Delete data
				if (!ReadingService.deleteReading(readingId)) {
					conn.rollback();
					return false;
				}
			}

			// Delete LESSON_COURSE
			if (readingDtoList.isEmpty()) {
				LessonCourseService.deleteLessonCourse(lessonId, CourseType.READING);
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
	 * Insert Reading
	 *
	 * @param readingDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean insertReading(ReadingDto readingDto) {

		// if [readingDto = null], return false
		if (readingDto == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" INSERT INTO `T_READING` \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     `LESSON_COURSE_ID` \n");
			sqlQuery.append("    ,`CONTENT` \n");
			sqlQuery.append("    ,`CONTENT_PRON` \n");
			sqlQuery.append("    ,`CONTENT_TRANSLATE` \n");
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
			stmt.setLong(1, readingDto.getLessonCourseId());
			stmt.setString(2, readingDto.getContent());
			stmt.setString(3, readingDto.getContentPron());
			stmt.setString(4, readingDto.getContentTranslate());
			stmt.setInt(5, readingDto.getOrderIndex());

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
	 * Update Reading
	 *
	 * @param readingDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean updateReading(ReadingDto readingDto) {

		// if [readingDto = null] or [readingId = null], return false
		if (readingDto == null || readingDto.getReadingId() == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" UPDATE `T_READING` \n");
			sqlQuery.append(" SET \n");
			sqlQuery.append("     `CONTENT` = ? \n");
			sqlQuery.append("    ,`CONTENT_PRON` = ? \n");
			sqlQuery.append("    ,`CONTENT_TRANSLATE` = ? \n");
			sqlQuery.append("    ,`ORDER_INDEX` = ? \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `READING_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setString(1, readingDto.getContent());
			stmt.setString(2, readingDto.getContentPron());
			stmt.setString(3, readingDto.getContentTranslate());
			stmt.setInt(4, readingDto.getOrderIndex());
			stmt.setLong(5, readingDto.getReadingId());

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
	 * Delete Reading
	 *
	 * @param readingId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean deleteReading(Long readingId) {

		// if [readingId = null], return false
		if (readingId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" DELETE FROM `T_READING` \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `READING_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, readingId);

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
