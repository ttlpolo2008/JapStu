package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.StringUtil;
import dao.CommonDAO;
import dto.ConversationDto;
import enums.CourseType;

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
//readUploadFile doc file roi tra ve dang byte, o client se dua vao byte nay se phat am thanh.
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
}
