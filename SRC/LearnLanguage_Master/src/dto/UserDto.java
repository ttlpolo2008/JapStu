package dto;

import java.io.Serializable;

public class UserDto implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4100989899811205586L;

	/**
	 * USER_ID
	 */
	private Long userId;

	/**
	 * USER_NAME
	 */
	private String userName;

	/**
	 * PASS_WORD
	 */
	private String password;

	/**
	 * USER_TYPE
	 */
	private String userType;

	/**
	 * NICK_NAME
	 */
	private String nickName;

	/**
	 * Change's flag
	 */
	private Boolean isChange;

	public UserDto() {
		this.isChange = false;
	}

	/**
	 * Create a copy of this data.
	 *
	 * @return LessonDto
	 */
	public UserDto copy() {
		UserDto lessonDto = new UserDto();
		lessonDto.setUserId(this.userId);
		lessonDto.setUserName(this.userName);
		lessonDto.setPassword(this.password);
		lessonDto.setUserType(this.userType);
		lessonDto.setNickName(this.nickName);
		lessonDto.setIsChange(this.isChange);

		return lessonDto;
	}

	/**
	 * @return the userId
	 */
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	/**
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the userType
	 */
	public String getUserType() {
		return userType;
	}

	/**
	 * @param userType the userType to set
	 */
	public void setUserType(String userType) {
		this.userType = userType;
	}

	/**
	 * @return the nickName
	 */
	public String getNickName() {
		return nickName;
	}

	/**
	 * @param nickName the nickName to set
	 */
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	/**
	 * @return the isChange
	 */
	public Boolean getIsChange() {
		return isChange;
	}

	/**
	 * @param isChange the isChange to set
	 */
	public void setIsChange(Boolean isChange) {
		this.isChange = isChange;
	}

}
