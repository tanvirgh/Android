package com.sinepulse.app.entities;

import java.util.Date;

/**
 * @author Tanvir
 * 
 */

public class DevicePropertyDetail {

	public int RowId;// for internal use...
	public int Id;
	public int DevicePropertyId;
	public String Value;
	public Date LoggedAt;
	public boolean IsLatest;
	// public SyncStatus ServerSyncStatus ;
	// public SyncStatus DCSyncStatus ;
	public boolean IsActive;
	public int UserId;

	/**
	 * @return the rowId
	 */
	public int getRowId() {
		return RowId;
	}

	/**
	 * @param rowId
	 *            the rowId to set
	 */
	public void setRowId(int rowId) {
		RowId = rowId;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return Id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		Id = id;
	}

	/**
	 * @return the devicePropertyId
	 */
	public int getDevicePropertyId() {
		return DevicePropertyId;
	}

	/**
	 * @param devicePropertyId
	 *            the devicePropertyId to set
	 */
	public void setDevicePropertyId(int devicePropertyId) {
		DevicePropertyId = devicePropertyId;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return Value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		Value = value;
	}

	/**
	 * @return the loggedAt
	 */
	public Date getLoggedAt() {
		return LoggedAt;
	}

	/**
	 * @param loggedAt
	 *            the loggedAt to set
	 */
	public void setLoggedAt(Date loggedAt) {
		LoggedAt = loggedAt;
	}

	/**
	 * @return the isLatest
	 */
	public boolean isIsLatest() {
		return IsLatest;
	}

	/**
	 * @param isLatest
	 *            the isLatest to set
	 */
	public void setIsLatest(boolean isLatest) {
		IsLatest = isLatest;
	}

	/**
	 * @return the isActive
	 */
	public boolean isIsActive() {
		return IsActive;
	}

	/**
	 * @param isActive
	 *            the isActive to set
	 */
	public void setIsActive(boolean isActive) {
		IsActive = isActive;
	}

	/**
	 * @return the userId
	 */
	public int getUserId() {
		return UserId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(int userId) {
		UserId = userId;
	}

}
