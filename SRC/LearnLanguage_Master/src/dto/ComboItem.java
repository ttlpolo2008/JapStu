package dto;

public class ComboItem {
	private Long value;
	private String label;

	public ComboItem(Long value, String label) {
		this.value = value;
		this.label = label;
	}

	public ComboItem(String value, String label) {
		this.value = Long.parseLong(value);
		this.label = label;
	}

	/**
	 * @return the value
	 */
	public Long getValue() {
		return value;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(Long value) {
		this.value = value;
	}

	/**
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label the label to set
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

}
