package enums;

public enum QuestionType {
	VOCABULARY("1", "Vocabulary"),
	GRAMMAR("2", "Grammar"),
	READING("3", "Reading"),
	LISTENING("4", "Listening"),
	CONVERSATION("5", "Conversation"),
	KANJI("6", "Kanji"),
	;

	/**
	 * CODE
	 */
	private String code;

	/**
	 * LABEL
	 */
	private String label;

	QuestionType(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public static QuestionType getQuestionType(String code) {
		for (QuestionType questionType : values()) {
			if (questionType.getCode().equals(code)) {
				return questionType;
			}
		}
		return null;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	@Override
	public String toString() {
		return label;
	}
}
