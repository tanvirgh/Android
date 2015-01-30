package com.sinepulse.app.entities;

import java.util.Date;


public class Audit {
	
	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return CreatedAt;
	}
	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}
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
	 * @return the message
	 */
	public String getMessage() {
		return Message;
	}
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		Message = message;
	}
	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return UpdatedAt;
	}
	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		UpdatedAt = updatedAt;
	}
	/**
	 * @return the userId
	 */
	public int getUserId() {
		return UserId;
	}
	/**
	 * @param userId the userId to set
	 */
	public void setUserId(int userId) {
		UserId = userId;
	}
	/**
	 * @return the userProfile
	 */
	public UserProfile getUserProfile() {
		return userProfile;
	}
	/**
	 * @param userProfile the userProfile to set
	 */
	public void setUserProfile(UserProfile userProfile) {
		this.userProfile = userProfile;
	}
	public Date CreatedAt ;
	public int Id ;
	public String Message ;
	public Date UpdatedAt ;
	public int UserId ;
	public UserProfile userProfile ;


}
