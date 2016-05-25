package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import util.StringUtil;
import dao.CommonDAO;
import dto.KanjiDto;
import enums.CourseType;

public class KanjiService {

	/**
	 * Search Kanji
	 *
	 * @param lessonId
	 * @return List<KanjiDto>
	 */
	public static List<KanjiDto> searchKanji(Long lessonId) {

		// Result list
		List<KanjiDto> kanjiDtoList = new ArrayList<KanjiDto>();

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   T3.KANJI_ID \n");
			sqlQuery.append("  ,T3.LESSON_COURSE_ID \n");
			sqlQuery.append("  ,T3.WORD \n");
			sqlQuery.append("  ,T3.ON_YOMI \n");
			sqlQuery.append("  ,T3.KUN_YOMI \n");
			sqlQuery.append("  ,T3.MEANING \n");
			sqlQuery.append("  ,T3.WRITE_FILE \n");
			sqlQuery.append("  ,T3.EXPLAIN \n");
			sqlQuery.append("  ,T3.ORDER_INDEX \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_LESSON T1 \n");
			sqlQuery.append("  ,T_LESSON_COURSE T2 \n");
			sqlQuery.append("  ,T_KANJI T3 \n");
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
			stmt.setString(2, CourseType.KANJI.getCode());

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				KanjiDto kanjiDto = new KanjiDto();
				kanjiDto.setKanjiId(rs.getLong("KANJI_ID"));
				kanjiDto.setLessonCourseId(rs.getLong("LESSON_COURSE_ID"));
				kanjiDto.setWord(rs.getString("WORD"));
				kanjiDto.setOnYomi(rs.getString("ON_YOMI"));
				kanjiDto.setKunYomi(rs.getString("KUN_YOMI"));
				kanjiDto.setMeaning(rs.getString("MEANING"));
				kanjiDto.setWriteFile(rs.getString("WRITE_FILE"));
				kanjiDto.setExplain(rs.getString("EXPLAIN"));
				kanjiDto.setOrderIndex(rs.getInt("ORDER_INDEX"));

				// Get File
				if (!StringUtil.isNullOrEmpty(kanjiDto.getWriteFile())) {
					kanjiDto.setWriteFileStream(
						StringUtil.readUploadFile(kanjiDto.getWriteFile()));
				}

				// Add Dto to List
				kanjiDtoList.add(kanjiDto);
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

		return kanjiDtoList;
	}
}
