package dto;

import java.io.Serializable;

public class ReadingDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2761990023414004874L;

	/**
	 * READING_ID
	 */
	private Long readingId;

	/**
	 * LESSON_COURSE_ID
	 */
	private Long lessonCourseId;

	/**
	 * CONTENT
	 */
	private String content;

	/**
	 * CONTENT_PRON
	 */
	private String contentPron;

	/**
	 * CONTENT_TRANSLATE
	 */
	private String contentTranslate;

	/**
	 * ORDER_INDEX
	 */
	private Integer orderIndex;

	/**
	 * Change's flag
	 */
	private Boolean isChange;

	public ReadingDto() {
		this.isChange = false;
	}

	/**
	 * Create a copy of this data.
	 *
	 * @return VocabularyDto
	 */
	public ReadingDto copy() {
		ReadingDto vocabularyDto = new ReadingDto();
		vocabularyDto.setReadingId(this.readingId);
		vocabularyDto.setLessonCourseId(this.lessonCourseId);
		vocabularyDto.setContent(this.content);
		vocabularyDto.setContentPron(this.contentPron);
		vocabularyDto.setContentTranslate(this.contentTranslate);
		vocabularyDto.setOrderIndex(this.orderIndex);
		vocabularyDto.setIsChange(this.isChange);

		return vocabularyDto;
	}

	/**
	 * @return the readingId
	 */
	public Long getReadingId() {
		return readingId;
	}

	/**
	 * @param readingId the readingId to set
	 */
	public void setReadingId(Long readingId) {
		this.readingId = readingId;
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
	 * @return the content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * @return the contentPron
	 */
	public String getContentPron() {
		return contentPron;
	}

	/**
	 * @param contentPron the contentPron to set
	 */
	public void setContentPron(String contentPron) {
		this.contentPron = contentPron;
	}

	/**
	 * @return the contentTranslate
	 */
	public String getContentTranslate() {
		return contentTranslate;
	}

	/**
	 * @param contentTranslate the contentTranslate to set
	 */
	public void setContentTranslate(String contentTranslate) {
		this.contentTranslate = contentTranslate;
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

}
