package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import dao.CommonDAO;
import dto.UserDto;
import util.StringUtil;

public class UserService {

	/**
	 * Find User
	 *
	 * @param userName
	 * @return UserDto
	 */
	public static UserDto findUserByUserName(String userName) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   USER_ID \n");
			sqlQuery.append("  ,USER_NAME \n");
			sqlQuery.append("  ,PASS_WORD \n");
			sqlQuery.append("  ,USER_TYPE \n");
			sqlQuery.append("  ,NICK_NAME \n");
			sqlQuery.append("  ,AGE \n");
			sqlQuery.append("  ,PROFILE_IMAGE \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_USER \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       1 = 1 \n");
			sqlQuery.append("  AND USER_NAME LIKE ? \n");

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());
			stmt.setString(1, userName);

			// Execute query
			rs = stmt.executeQuery();
			while (rs.next()) {
				// Edit Dto
				UserDto userDto = new UserDto();
				userDto.setUserId(rs.getLong("USER_ID"));
				userDto.setUserName(rs.getString("USER_NAME"));
				userDto.setPassword(rs.getString("PASS_WORD"));
				userDto.setUserType(rs.getString("USER_TYPE"));
				userDto.setNickName(rs.getString("NICK_NAME"));
				userDto.setAge(rs.getInt("AGE"));
				userDto.setProfileImage(rs.getString("PROFILE_IMAGE"));

				return userDto;
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception ex) {
			}
		}

		return null;
	}

	/**
	 * Find User
	 *
	 * @param userName
	 * @param password
	 * @return List<UserDto>
	 */
	public static UserDto findUserInfo(String userName, String password) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" SELECT \n");
			sqlQuery.append("   USER_ID \n");
			sqlQuery.append("  ,USER_NAME \n");
			sqlQuery.append("  ,PASS_WORD \n");
			sqlQuery.append("  ,USER_TYPE \n");
			sqlQuery.append("  ,NICK_NAME \n");
			sqlQuery.append("  ,AGE \n");
			sqlQuery.append("  ,PROFILE_IMAGE \n");
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_USER \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       1 = 1 \n");

			// Add condition SQL
			if (!StringUtil.isNullOrEmpty(userName)) {
				sqlQuery.append("  AND USER_NAME LIKE ? \n");
			}
			if (!StringUtil.isNullOrEmpty(password)) {
				sqlQuery.append("  AND PASS_WORD LIKE ? \n");
			}

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString()); //stmt là kết nối, trong kết nối có tạo Statement

			// Add condition
			int conditionIndex = 1;
			if (!StringUtil.isNullOrEmpty(userName)) {
				stmt.setString(conditionIndex, userName);
				conditionIndex++;
			}
			if (!StringUtil.isNullOrEmpty(password)) {
				stmt.setString(conditionIndex, password);
				conditionIndex++;
			}

			// Execute query
			rs = stmt.executeQuery();
			if (rs.next()) {
				// Edit Dto
				UserDto userDto = new UserDto();
				userDto.setUserId(rs.getLong("USER_ID"));
				userDto.setUserName(rs.getString("USER_NAME"));
				userDto.setPassword(rs.getString("PASS_WORD"));
				userDto.setUserType(rs.getString("USER_TYPE"));
				userDto.setNickName(rs.getString("NICK_NAME"));
				userDto.setAge(rs.getInt("AGE"));
				userDto.setProfileImage(rs.getString("PROFILE_IMAGE"));

				return userDto;
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (stmt != null) {
					stmt.close();
				}
			} catch (Exception ex) {
			}
		}

		return null;
	}
	/**
	 * Insert User
	 *
	 * @param userDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean insertUser(UserDto userDto) {

		// if [userDto = null], return false
		if (userDto == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" INSERT INTO T_USER \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     USER_NAME \n");
			sqlQuery.append("    ,PASS_WORD \n");
			sqlQuery.append("    ,USER_TYPE \n");
			sqlQuery.append("    ,NICK_NAME \n");
			sqlQuery.append("    ,AGE \n");
			sqlQuery.append("    ,PROFILE_IMAGE \n");
			sqlQuery.append("   ) \n");
			sqlQuery.append(" VALUES \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     ? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("   ) \n");

			// Create Statement
			PreparedStatement stmt =
					conn.prepareStatement(
						sqlQuery.toString(), Statement.RETURN_GENERATED_KEYS); //RETURN_GENERATED_KEYS để lấy id tự sinh ngay sau khi insert.

			// Edit parameter
			stmt.setString(1, userDto.getUserName());
			stmt.setString(2, userDto.getPassword());
			stmt.setString(3, userDto.getUserType());
			stmt.setString(4, userDto.getNickName());
			stmt.setInt(5, userDto.getAge());
			stmt.setString(6, userDto.getProfileImage());

			// Execute SQL
			int cnt = stmt.executeUpdate();
			if (cnt > 0) {	//nếu đk được ít nhất 1 record.
				ResultSet rs = stmt.getGeneratedKeys(); //sau đó đưa nó vào một resultset bằng stmt.getGeneratedKeys()
				rs.next();
				userDto.setUserId(rs.getLong(1));
				rs.close();
				return true;
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
