package enums;

public enum ListeningType {
	REIBUN("1", "例文"),
	BUNKEI("2", "文型"),
	;

	/**
	 * CODE
	 */
	private String code;

	/**
	 * LABEL
	 */
	private String label;

	ListeningType(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public static ListeningType getListeningType(String code) {
		for (ListeningType listeningType : values()) {
			if (listeningType.getCode().equals(code)) {
				return listeningType;
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
