package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.ConversationDto;
import dto.LessonCourseDto;
import enums.CourseType;
import util.StringUtil;

public class ConversationService {

	/**
	 * Search Conversation
	 *
	 * @param lessonId
	 * @return List<ConversationDto>
	 */
	public static List<ConversationDto> searchConversation(Long lessonId) {

		// Result list
		List<ConversationDto> conversationDtoList = new ArrayList<ConversationDto>();

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   T3.CONVERSATION_ID \n");
			sqlQuery.append("  ,T3.LESSON_COURSE_ID \n");
			sqlQuery.append("  ,T3.TITLE \n");
			sqlQuery.append("  ,T3.CONTENT \n");
			sqlQuery.append("  ,T3.CONTENT_FILE \n");
			sqlQuery.append("  ,T3.ORDER_INDEX \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LESSON T1 \n");
			sqlQuery.append("  ,T_LESSON_COURSE T2 \n");
			sqlQuery.append("  ,T_CONVERSATION T3 \n");
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
			stmt.setString(2, CourseType.CONVERSATION.getCode());

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				ConversationDto conversationDto = new ConversationDto();
				conversationDto.setConversationId(rs.getLong("CONVERSATION_ID"));
				conversationDto.setLessonCourseId(rs.getLong("LESSON_COURSE_ID"));
				conversationDto.setTitle(rs.getString("TITLE"));
				conversationDto.setContent(rs.getString("CONTENT"));
				conversationDto.setContentFile(rs.getString("CONTENT_FILE"));
				conversationDto.setOrderIndex(rs.getInt("ORDER_INDEX"));

				// Get File
				if (!StringUtil.isNullOrEmpty(conversationDto.getContentFile())) {
					conversationDto.setContentFileStream(
						StringUtil.readUploadFile(conversationDto.getContentFile()));
				}

				// Add Dto to List
				conversationDtoList.add(conversationDto);
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

		return conversationDtoList;
	}

	/**
	 * Register all Conversation
	 *
	 * @param conversationDtoList
	 * @param deletedList
	 * @param lessonId
	 * @return Boolean True:Success / False:Error
	 */
	public static Boolean registerAll(List<ConversationDto> conversationDtoList,
									  List<Long> deletedList,
									  Long lessonId) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Insert LESSON_COURSE
			Long lessonCourseId = null;
			if (!conversationDtoList.isEmpty()) {
				if (LessonCourseService.countLessonCourse(lessonId, CourseType.CONVERSATION) == 0) {
					lessonCourseId =
						LessonCourseService.insertLessonCourse(
							lessonId, CourseType.CONVERSATION);
				} else {
					List<LessonCourseDto> lessonCourseDtoList =
						LessonCourseService.searchLesson(
							lessonId, CourseType.CONVERSATION);
					if (!lessonCourseDtoList.isEmpty()) {
						lessonCourseId = lessonCourseDtoList.get(0).getLessonCourseId();
					}
				}
			}

			// Register Data
			Integer orderIndex = 1;
			for (ConversationDto conversationDto : conversationDtoList) {

				if (conversationDto.getConversationId() == null) {
					// Register new data
					conversationDto.setLessonCourseId(lessonCourseId);
					conversationDto.setOrderIndex(orderIndex);
					if (!ConversationService.insertConversation(conversationDto)) {
						conn.rollback();
						return false;
					}

				} else if (!orderIndex.equals(conversationDto.getOrderIndex())
						|| conversationDto.getIsChange()) {
					// Update data
					conversationDto.setOrderIndex(orderIndex);
					if (!ConversationService.updateConversation(conversationDto)) {
						conn.rollback();
						return false;
					}

				}

				orderIndex++;
			}

			// Delete data
			for (Long conversationId : deletedList) {
				// Delete data
				if (!ConversationService.deleteConversation(conversationId)) {
					conn.rollback();
					return false;
				}
			}

			// Delete LESSON_COURSE
			if (conversationDtoList.isEmpty()) {
				LessonCourseService.deleteLessonCourse(lessonId, CourseType.CONVERSATION);
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
	 * Insert Conversation
	 *
	 * @param conversationDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean insertConversation(ConversationDto conversationDto) {

		// if [conversationDto = null], return false
		if (conversationDto == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" INSERT INTO `T_CONVERSATION` \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     `LESSON_COURSE_ID` \n");
			sqlQuery.append("    ,`TITLE` \n");
			sqlQuery.append("    ,`CONTENT` \n");
			sqlQuery.append("    ,`CONTENT_FILE` \n");
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
			stmt.setLong(1, conversationDto.getLessonCourseId());
			stmt.setString(2, conversationDto.getTitle());
			stmt.setString(3, conversationDto.getContent());
			stmt.setString(4, conversationDto.getContentFile());
			stmt.setInt(5, conversationDto.getOrderIndex());

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
	 * Update Conversation
	 *
	 * @param conversationDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean updateConversation(ConversationDto conversationDto) {

		// if [conversationDto = null] or [conversationId = null], return false
		if (conversationDto == null || conversationDto.getConversationId() == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" UPDATE `T_CONVERSATION` \n");
			sqlQuery.append(" SET \n");
			sqlQuery.append("     `TITLE` = ? \n");
			sqlQuery.append("    ,`CONTENT` = ? \n");
			sqlQuery.append("    ,`CONTENT_FILE` = ? \n");
			sqlQuery.append("    ,`ORDER_INDEX` = ? \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `CONVERSATION_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setString(1, conversationDto.getTitle());
			stmt.setString(2, conversationDto.getContent());
			stmt.setString(3, conversationDto.getContentFile());
			stmt.setInt(4, conversationDto.getOrderIndex());
			stmt.setLong(5, conversationDto.getConversationId());

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
	 * Delete Conversation
	 *
	 * @param conversationId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean deleteConversation(Long conversationId) {

		// if [conversationId = null], return false
		if (conversationId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" DELETE FROM `T_CONVERSATION` \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `CONVERSATION_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, conversationId);

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
