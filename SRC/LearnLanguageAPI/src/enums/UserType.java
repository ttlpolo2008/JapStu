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
