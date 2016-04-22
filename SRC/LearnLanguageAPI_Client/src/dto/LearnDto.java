package dto;

import java.io.Serializable;
import java.util.Date;

public class LearnDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2761990023414004874L;

	/**
	 * LEARN_ID
	 */
	private Long learnId;

	/**
	 * LESSON_ID
	 */
	private Long lessonId;

	/**
	 * START_DATE
	 */
	private Date startDate;

	/**
	 * END_DATE
	 */
	private Date endDate;

	/**
	 * STATUS
	 */
	private Boolean status;

	/**
	 * COURSE_STATUS
	 */
	private String courseStatus;

	/**
	 * EXAM_MARK
	 */
	private Long examMark;

	/**
	 * @return the learnId
	 */
	public Long getLearnId() {
		return learnId;
	}

	/**
	 * @param learnId the learnId to set
	 */
	public void setLearnId(Long learnId) {
		this.learnId = learnId;
	}

	/**
	 * @return the lessonId
	 */
	public Long getLessonId() {
		return lessonId;
	}

	/**
	 * @param lessonId the lessonId to set
	 */
	public void setLessonId(Long lessonId) {
		this.lessonId = lessonId;
	}

	/**
	 * @return the startDate
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * @param startDate the startDate to set
	 */
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	/**
	 * @return the endDate
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate the endDate to set
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return the status
	 */
	public Boolean getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Boolean status) {
		this.status = status;
	}

	/**
	 * @return the courseStatus
	 */
	public String getCourseStatus() {
		return courseStatus;
	}

	/**
	 * @param courseStatus the courseStatus to set
	 */
	public void setCourseStatus(String courseStatus) {
		this.courseStatus = courseStatus;
	}

	/**
	 * @return the examMark
	 */
	public Long getExamMark() {
		return examMark;
	}

	/**
	 * @param examMark the examMark to set
	 */
	public void setExamMark(Long examMark) {
		this.examMark = examMark;
	}

}
