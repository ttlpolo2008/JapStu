package dto;

import java.io.Serializable;

public class ListeningDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2761990023414004874L;

	/**
	 * LISTENING_ID
	 */
	private Long listeningId;

	/**
	 * LESSON_COURSE_ID
	 */
	private Long lessonCourseId;

	/**
	 * CONTENT_FILE
	 */
	private String contentFile;

	/**
	 * CONTENT_FILE_STREAM
	 */
	private byte[] contentFileStream;

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

	public ListeningDto() {
		this.isChange = false;
	}

	/**
	 * Create a copy of this data.
	 *
	 * @return VocabularyDto
	 */
	public ListeningDto copy() {
		ListeningDto vocabularyDto = new ListeningDto();
		vocabularyDto.setListeningId(this.listeningId);
		vocabularyDto.setLessonCourseId(this.lessonCourseId);
		vocabularyDto.setContentFile(this.contentFile);
		vocabularyDto.setContentTranslate(this.contentTranslate);
		vocabularyDto.setOrderIndex(this.orderIndex);
		vocabularyDto.setIsChange(this.isChange);

		return vocabularyDto;
	}

	/**
	 * @return the listeningId
	 */
	public Long getListeningId() {
		return listeningId;
	}

	/**
	 * @param listeningId the listeningId to set
	 */
	public void setListeningId(Long listeningId) {
		this.listeningId = listeningId;
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
	 * @return the contentFile
	 */
	public String getContentFile() {
		return contentFile;
	}

	/**
	 * @param contentFile the contentFile to set
	 */
	public void setContentFile(String contentFile) {
		this.contentFile = contentFile;
	}

	/**
	 * @return the contentFileStream
	 */
	public byte[] getContentFileStream() {
		return contentFileStream;
	}

	/**
	 * @param contentFileStream the contentFileStream to set
	 */
	public void setContentFileStream(byte[] contentFileStream) {
		this.contentFileStream = contentFileStream;
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
