package dto;

import java.io.Serializable;

public class VocabularyDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2761990023414004874L;

	/**
	 * VOCABULARY_ID
	 */
	private Long vocabularyId;

	/**
	 * LESSON_COURSE_ID
	 */
	private Long lessonCourseId;

	/**
	 * WORD
	 */
	private String word;

	/**
	 * KANJI
	 */
	private String kanji;

	/**
	 * MEANING
	 */
	private String meaning;

	/**
	 * PRONUNCE_FILE
	 */
	private String pronunceFile;

	/**
	 * PRONUNCE_FILE_STREAM
	 */
	private byte[] pronunceFileStream;

	/**
	 * EXPLAIN
	 */
	private String explain;

	/**
	 * ORDER_INDEX
	 */
	private Integer orderIndex;

	/**
	 * Change's flag
	 */
	private Boolean isChange;

	public VocabularyDto() {
		this.isChange = false;
	}

	/**
	 * Create a copy of this data.
	 *
	 * @return VocabularyDto
	 */
	public VocabularyDto copy() {
		VocabularyDto vocabularyDto = new VocabularyDto();
		vocabularyDto.setVocabularyId(this.vocabularyId);
		vocabularyDto.setLessonCourseId(this.lessonCourseId);
		vocabularyDto.setWord(this.word);
		vocabularyDto.setKanji(this.kanji);
		vocabularyDto.setMeaning(this.meaning);
		vocabularyDto.setPronunceFile(this.pronunceFile);
		vocabularyDto.setPronunceFileStream(this.pronunceFileStream);
		vocabularyDto.setExplain(this.explain);
		vocabularyDto.setOrderIndex(this.orderIndex);
		vocabularyDto.setIsChange(this.isChange);

		return vocabularyDto;
	}

	/**
	 * @return the vocabularyId
	 */
	public Long getVocabularyId() {
		return vocabularyId;
	}

	/**
	 * @param vocabularyId the vocabularyId to set
	 */
	public void setVocabularyId(Long vocabularyId) {
		this.vocabularyId = vocabularyId;
	}

	/**
	 * @return the lessonCourseId
	 */
	public Long getLessonCourseId() {
		return lessonCourseId;
	}

	/**
	 * @param lessonCourseId the lessonCourseId to set
	 */
	public void setLessonCourseId(Long lessonCourseId) {
		this.lessonCourseId = lessonCourseId;
	}

	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}

	/**
	 * @param word the word to set
	 */
	public void setWord(String word) {
		this.word = word;
	}

	/**
	 * @return the meaning
	 */
	public String getMeaning() {
		return meaning;
	}

	/**
	 * @param meaning the meaning to set
	 */
	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	/**
	 * @return the pronunceFile
	 */
	public String getPronunceFile() {
		return pronunceFile;
	}

	/**
	 * @param pronunceFile the pronunceFile to set
	 */
	public void setPronunceFile(String pronunceFile) {
		this.pronunceFile = pronunceFile;
	}

	/**
	 * @return the explain
	 */
	public String getExplain() {
		return explain;
	}

	/**
	 * @param explain the explain to set
	 */
	public void setExplain(String explain) {
		this.explain = explain;
	}

	/**
	 * @return the orderIndex
	 */
	public Integer getOrderIndex() {
		return orderIndex;
	}

	/**
	 * @param orderIndex the orderIndex to set
	 */
	public void setOrderIndex(Integer orderIndex) {
		this.orderIndex = orderIndex;
	}

	/**
	 * @return the isChange
	 */
	public Boolean getIsChange() {
		return isChange;
	}

	/**
	 * @param isChange the isChange to set
	 */
	public void setIsChange(Boolean isChange) {
		this.isChange = isChange;
	}

	/**
	 * @return the pronunceFileStream
	 */
	public byte[] getPronunceFileStream() {
		return pronunceFileStream;
	}

	/**
	 * @param pronunceFileStream the pronunceFileStream to set
	 */
	public void setPronunceFileStream(byte[] pronunceFileStream) {
		this.pronunceFileStream = pronunceFileStream;
	}

	/**
	 * @return the kanji
	 */
	public String getKanji() {
		return kanji;
	}

	/**
	 * @param kanji the kanji to set
	 */
	public void setKanji(String kanji) {
		this.kanji = kanji;
	}

}
