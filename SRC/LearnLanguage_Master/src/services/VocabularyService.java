package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.LessonCourseDto;
import dto.VocabularyDto;
import enums.CourseType;
import util.StringUtil;

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

	/**
	 * Register all Vocabulary
	 *
	 * @param vocabularyDtoList
	 * @param deletedList
	 * @param lessonId
	 * @return Boolean True:Success / False:Error
	 */
	public static Boolean registerAll(List<VocabularyDto> vocabularyDtoList,
									  List<Long> deletedList,
									  Long lessonId) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Insert LESSON_COURSE
			Long lessonCourseId = null;
			if (!vocabularyDtoList.isEmpty()) {
				if (LessonCourseService.countLessonCourse(lessonId, CourseType.VOCABULARY) == 0) {
					lessonCourseId =
						LessonCourseService.insertLessonCourse(
							lessonId, CourseType.VOCABULARY);
				} else {
					List<LessonCourseDto> lessonCourseDtoList =
						LessonCourseService.searchLesson(
							lessonId, CourseType.VOCABULARY);
					if (!lessonCourseDtoList.isEmpty()) {
						lessonCourseId = lessonCourseDtoList.get(0).getLessonCourseId();
					}
				}
			}

			// Register Data
			Integer orderIndex = 1;
			for (VocabularyDto vocabularyDto : vocabularyDtoList) {

				if (vocabularyDto.getVocabularyId() == null) {
					// Register new data
					vocabularyDto.setLessonCourseId(lessonCourseId);
					vocabularyDto.setOrderIndex(orderIndex);
					if (!VocabularyService.insertVocabulary(vocabularyDto)) {
						conn.rollback();
						return false;
					}

				} else if (!orderIndex.equals(vocabularyDto.getOrderIndex())
						|| vocabularyDto.getIsChange()) {
					// Update data
					vocabularyDto.setOrderIndex(orderIndex);
					if (!VocabularyService.updateVocabulary(vocabularyDto)) {
						conn.rollback();
						return false;
					}

				}

				orderIndex++;
			}

			// Delete data
			for (Long vocabularyId : deletedList) {
				// Delete data
				if (!VocabularyService.deleteVocabulary(vocabularyId)) {
					conn.rollback();
					return false;
				}
			}

			// Delete LESSON_COURSE
			if (vocabularyDtoList.isEmpty()) {
				LessonCourseService.deleteLessonCourse(lessonId, CourseType.VOCABULARY);
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
	 * Insert Vocabulary
	 *
	 * @param vocabularyDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean insertVocabulary(VocabularyDto vocabularyDto) {

		// if [vocabularyDto = null], return false
		if (vocabularyDto == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" INSERT INTO `T_VOCABULARY` \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     `LESSON_COURSE_ID` \n");
			sqlQuery.append("    ,`WORD` \n");
			sqlQuery.append("    ,`KANJI` \n");
			sqlQuery.append("    ,`MEANING` \n");
			sqlQuery.append("    ,`PRONUNCE_FILE` \n");
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
			sqlQuery.append("    ,? \n");
			sqlQuery.append("   ) \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, vocabularyDto.getLessonCourseId());
			stmt.setString(2, vocabularyDto.getWord());
			stmt.setString(3, vocabularyDto.getKanji());
			stmt.setString(4, vocabularyDto.getMeaning());
			stmt.setString(5, vocabularyDto.getPronunceFile());
			stmt.setString(6, vocabularyDto.getExplain());
			stmt.setInt(7, vocabularyDto.getOrderIndex());

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
	 * Update Vocabulary
	 *
	 * @param vocabularyDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean updateVocabulary(VocabularyDto vocabularyDto) {

		// if [vocabularyDto = null] or [vocabularyId = null], return false
		if (vocabularyDto == null || vocabularyDto.getVocabularyId() == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" UPDATE `T_VOCABULARY` \n");
			sqlQuery.append(" SET \n");
			sqlQuery.append("     `WORD` = ? \n");
			sqlQuery.append("    ,`KANJI` = ? \n");
			sqlQuery.append("    ,`MEANING` = ? \n");
			sqlQuery.append("    ,`PRONUNCE_FILE` = ? \n");
			sqlQuery.append("    ,`EXPLAIN` = ? \n");
			sqlQuery.append("    ,`ORDER_INDEX` = ? \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `VOCABULARY_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setString(1, vocabularyDto.getWord());
			stmt.setString(2, vocabularyDto.getKanji());
			stmt.setString(3, vocabularyDto.getMeaning());
			stmt.setString(4, vocabularyDto.getPronunceFile());
			stmt.setString(5, vocabularyDto.getExplain());
			stmt.setInt(6, vocabularyDto.getOrderIndex());
			stmt.setLong(7, vocabularyDto.getVocabularyId());

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
	 * Delete Vocabulary
	 *
	 * @param vocabularyId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean deleteVocabulary(Long vocabularyId) {

		// if [vocabularyId = null], return false
		if (vocabularyId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" DELETE FROM `T_VOCABULARY` \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       `VOCABULARY_ID` = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, vocabularyId);

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
