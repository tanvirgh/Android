/**
 * 
 */
package com.sinepulse.app.entities;

/**
 * @author tanvir.ahmed
 *
 */
public class HomeLink {
	
	
	public int Id ;
	public int homeId ;
	public String name ;
	public String IpAddress ;
	public int port ;
	public String videoQuality ;
	public String userName ;
	public String password ;
	public int ChannelCount ;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		Id = id;
	}
	/**
	 * @return the homeId
	 */
	public int getHomeId() {
		return homeId;
	}
	/**
	 * @param homeId the homeId to set
	 */
	public void setHomeId(int homeId) {
		this.homeId = homeId;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the ipAddress
	 */
	public String getIpAddress() {
		return IpAddress;
	}
	/**
	 * @param ipAddress the ipAddress to set
	 */
	public void setIpAddress(String ipAddress) {
		IpAddress = ipAddress;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		this.port = port;
	}
	/**
	 * @return the videoQuality
	 */
	public String getVideoQuality() {
		return videoQuality;
	}
	/**
	 * @param videoQuality the videoQuality to set
	 */
	public void setVideoQuality(String videoQuality) {
		this.videoQuality = videoQuality;
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
	 * @return the channelCount
	 */
	public int getChannelCount() {
		return ChannelCount;
	}
	/**
	 * @param channelCount the channelCount to set
	 */
	public void setChannelCount(int channelCount) {
		ChannelCount = channelCount;
	}
	


}
