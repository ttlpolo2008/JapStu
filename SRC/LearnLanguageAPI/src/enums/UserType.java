package enums;

public enum UserType {
	ADMIN("1", "ADMIN"),
	USER("2", "USER");

	/**
	 * CODE
	 */
	private String code;

	/**
	 * LABEL
	 */
	private String label;

	UserType(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public static UserType getUserType(String code) {
		for (UserType answerType : values()) {
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
