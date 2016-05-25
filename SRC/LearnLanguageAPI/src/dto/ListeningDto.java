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
	 * LISTENING_TYPE
	 */
	private String listeningType;

	/**
	 * CONTENT_FILE
	 */
	private String contentFile;

	/**
	 * CONTENT_FILE_STREAM
	 */
	private byte[] contentFileStream;

	/**
	 * CONTENT
	 */
	private String content;

	/**
	 * ORDER_INDEX
	 */
	private Integer orderIndex;

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
	 * @return the listeningType
	 */
	public String getListeningType() {
		return listeningType;
	}

	/**
	 * @param listeningType the listeningType to set
	 */
	public void setListeningType(String listeningType) {
		this.listeningType = listeningType;
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

}
