package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import dao.CommonDAO;
import dto.LearnDto;

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
	 * Drop table T_LEARN
	 *
	 * @param userId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean dropLearn(Long userId) {

		// if [userId = null], return false
		if (userId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" DROP TABLE `T_LEARN_" + userId + "` \n");

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
	public static Map<Long, LearnDto> searchStudyHistoryAll(Long userId) {

		// Result Map
		Map<Long, LearnDto> studyMap = new HashMap<Long, LearnDto>();

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
			sqlQuery.append(" ORDER BY \n");
			sqlQuery.append("   LEARN_ID \n");

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				LearnDto learnDto = new LearnDto();
				learnDto.setLearnId(rs.getLong("LEARN_ID"));
				learnDto.setLessonId(rs.getLong("LESSON_ID"));

//				Date startDate = rs.getDate("START_DATE");
//				if (startDate != null) {
//					learnDto.setStartDate(
//							StringUtil.cnvToDate(startDate.toString()));
//				}
				learnDto.setStartDate(rs.getDate("START_DATE"));
				learnDto.setEndDate(rs.getDate("END_DATE"));
				learnDto.setStatus(rs.getBoolean("STATUS"));
				learnDto.setCourseStatus(rs.getString("COURSE_STATUS"));
				learnDto.setExamMark(rs.getLong("EXAM_MARK"));

				// Add Dto to List
				studyMap.put(learnDto.getLessonId(), learnDto);
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

		return studyMap;
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
			sqlQuery.append(" ORDER BY \n");
			sqlQuery.append("   LEARN_ID DESC \n");
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
}
