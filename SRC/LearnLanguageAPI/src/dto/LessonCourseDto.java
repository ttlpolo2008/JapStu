package dto;

import java.io.Serializable;

public class LessonCourseDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4100989899811205586L;

	/**
	 * LESSON_COURSE_ID
	 */
	private Long lessonCourseId;

	/**
	 * LESSON_ID
	 */
	private Long lessonId;

	/**
	 * COURSE_TYPE
	 */
	private String courseType;

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
	 * @return the courseType
	 */
	public String getCourseType() {
		return courseType;
	}

	/**
	 * @param courseType the courseType to set
	 */
	public void setCourseType(String courseType) {
		this.courseType = courseType;
	}

}
