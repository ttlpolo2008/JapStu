package services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import dao.CommonDAO;
import dto.UserDto;
import util.StringUtil;

public class UserService {

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
			stmt = conn.prepareStatement(sqlQuery.toString());

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
	 * Search User
	 *
	 * @param conditionDto
	 * @return List<UserDto>
	 */
	public static List<UserDto> searchUser(UserDto conditionDto) {

		// Result list
		List<UserDto> userDtoList = new ArrayList<UserDto>();

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
			sqlQuery.append(" FROM \n");
			sqlQuery.append("   T_USER \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       1 = 1 \n");

			// Add condition SQL
			if (conditionDto != null) {
				if (!StringUtil.isNullOrEmpty(conditionDto.getUserId())) {
					sqlQuery.append("  AND USER_ID = ? \n");
				}
				if (!StringUtil.isNullOrEmpty(conditionDto.getUserName())) {
					sqlQuery.append("  AND USER_NAME LIKE ? \n");
				}
				if (!StringUtil.isNullOrEmpty(conditionDto.getUserType())) {
					sqlQuery.append("  AND USER_TYPE LIKE ? \n");
				}
				if (!StringUtil.isNullOrEmpty(conditionDto.getNickName())) {
					sqlQuery.append("  AND NICK_NAME LIKE ? \n");
				}
			}

			// Order
			sqlQuery.append(" ORDER BY \n");
			sqlQuery.append("   USER_NAME \n");

			// Create Statement
			stmt = conn.prepareStatement(sqlQuery.toString());

			// Add condition
			if (conditionDto != null) {
				int conditionIndex = 1;
				if (!StringUtil.isNullOrEmpty(conditionDto.getUserId())) {
					stmt.setLong(conditionIndex, conditionDto.getUserId());
					conditionIndex++;
				}
				if (!StringUtil.isNullOrEmpty(conditionDto.getUserName())) {
					stmt.setString(conditionIndex, "%" + conditionDto.getUserName() + "%");
					conditionIndex++;
				}
				if (!StringUtil.isNullOrEmpty(conditionDto.getUserType())) {
					stmt.setString(conditionIndex, "%" + conditionDto.getUserType() + "%");
					conditionIndex++;
				}
				if (!StringUtil.isNullOrEmpty(conditionDto.getNickName())) {
					stmt.setString(conditionIndex, "%" + conditionDto.getNickName() + "%");
					conditionIndex++;
				}
			}

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

				// Add Dto to List
				userDtoList.add(userDto);
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

		return userDtoList;
	}

	/**
	 * Register all User
	 *
	 * @param userDtoList
	 * @param deletedList
	 * @return Boolean True:Success / False:Error
	 */
	public static Boolean registerAll(List<UserDto> userDtoList,
									  List<Long> deletedList) {

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Register Data
			for (UserDto userDto : userDtoList) {

				if (userDto.getUserId() == null) {
					// Register new data
					if (!UserService.insertUser(userDto)
							|| !LearnService.createLearn(userDto.getUserId())) {
						conn.rollback();
						return false;
					}

				} else if (userDto.getIsChange()) {
					// Update data
					if (!UserService.updateUser(userDto)) {
						conn.rollback();
						return false;
					}

				}
			}

			// Delete data
			for (Long userId : deletedList) {
				// Delete data
				LearnService.dropLearn(userId);
				if (!UserService.deleteUser(userId)) {
					conn.rollback();
					return false;
				}
			}

			// Commit Transaction
			conn.commit();

			return true;

		} catch (Exception ex) {
			ex.printStackTrace();
			try {
				// Rollback Transaction
				if (conn != null) {
					conn.rollback();
				}
			} catch(Exception ex1) {
			}
		}

		return false;
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
			sqlQuery.append("   ) \n");
			sqlQuery.append(" VALUES \n");
			sqlQuery.append("   ( \n");
			sqlQuery.append("     ? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("    ,? \n");
			sqlQuery.append("   ) \n");

			// Create Statement
			PreparedStatement stmt =
					conn.prepareStatement(
						sqlQuery.toString(), Statement.RETURN_GENERATED_KEYS);

			// Edit parameter
			stmt.setString(1, userDto.getUserName());
			stmt.setString(2, userDto.getPassword());
			stmt.setString(3, userDto.getUserType());
			stmt.setString(4, userDto.getNickName());

			// Execute SQL
			int cnt = stmt.executeUpdate();
			if (cnt > 0) {
				ResultSet rs = stmt.getGeneratedKeys();
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

	/**
	 * Update User
	 *
	 * @param userDto
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean updateUser(UserDto userDto) {

		// if [userDto = null] or [userNo = null], return false
		if (userDto == null || userDto.getUserId() == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" UPDATE T_USER \n");
			sqlQuery.append(" SET \n");
			sqlQuery.append("     USER_NAME = ? \n");
			sqlQuery.append("    ,PASS_WORD = ? \n");
			sqlQuery.append("    ,USER_TYPE = ? \n");
			sqlQuery.append("    ,NICK_NAME = ? \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       USER_ID = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setString(1, userDto.getUserName());
			stmt.setString(2, userDto.getPassword());
			stmt.setString(3, userDto.getUserType());
			stmt.setString(4, userDto.getNickName());
			stmt.setLong(5, userDto.getUserId());

			// Execute SQL
			int cnt = stmt.executeUpdate();
			if (cnt > 0) {
				return true;
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}

	/**
	 * Delete User
	 *
	 * @param userId
	 * @return Boolean True:Success / False:Fail
	 */
	public static Boolean deleteUser(Long userId) {

		// if [userId = null], return false
		if (userId == null) {
			return false;
		}

		// Get DAO
		Connection conn = CommonDAO.getDAO();
		try {
			// Create SQL
			StringBuffer sqlQuery = new StringBuffer();
			sqlQuery.append(" DELETE FROM T_USER \n");
			sqlQuery.append(" WHERE \n");
			sqlQuery.append("       USER_ID = ? \n");

			// Create Statement
			PreparedStatement stmt = conn.prepareStatement(sqlQuery.toString());

			// Edit parameter
			stmt.setLong(1, userId);

			// Execute SQL
			int cnt = stmt.executeUpdate();
			if (cnt > 0) {
				return true;
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}
		return false;
	}
}
