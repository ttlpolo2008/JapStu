package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.KanjiDto;
import dto.LessonCourseDto;
import enums.CourseType;
import util.StringUtil;

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

	/**
	 * Register all Kanji
	 *
	 * @param kanjiDtoList
	 * @param deletedList
	 * @param lessonId
	 * @return Boolean True:Success / False:Error
	 */
	public static Boolean registerAll(List<KanjiDto> kanjiDtoList,
									  List<Long> deletedList,
									  Long lessonId) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Insert LESSON_COURSE
			Long lessonCourseId = null;
			if (!kanjiDtoList.isEmpty()) {
				if (LessonCourseService.countLessonCourse(lessonId, CourseType.KANJI) == 0) {
					lessonCourseId =
						LessonCourseService.insertLessonCourse(
							lessonId, CourseType.KANJI);
				} else {
					List<LessonCourseDto> lessonCourseDtoList =
						LessonCourseService.searchLesson(
							lessonId, CourseType.KANJI);
					if (!lessonCourseDtoList.isEmpty()) {
						lessonCourseId = lessonCourseDtoList.get(0).getLessonCourseId();
					}
				}
			}

			// Register Data
			Integer orderIndex = 1;
			for (KanjiDto kanjiDto : kanjiDtoList) {

				if (kanjiDto.getKanjiId() == null) {
					// Register new data
					kanjiDto.setLessonCourseId(lessonCourseId);
					kanjiDto.setOrderIndex(orderIndex);
					if (!KanjiService.insertKanji(kanjiDto)) {
						conn.rollback();
						return false;
					}

				} else if (!orderIndex.equals(kanjiDto.getOrderIndex())
						|| kanjiDto.getIsChange()) {
					// Update data
					kanjiDto.setOrderIndex(orderIndex);
					if (!KanjiService.updateKanji(kanjiDto)) {
						conn.rollback();
						return false;
					}

				}

				orderIndex++;
			}

			// Delete data
			for (Long kanjiId : deletedList) {
				// Delete data
				if (!KanjiService.deleteKanji(kanjiId)) {
					conn.rollback();
					return false;
				}
			}

			// Delete LESSON_COURSE
			if (kanjiDtoList.isEmpty()) {
				LessonCourseService.deleteLessonCourse(lessonId, CourseType.KANJI);
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
	 * Insert Kanji
	 *
	 * @param kanjiDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean insertKanji(KanjiDto kanjiDto) {

		// if [kanjiDto = null], return false
		if (kanjiDto == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" INSERT INTO `T_KANJI` \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     `LESSON_COURSE_ID` \n");
			sqlQuery.append("    ,`WORD` \n");
			sqlQuery.append("    ,`MEANING` \n");
			sqlQuery.append("    ,`WRITE_FILE` \n");
			sqlQuery.append("    ,`EXPLAIN` \n");
			sqlQuery.append("    ,`ORDER_INDEX` \n");
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
			stmt.setLong(1, kanjiDto.getLessonCourseId());
			stmt.setString(2, kanjiDto.getWord());
			stmt.setString(3, kanjiDto.getMeaning());
			stmt.setString(4, kanjiDto.getWriteFile());
			stmt.setString(5, kanjiDto.getExplain());
			stmt.setInt(6, kanjiDto.getOrderIndex());

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
	 * Update Kanji
	 *
	 * @param kanjiDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean updateKanji(KanjiDto kanjiDto) {

		// if [kanjiDto = null] or [kanjiId = null], return false
		if (kanjiDto == null || kanjiDto.getKanjiId() == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" UPDATE `T_KANJI` \n");
			sqlQuery.append(" SET \n");
			sqlQuery.append("     `WORD` = ? \n");
			sqlQuery.append("    ,`MEANING` = ? \n");
			sqlQuery.append("    ,`WRITE_FILE` = ? \n");
			sqlQuery.append("    ,`EXPLAIN` = ? \n");
			sqlQuery.append("    ,`ORDER_INDEX` = ? \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `KANJI_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setString(1, kanjiDto.getWord());
			stmt.setString(2, kanjiDto.getMeaning());
			stmt.setString(3, kanjiDto.getWriteFile());
			stmt.setString(4, kanjiDto.getExplain());
			stmt.setInt(5, kanjiDto.getOrderIndex());
			stmt.setLong(6, kanjiDto.getKanjiId());

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
	 * Delete Kanji
	 *
	 * @param kanjiId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean deleteKanji(Long kanjiId) {

		// if [kanjiId = null], return false
		if (kanjiId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" DELETE FROM `T_KANJI` \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `KANJI_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, kanjiId);

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
