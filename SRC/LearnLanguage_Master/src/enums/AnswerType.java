package enums;

public enum AnswerType {
	CHOOSE_ONE("1", "Choose one right answer."),
	CHOOSE_MANY("2", "Choose all right answer.");

	/**
	 * CODE
	 */
	private String code;

	/**
	 * LABEL
	 */
	private String label;

	AnswerType(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public static AnswerType getAnswerType(String code) {
		for (AnswerType answerType : values()) {
			if (answerType.getCode().equals(code)) {
				return answerType;
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
