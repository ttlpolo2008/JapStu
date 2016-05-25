package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.StringUtil;
import dao.CommonDAO;
import dto.ListeningDto;
import enums.CourseType;

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
			sqlQuery.append("  ,T3.LISTENING_TYPE \n");
			sqlQuery.append("  ,T3.CONTENT_FILE \n");
			sqlQuery.append("  ,T3.CONTENT \n");
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
				listeningDto.setListeningType(rs.getString("LISTENING_TYPE"));
				listeningDto.setContentFile(rs.getString("CONTENT_FILE"));
				listeningDto.setContent(rs.getString("CONTENT"));
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
}
