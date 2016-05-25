package dto;

import java.io.Serializable;

public class ExerciseDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4100989899811205586L;

	/**
	 * EXERCISE_ID
	 */
	private Long exerciseId;

	/**
	 * LESSON_ID
	 */
	private Long lessonId;

	/**
	 * QUESTION_TYPE
	 */
	private String questionType;

	/**
	 * ANSWER_TYPE
	 */
	private String answerType;

	/**
	 * QUESTION_CONTENT
	 */
	private String questionContent;

	/**
	 * QUESTION_CONTENT_FILE
	 */
	private String questionContentFile;

	/**
	 * QUESTION_CONTENT_FILE_STREAM
	 */
	private byte[] questionContentFileStream;

	/**
	 * MARK
	 */
	private Integer mark;

	/**
	 * TIME
	 */
	private Integer time;

	/**
	 * ANSWER_CHOOSE
	 */
	private String answerChoose;

	/**
	 * ANSWER_1
	 */
	private String answer1;

	/**
	 * ANSWER_2
	 */
	private String answer2;

	/**
	 * ANSWER_3
	 */
	private String answer3;

	/**
	 * ANSWER_4
	 */
	private String answer4;

	/**
	 * ANSWER_5
	 */
	private String answer5;

	/**
	 * @return the exerciseId
	 */
	public Long getExerciseId() {
		return exerciseId;
	}

	/**
	 * @param exerciseId the exerciseId to set
	 */
	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
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
	 * @return the questionType
	 */
	public String getQuestionType() {
		return questionType;
	}

	/**
	 * @param questionType the questionType to set
	 */
	public void setQuestionType(String questionType) {
		this.questionType = questionType;
	}

	/**
	 * @return the questionContent
	 */
	public String getQuestionContent() {
		return questionContent;
	}

	/**
	 * @param questionContent the questionContent to set
	 */
	public void setQuestionContent(String questionContent) {
		this.questionContent = questionContent;
	}

	/**
	 * @return the questionContentFile
	 */
	public String getQuestionContentFile() {
		return questionContentFile;
	}

	/**
	 * @param questionContentFile the questionContentFile to set
	 */
	public void setQuestionContentFile(String questionContentFile) {
		this.questionContentFile = questionContentFile;
	}

	/**
	 * @return the questionContentFileStream
	 */
	public byte[] getQuestionContentFileStream() {
		return questionContentFileStream;
	}

	/**
	 * @param questionContentFileStream the questionContentFileStream to set
	 */
	public void setQuestionContentFileStream(byte[] questionContentFileStream) {
		this.questionContentFileStream = questionContentFileStream;
	}

	/**
	 * @return the mark
	 */
	public Integer getMark() {
		return mark;
	}

	/**
	 * @param mark the mark to set
	 */
	public void setMark(Integer mark) {
		this.mark = mark;
	}

	/**
	 * @return the time
	 */
	public Integer getTime() {
		return time;
	}

	/**
	 * @param time the time to set
	 */
	public void setTime(Integer time) {
		this.time = time;
	}

	/**
	 * @return the answerChoose
	 */
	public String getAnswerChoose() {
		return answerChoose;
	}

	/**
	 * @param answerChoose the answerChoose to set
	 */
	public void setAnswerChoose(String answerChoose) {
		this.answerChoose = answerChoose;
	}

	/**
	 * @return the answer1
	 */
	public String getAnswer1() {
		return answer1;
	}

	/**
	 * @param answer1 the answer1 to set
	 */
	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	/**
	 * @return the answer2
	 */
	public String getAnswer2() {
		return answer2;
	}

	/**
	 * @param answer2 the answer2 to set
	 */
	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	/**
	 * @return the answer3
	 */
	public String getAnswer3() {
		return answer3;
	}

	/**
	 * @param answer3 the answer3 to set
	 */
	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	/**
	 * @return the answer4
	 */
	public String getAnswer4() {
		return answer4;
	}

	/**
	 * @param answer4 the answer4 to set
	 */
	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	/**
	 * @return the answer5
	 */
	public String getAnswer5() {
		return answer5;
	}

	/**
	 * @param answer5 the answer5 to set
	 */
	public void setAnswer5(String answer5) {
		this.answer5 = answer5;
	}

	/**
	 * @return the answerType
	 */
	public String getAnswerType() {
		return answerType;
	}

	/**
	 * @param answerType the answerType to set
	 */
	public void setAnswerType(String answerType) {
		this.answerType = answerType;
	}

}
