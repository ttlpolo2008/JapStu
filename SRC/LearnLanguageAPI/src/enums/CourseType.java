package enums;

public enum CourseType {
	VOCABULARY("1"),
	GRAMMAR("2"),
	READING("3"),
	LISTENING("4"),
	CONVERSATION("5"),
	KANJI("6");

	/**
	 * CODE
	 */
	private String code;

	CourseType(String code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}
}
