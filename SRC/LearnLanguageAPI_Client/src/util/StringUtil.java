package util;

public class StringUtil {

	/**
	 * Check null or empty
	 *
	 * @param value
	 * @return Boolean
	 */
	public static Boolean isNullOrEmpty(Object value) {
		if (value == null || "".equals(value.toString())) {
			return true;
		}
		return false;
	}

	/**
	 * Convert Object to String
	 *
	 * @param value
	 * @return String
	 */
	public static String cnvToString(Object value) {
		if (value == null) {
			return "";
		}
		return value.toString();
	}

	/**
	 * Convert Object to Integer
	 *
	 * @param value
	 * @return
	 */
	public static Integer cnvToInt(Object value) {
		try {
			return Integer.parseInt(value.toString());
		} catch (Exception ex) {
//			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * Convert Object to Long
	 *
	 * @param value
	 * @return
	 */
	public static Long cnvToLong(Object value) {
		try {
			return Long.parseLong(value.toString());
		} catch (Exception ex) {
//			ex.printStackTrace();
		}
		return null;
	}

}
