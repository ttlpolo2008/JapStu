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
	 * TITLE
	 */
	private String title;

	/**
	 * CONTENT
	 */
	private String content;

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
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

}
