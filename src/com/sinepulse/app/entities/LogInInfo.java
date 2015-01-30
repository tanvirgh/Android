package com.sinepulse.app.entities;

/**
 * @author Tanvir
 *
 */

public class LogInInfo {
	
	private String userName;
	private String userpassword;
	private String AppToken;
	private String AppType;
	
	/**
	 * @return the appType
	 */
	public String getAppType() {
		return AppType;
	}
	/**
	 * @param appType the appType to set
	 */
	public void setAppType(String appType) {
		AppType = appType;
	}
	/**
	 * @return the appToken
	 */
	public String getAppToken() {
		return AppToken;
	}
	/**
	 * @param appToken the appToken to set
	 */
	public void setAppToken(String appToken) {
		AppToken = appToken;
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
	 * @return the userpassword
	 */
	public String getUserpassword() {
		return userpassword;
	}
	/**
	 * @param userpassword the userpassword to set
	 */
	public void setUserpassword(String userpassword) {
		this.userpassword = userpassword;
	}

}
