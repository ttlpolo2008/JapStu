package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.LessonCourseDto;
import enums.CourseType;
import util.StringUtil;

public class LessonCourseService {

	/**
	 * Count LessonCourse by lessonId
	 *
	 * @param lessonId
	 * @param courseType
	 * @return Long
	 */
	public static Long countLessonCourse(Long lessonId, CourseType courseType) {

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
			sqlQuery.append("   T_LESSON_COURSE \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       LESSON_ID = ? \n");

			// Add condition SQL
			if (courseType != null) {
				sqlQuery.append("  AND COURSE_TYPE = ? \n");
			}

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Add condition
			stmt.setLong(1, lessonId);
			if (courseType != null) {
				stmt.setString(2, courseType.getCode());
			}

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
	 * Search LessonCourse
	 *
	 * @param lessonId
	 * @param courseType
	 * @return List<LessonCourseDto>
	 */
	public static List<LessonCourseDto> searchLesson(Long lessonId, CourseType courseType) {

		// Result list
		List<LessonCourseDto> lessonCourseDtoList = new ArrayList<LessonCourseDto>();

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   LESSON_COURSE_ID \n");
			sqlQuery.append("  ,LESSON_ID \n");
			sqlQuery.append("  ,COURSE_TYPE \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LESSON_COURSE \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       1 = 1 \n");
			sqlQuery.append("  AND LESSON_ID = ? \n");
			if (!StringUtil.isNullOrEmpty(courseType)) {
				sqlQuery.append("  AND COURSE_TYPE = ? \n");
			}

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Add condition
			stmt.setLong(1, lessonId);
			if (courseType != null) {
				stmt.setString(2, courseType.getCode());
			}

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				LessonCourseDto lessonCourseDto = new LessonCourseDto();
				lessonCourseDto.setLessonCourseId(rs.getLong("LESSON_COURSE_ID"));
				lessonCourseDto.setLessonId(rs.getLong("LESSON_ID"));
				lessonCourseDto.setCourseType(rs.getString("COURSE_TYPE"));

				// Add Dto to List
				lessonCourseDtoList.add(lessonCourseDto);
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

		return lessonCourseDtoList;
	}

	/**
	 * Insert LessonCourse
	 *
	 * @param lessonId
	 * @param courseType
	 * @return Long inserted LessonCourseId
	 */
	public static Long insertLessonCourse(Long lessonId, CourseType courseType) {

		Long lessonCourseId = null;

		// if [lessonId = null] or [courseType = null], return false
		if (lessonId == null || courseType == null) {
			return null;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" INSERT INTO T_LESSON_COURSE \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     LESSON_ID \n");
			sqlQuery.append("    ,COURSE_TYPE \n");
			sqlQuery.append("   ) \n");
			sqlQuery.append(" VALUES \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     ? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("   ) \n");

			// Create Statement
			PreparedStatement stmt =
				conn.prepareStatement(
					sqlQuery.toString(), Statement.RETURN_GENERATED_KEYS);

			// Edit parameter
			stmt.setLong(1, lessonId);
			stmt.setString(2, courseType.getCode());

			// Execute SQL
			int cnt = stmt.executeUpdate();
			if (cnt > 0) {
				ResultSet rs = stmt.getGeneratedKeys();
				rs.next();
				lessonCourseId = rs.getLong(1);
				rs.close();
			}
			stmt.close();

		} catch(Exception ex) {
			ex.printStackTrace();
		}

		return lessonCourseId;
	}

	/**
	 * Delete LessonCourse
	 *
	 * @param lessonId
	 * @param courseType
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean deleteLessonCourse(Long lessonId, CourseType courseType) {

		// if [lessonId = null], return false
		if (lessonId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" DELETE FROM `T_LESSON_COURSE` \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `LESSON_ID` = ? \n");
			if (!StringUtil.isNullOrEmpty(courseType)) {
				sqlQuery.append("  AND `COURSE_TYPE` = ? \n");
			}

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, lessonId);
			if (courseType != null) {
				stmt.setString(2, courseType.getCode());
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
}
