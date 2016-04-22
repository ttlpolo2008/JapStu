package dto;

import java.io.Serializable;

public class LessonDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4100989899811205586L;

	/**
	 * LESSON_ID
	 */
	private Long lessonId;

	/**
	 * LESSON_NO
	 */
	private Integer lessonNo;

	/**
	 * LESSON_NAME
	 */
	private String lessonName;

	/**
	 * PASS_SCORE
	 */
	private Integer passScore;

	/**
	 * Change's flag
	 */
	private Boolean isChange;

	public LessonDto() {
		this.isChange = false;
	}

	/**
	 * Create a copy of this data.
	 *
	 * @return LessonDto
	 */
	public LessonDto copy() {
		LessonDto lessonDto = new LessonDto();
		lessonDto.setLessonId(this.lessonId);
		lessonDto.setLessonNo(this.lessonNo);
		lessonDto.setLessonName(this.lessonName);
		lessonDto.setPassScore(this.passScore);
		lessonDto.setIsChange(this.isChange);

		return lessonDto;
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
	 * @return the lessonNo
	 */
	public Integer getLessonNo() {
		return lessonNo;
	}

	/**
	 * @param lessonNo the lessonNo to set
	 */
	public void setLessonNo(Integer lessonNo) {
		this.lessonNo = lessonNo;
	}

	/**
	 * @return the lessonName
	 */
	public String getLessonName() {
		return lessonName;
	}

	/**
	 * @param lessonName the lessonName to set
	 */
	public void setLessonName(String lessonName) {
		this.lessonName = lessonName;
	}

	/**
	 * @return the passScore
	 */
	public Integer getPassScore() {
		return passScore;
	}

	/**
	 * @param passScore the passScore to set
	 */
	public void setPassScore(Integer passScore) {
		this.passScore = passScore;
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
