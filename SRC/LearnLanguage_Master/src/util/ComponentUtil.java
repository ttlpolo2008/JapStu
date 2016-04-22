package util;

import java.awt.Component;
import java.awt.Container;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import dto.ComboItem;

public class ComponentUtil {

	/**
	 * Enable/Disable all components.
	 *
	 * @param panel
	 * @param enable
	 */
	public static void enableComponents(Container container, Boolean enable) {
		Component[] components = container.getComponents();
		for (Component component : components) {
			component.setEnabled(enable);
			if (component instanceof Container) {
				enableComponents((Container)component, enable);
			}
		}
	}

	/**
	 * Select combo item by value
	 *
	 * @param combo
	 * @param value
	 */
	@SuppressWarnings("rawtypes")
	public static void selectItem(JComboBox combo, String value) {
		for (int index = 0; index < combo.getItemCount(); index++) {
			ComboItem item = (ComboItem) combo.getItemAt(index);
			if (item.getValue().toString().equals(value)) {
				combo.setSelectedIndex(index);
				return;
			}
		}
	}

	/**
	 * Set value of checkbox
	 *
	 * @param combo
	 * @param value
	 */
	public static void setCheckBoxValue(JCheckBox checkbox, char value) {
		checkbox.setSelected(false);
		if ("1".equals(StringUtil.cnvToString(value))) {
			checkbox.setSelected(true);
		}
	}

	/**
	 * Get value of checkbox
	 *
	 * @param combo
	 * @param value
	 * @return String
	 */
	public static String getCheckBoxValue(JCheckBox checkbox) {
		if (checkbox.isSelected()) {
			return "1";
		}
		return "0";
	}

}
