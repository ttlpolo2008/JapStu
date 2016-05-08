package dto;

import java.io.Serializable;

public class KanjiDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2761990023414004874L;

	/**
	 * KANJI_ID
	 */
	private Long kanjiId;

	/**
	 * LESSON_COURSE_ID
	 */
	private Long lessonCourseId;

	/**
	 * WORD
	 */
	private String word;

	/**
	 * ON_YOMI
	 */
	private String onYomi;

	/**
	 * KUN_YOMI
	 */
	private String kunYomi;

	/**
	 * MEANING
	 */
	private String meaning;

	/**
	 * WRITE_FILE
	 */
	private String writeFile;

	/**
	 * WRITE_FILE_STREAM
	 */
	private byte[] writeFileStream;

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

	public KanjiDto() {
		this.isChange = false;
	}

	/**
	 * Create a copy of this data.
	 *
	 * @return VocabularyDto
	 */
	public KanjiDto copy() {
		KanjiDto vocabularyDto = new KanjiDto();
		vocabularyDto.setKanjiId(this.kanjiId);
		vocabularyDto.setLessonCourseId(this.lessonCourseId);
		vocabularyDto.setWord(this.word);
		vocabularyDto.setOnYomi(this.onYomi);
		vocabularyDto.setKunYomi(this.kunYomi);
		vocabularyDto.setMeaning(this.meaning);
		vocabularyDto.setWriteFile(this.writeFile);
		vocabularyDto.setWriteFileStream(this.writeFileStream);
		vocabularyDto.setExplain(this.explain);
		vocabularyDto.setOrderIndex(this.orderIndex);
		vocabularyDto.setIsChange(this.isChange);

		return vocabularyDto;
	}

	/**
	 * @return the kanjiId
	 */
	public Long getKanjiId() {
		return kanjiId;
	}

	/**
	 * @param kanjiId the kanjiId to set
	 */
	public void setKanjiId(Long kanjiId) {
		this.kanjiId = kanjiId;
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
	 * @return the writeFile
	 */
	public String getWriteFile() {
		return writeFile;
	}

	/**
	 * @param writeFile the writeFile to set
	 */
	public void setWriteFile(String writeFile) {
		this.writeFile = writeFile;
	}

	/**
	 * @return the writeFileStream
	 */
	public byte[] getWriteFileStream() {
		return writeFileStream;
	}

	/**
	 * @param writeFileStream the writeFileStream to set
	 */
	public void setWriteFileStream(byte[] writeFileStream) {
		this.writeFileStream = writeFileStream;
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
	 * @return the onYomi
	 */
	public String getOnYomi() {
		return onYomi;
	}

	/**
	 * @param onYomi the onYomi to set
	 */
	public void setOnYomi(String onYomi) {
		this.onYomi = onYomi;
	}

	/**
	 * @return the kunYomi
	 */
	public String getKunYomi() {
		return kunYomi;
	}

	/**
	 * @param kunYomi the kunYomi to set
	 */
	public void setKunYomi(String kunYomi) {
		this.kunYomi = kunYomi;
	}

}
