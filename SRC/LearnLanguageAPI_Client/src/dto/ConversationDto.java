package dto;

import java.io.Serializable;

public class ConversationDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2761990023414004874L;

	/**
	 * CONVERSATION_ID
	 */
	private Long conversationId;

	/**
	 * LESSON_COURSE_ID
	 */
	private Long lessonCourseId;

	/**
	 * CONTENT
	 */
	private String content;

	/**
	 * CONTENT_TRANSLATE
	 */
	private String contentTranslate;

	/**
	 * CONTENT_FILE
	 */
	private String contentFile;

	/**
	 * CONTENT_FILE_STREAM
	 */
	private byte[] contentFileStream;

	/**
	 * ORDER_INDEX
	 */
	private Integer orderIndex;

	/**
	 * Change's flag
	 */
	private Boolean isChange;

	public ConversationDto() {
		this.isChange = false;
	}

	/**
	 * Create a copy of this data.
	 *
	 * @return VocabularyDto
	 */
	public ConversationDto copy() {
		ConversationDto vocabularyDto = new ConversationDto();
		vocabularyDto.setConversationId(this.conversationId);
		vocabularyDto.setLessonCourseId(this.lessonCourseId);
		vocabularyDto.setContent(this.content);
		vocabularyDto.setContentTranslate(this.contentTranslate);
		vocabularyDto.setContentFile(this.contentFile);
		vocabularyDto.setOrderIndex(this.orderIndex);
		vocabularyDto.setIsChange(this.isChange);

		return vocabularyDto;
	}

	/**
	 * @return the conversationId
	 */
	public Long getConversationId() {
		return conversationId;
	}

	/**
	 * @param conversationId the conversationId to set
	 */
	public void setConversationId(Long conversationId) {
		this.conversationId = conversationId;
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
