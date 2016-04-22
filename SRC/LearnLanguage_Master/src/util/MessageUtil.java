package util;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MessageUtil {

	/**
	 * Show error message
	 *
	 * @param frame
	 * @param message
	 */
	public static void showErrorMessage(Component frame, String message) {
		JOptionPane.showMessageDialog(frame, message, "Error", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Show info message
	 *
	 * @param frame
	 * @param message
	 */
	public static void showInfoMessage(Component frame, String message) {
		JOptionPane.showMessageDialog(frame, message, "Info", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Show confirm message
	 *
	 * @param frame
	 * @param message
	 * @return True:Yes / False:No
	 */
	public static Boolean showConfirmMessage(Component frame, String message) {
		int confirmResult =
				JOptionPane.showConfirmDialog(frame, message, "Confirm", JOptionPane.YES_NO_OPTION);
		if (confirmResult == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}
}
