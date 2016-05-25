package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
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
			sqlQuery.append("  ,T3.TITLE \n");
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
				readingDto.setTitle(rs.getString("TITLE"));
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
}
