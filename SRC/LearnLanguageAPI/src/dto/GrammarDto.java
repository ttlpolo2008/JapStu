package dto;

import java.io.Serializable;

public class GrammarDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2761990023414004874L;

	/**
	 * GRAMMAR_ID
	 */
	private Long grammarId;

	/**
	 * LESSON_COURSE_ID
	 */
	private Long lessonCourseId;

	/**
	 * SYNTAX
	 */
	private String syntax;

	/**
	 * EXPLAIN
	 */
	private String explain;

	/**
	 * EXAMPLE
	 */
	private String example;

	/**
	 * ORDER_INDEX
	 */
	private Integer orderIndex;

	/**
	 * @return the grammarId
	 */
	public Long getGrammarId() {
		return grammarId;
	}

	/**
	 * @param grammarId the grammarId to set
	 */
	public void setGrammarId(Long grammarId) {
		this.grammarId = grammarId;
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
	 * @return the syntax
	 */
	public String getSyntax() {
		return syntax;
	}

	/**
	 * @param syntax the syntax to set
	 */
	public void setSyntax(String syntax) {
		this.syntax = syntax;
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
	 * @return the example
	 */
	public String getExample() {
		return example;
	}

	/**
	 * @param example the example to set
	 */
	public void setExample(String example) {
		this.example = example;
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



}
