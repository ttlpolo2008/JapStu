package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.LearnDto;
import util.StringUtil;

public class LearnService {

	/**
	 * Create table T_LEARN
	 *
	 * @param userId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean createLearn(Long userId) {

		// if [userId = null], return false
		if (userId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" CREATE TABLE `T_LEARN_" + userId + "` ( \n");
			sqlQuery.append("   `LEARN_ID` INT(11) NOT NULL AUTO_INCREMENT \n");
			sqlQuery.append("  ,`LESSON_ID` INT(11) NOT NULL \n");
			sqlQuery.append("  ,`START_DATE` DATE NULL \n");
			sqlQuery.append("  ,`END_DATE` DATE NULL \n");
			sqlQuery.append("  ,`STATUS` TINYINT(1)  NULL \n");
			sqlQuery.append("  ,`COURSE_STATUS` CHAR(6) NULL \n");
			sqlQuery.append("  ,`EXAM_MARK` INT(5) NULL \n");
			sqlQuery.append("  ,PRIMARY KEY (`LEARN_ID`) \n");
			sqlQuery.append("  ,INDEX `T_LEARN_" + userId + "-T_LESSON_COURSE` (`LESSON_ID` ASC) \n");
			sqlQuery.append("  ,CONSTRAINT `T_LEARN_" + userId + "-T_LESSON_COURSE` \n");
			sqlQuery.append("    FOREIGN KEY (`LESSON_ID` )  \n");
			sqlQuery.append("    REFERENCES `T_LESSON` (`LESSON_ID` ) \n");
			sqlQuery.append("    ON DELETE NO ACTION \n");
			sqlQuery.append("    ON UPDATE NO ACTION \n");
			sqlQuery.append("   ) \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Execute SQL
			stmt.executeUpdate();
			return true;

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
	/**
	 * Search all study history
	 *
	 * @param userId
	 * @return List<LearnDto>
	 */
	public static List<LearnDto> searchStudyHistoryAll(Long userId) {

		// Result Map
		List<LearnDto> studyList = new ArrayList<LearnDto>();

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   T1.LEARN_ID \n");
			sqlQuery.append("  ,T1.LESSON_ID \n");
			sqlQuery.append("  ,T1.START_DATE \n");
			sqlQuery.append("  ,T1.END_DATE \n");
			sqlQuery.append("  ,T1.STATUS \n");
			sqlQuery.append("  ,T1.COURSE_STATUS \n");
			sqlQuery.append("  ,T1.EXAM_MARK \n");
			sqlQuery.append("  ,T2.LESSON_NAME \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LEARN_" + userId + " T1 \n");
			sqlQuery.append("  ,T_LESSON T2 \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       1 = 1 \n");
			sqlQuery.append("   AND T1.LESSON_ID = T2.LESSON_ID \n");
			sqlQuery.append(" ORDER BY \n");
			sqlQuery.append("   T1.LEARN_ID \n");

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				LearnDto learnDto = new LearnDto();
				learnDto.setLearnId(rs.getLong("LEARN_ID"));
				learnDto.setLessonId(rs.getLong("LESSON_ID"));
				learnDto.setStartDate(rs.getDate("START_DATE"));
				learnDto.setEndDate(rs.getDate("END_DATE"));
				learnDto.setStatus(rs.getBoolean("STATUS"));
				learnDto.setCourseStatus(rs.getString("COURSE_STATUS"));
				learnDto.setExamMark(rs.getLong("EXAM_MARK"));
				learnDto.setLessonName(rs.getString("LESSON_NAME"));

				// Add Dto to List
				studyList.add(learnDto);
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

		return studyList;
	}

	/**
	 * Search last study history
	 *
	 * @param userId
	 * @return LearnDto
	 */
	public static LearnDto searchStudyHistoryLast(Long userId) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   T1.LEARN_ID \n");
			sqlQuery.append("  ,T1.LESSON_ID \n");
			sqlQuery.append("  ,T1.START_DATE \n");
			sqlQuery.append("  ,T1.END_DATE \n");
			sqlQuery.append("  ,T1.STATUS \n");
			sqlQuery.append("  ,T1.COURSE_STATUS \n");
			sqlQuery.append("  ,T1.EXAM_MARK \n");
			sqlQuery.append("  ,T2.LESSON_NAME \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LEARN_" + userId + " T1 \n");
			sqlQuery.append("  ,T_LESSON T2 \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       1 = 1 \n");
			sqlQuery.append("   AND T1.LESSON_ID = T2.LESSON_ID \n");
			sqlQuery.append(" ORDER BY \n");
			sqlQuery.append("   T1.LEARN_ID DESC \n");
			sqlQuery.append(" LIMIT 1 \n");

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Execute query
			rs = stmt.executeQuery();
			if (rs.next()) {
				// Edit Dto
				LearnDto learnDto = new LearnDto();
				learnDto.setLearnId(rs.getLong("LEARN_ID"));
				learnDto.setLessonId(rs.getLong("LESSON_ID"));
				learnDto.setStartDate(rs.getDate("START_DATE"));
				learnDto.setEndDate(rs.getDate("END_DATE"));
				learnDto.setStatus(rs.getBoolean("STATUS"));
				learnDto.setCourseStatus(rs.getString("COURSE_STATUS"));
				learnDto.setExamMark(rs.getLong("EXAM_MARK"));
				learnDto.setLessonName(rs.getString("LESSON_NAME"));

				return learnDto;
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

		return null;
	}

	/**
	 * Find Lesson by lessonId
	 *
	 * @param lessonId
	 * @return LearnDto
	 */
	public static LearnDto findLessonByLessonId(Long userId, Long lessonId) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   LEARN_ID \n");
			sqlQuery.append("  ,LESSON_ID \n");
			sqlQuery.append("  ,START_DATE \n");
			sqlQuery.append("  ,END_DATE \n");
			sqlQuery.append("  ,STATUS \n");
			sqlQuery.append("  ,COURSE_STATUS \n");
			sqlQuery.append("  ,EXAM_MARK \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LEARN_" + userId + " \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       1 = 1 \n");
			sqlQuery.append("   AND LESSON_ID = ? \n");

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());
			stmt.setLong(1, lessonId);

			// Execute query
			rs = stmt.executeQuery();
			if (rs.next()) {
				// Edit Dto
				LearnDto learnDto = new LearnDto();
				learnDto.setLearnId(rs.getLong("LEARN_ID"));
				learnDto.setLessonId(rs.getLong("LESSON_ID"));
				learnDto.setStartDate(rs.getDate("START_DATE"));
				learnDto.setEndDate(rs.getDate("END_DATE"));
				learnDto.setStatus(rs.getBoolean("STATUS"));
				learnDto.setCourseStatus(rs.getString("COURSE_STATUS"));
				learnDto.setExamMark(rs.getLong("EXAM_MARK"));

				return learnDto;
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

		return null;
	}

	/**
	 * Insert Learn
	 *
	 * @param learnDto
	 * @param userId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean insertLearn(LearnDto learnDto, Long userId) {

		// if [learnDto = null], return false
		if (learnDto == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" INSERT INTO T_LEARN_" + userId + " \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     LESSON_ID \n");
			sqlQuery.append("    ,START_DATE \n");
			sqlQuery.append("    ,END_DATE \n");
			sqlQuery.append("    ,STATUS \n");
			sqlQuery.append("    ,COURSE_STATUS \n");
			sqlQuery.append("    ,EXAM_MARK \n");
			sqlQuery.append("   ) \n");
			sqlQuery.append(" VALUES \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     ? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("   ) \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, learnDto.getLessonId());
			stmt.setDate(2, StringUtil.cnvToDBDate(learnDto.getStartDate()));
			stmt.setDate(3, StringUtil.cnvToDBDate(learnDto.getEndDate()));
			stmt.setBoolean(4, learnDto.getStatus());
			stmt.setString(5, learnDto.getCourseStatus());
			if (learnDto.getExamMark() == null) {
				stmt.setNull(6, java.sql.Types.INTEGER);
			} else {
				stmt.setLong(6, learnDto.getExamMark());
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
	 * Update Learn
	 *
	 * @param learnDto
	 * @param userId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean updateLearn(LearnDto learnDto, Long userId) {

		// if [learnDto = null], return false
		if (learnDto == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" UPDATE `T_LEARN_" + userId + "` \n");
			sqlQuery.append(" SET \n");
			sqlQuery.append("     `START_DATE` = ? \n");
			sqlQuery.append("    ,`END_DATE` = ? \n");
			sqlQuery.append("    ,`STATUS` = ? \n");
			sqlQuery.append("    ,`COURSE_STATUS` = ? \n");
			sqlQuery.append("    ,`EXAM_MARK` = ? \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `LESSON_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setDate(1, StringUtil.cnvToDBDate(learnDto.getStartDate()));
			stmt.setDate(2, StringUtil.cnvToDBDate(learnDto.getEndDate()));
			stmt.setBoolean(3, learnDto.getStatus());
			stmt.setString(4, learnDto.getCourseStatus());
			if (learnDto.getExamMark() == null) {
				stmt.setNull(5, java.sql.Types.INTEGER);
			} else {
				stmt.setLong(5, learnDto.getExamMark());
			}
			stmt.setLong(6, learnDto.getLessonId());

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
	 * Select Avg Mark
	 *
	 * @param userId
	 * @return Double
	 */
	public static Double selectAvgMark(Long userId) {

		// if [userId = null], return false
		if (userId == null) {
			return null;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   AVG(EXAM_MARK) \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LEARN_" + userId + " \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       1 = 1 \n");
			sqlQuery.append("   AND STATUS = ? \n");

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setBoolean(1, true);

			// Execute query
			rs = stmt.executeQuery();
			if (rs.next()) {
				return rs.getDouble(1);
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

		return null;
	}
}
