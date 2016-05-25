package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.StringUtil;
import dao.CommonDAO;
import dto.VocabularyDto;
import enums.CourseType;

public class VocabularyService {

	/**
	 * Search Vocabulary
	 *
	 * @param lessonId
	 * @return List<VocabularyDto>
	 */
	public static List<VocabularyDto> searchVocabulary(Long lessonId) {

		// Result list
		List<VocabularyDto> vocabularyDtoList = new ArrayList<VocabularyDto>();

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   T3.VOCABULARY_ID \n");
			sqlQuery.append("  ,T3.LESSON_COURSE_ID \n");
			sqlQuery.append("  ,T3.WORD \n");
			sqlQuery.append("  ,T3.KANJI \n");
			sqlQuery.append("  ,T3.MEANING \n");
			sqlQuery.append("  ,T3.PRONUNCE_FILE \n");
			sqlQuery.append("  ,T3.EXPLAIN \n");
			sqlQuery.append("  ,T3.ORDER_INDEX \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LESSON T1 \n");
			sqlQuery.append("  ,T_LESSON_COURSE T2 \n");
			sqlQuery.append("  ,T_VOCABULARY T3 \n");
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
			stmt.setString(2, CourseType.VOCABULARY.getCode());

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				VocabularyDto vocabularyDto = new VocabularyDto();
				vocabularyDto.setVocabularyId(rs.getLong("VOCABULARY_ID"));
				vocabularyDto.setLessonCourseId(rs.getLong("LESSON_COURSE_ID"));
				vocabularyDto.setWord(rs.getString("WORD"));
				vocabularyDto.setKanji(rs.getString("KANJI"));
				vocabularyDto.setMeaning(rs.getString("MEANING"));
				vocabularyDto.setPronunceFile(rs.getString("PRONUNCE_FILE"));
				vocabularyDto.setExplain(rs.getString("EXPLAIN"));
				vocabularyDto.setOrderIndex(rs.getInt("ORDER_INDEX"));

				// Get File
				if (!StringUtil.isNullOrEmpty(vocabularyDto.getPronunceFile())) {
					vocabularyDto.setPronunceFileStream(
						StringUtil.readUploadFile(vocabularyDto.getPronunceFile()));
				}

				// Add Dto to List
				vocabularyDtoList.add(vocabularyDto);
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

		return vocabularyDtoList;
	}
}
