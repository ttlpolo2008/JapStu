package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class CommonDAO {

	private static Connection conn;

	/**
	 * Connect DB
	 *
	 * @return result True:success / False:fail
	 */
	public static Boolean connectDB() {

		String url = "jdbc:mysql://localhost:3306/LearnLanguage";
		String username = "root";
		String password = "123456";

		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(url, username, password);
			conn.setAutoCommit(false);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		if (conn != null) {
			return true;
		}
		return false;
	}

	/**
	 * Get DAO
	 *
	 * @return Connection
	 */
	public static Connection getDAO() {
		if (conn == null) {
			connectDB();
		}
		return conn;
	}
}
;