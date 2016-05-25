//chua cac common dc sdung trong project.

package util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;

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
	 * Read upload file
	 *
	 * @param fileName
	 * @return String
	 */
	public static byte[] readUploadFile(String fileName) {
		if (!isNullOrEmpty(fileName)) {
			try {
				return Files.readAllBytes(Paths.get(Constants.UPLOAD_FOLDER + fileName));
			} catch (Exception ex) {
			}
		}
		return null;
	}

	public static java.sql.Date cnvToDBDate(Date value) {
		if (value == null) {
			return null;
		}
		return new java.sql.Date(value.getTime());
	}
}
