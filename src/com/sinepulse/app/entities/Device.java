package com.sinepulse.app.entities;

/**
 * @author tanvir.ahmed
 *
 */

public class Device {
	
public int RowId;//for internal use...
	
	public int Id ;
	public String Name ;
	public int DeviceTypeId ;
	public int RoomId ;
	public String HardwareId ;
	public boolean IsOn ;
	public boolean IsActive ;
	public double RealPower ;
	public String RoomName ;
	public boolean IsActionPending;
	

	/**
	 * @return the rowId
	 */
	public int getRowId() {
		return RowId;
	}
	/**
	 * @param rowId the rowId to set
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
	 * @param id the id to set
	 */
	public void setId(int id) {
		Id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return Name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		Name = name;
	}
	/**
	 * @return the deviceTypeId
	 */
	public int getDeviceTypeId() {
		return DeviceTypeId;
	}
	/**
	 * @param deviceTypeId the deviceTypeId to set
	 */
	public void setDeviceTypeId(int deviceTypeId) {
		DeviceTypeId = deviceTypeId;
	}
	/**
	 * @return the roomId
	 */
	public int getRoomId() {
		return RoomId;
	}
	/**
	 * @param roomId the roomId to set
	 */
	public void setRoomId(int roomId) {
		RoomId = roomId;
	}
	/**
	 * @return the hardwareId
	 */
	public String getHardwareId() {
		return HardwareId;
	}
	/**
	 * @param hardwareId the hardwareId to set
	 */
	public void setHardwareId(String hardwareId) {
		HardwareId = hardwareId;
	}
	/**
	 * @return the isOn
	 */
	public boolean isIsOn() {
		return IsOn;
	}
	/**
	 * @param isOn the isOn to set
	 */
	public void setIsOn(boolean isOn) {
		IsOn = isOn;
	}
	/**
	 * @return the isActive
	 */
	public boolean isIsActive() {
		return IsActive;
	}
	/**
	 * @param isActive the isActive to set
	 */
	public void setIsActive(boolean isActive) {
		IsActive = isActive;
	}
	/**
	 * @return the realPower
	 */
	public double getRealPower() {
		return RealPower;
	}
	/**
	 * @param realPower the realPower to set
	 */
	public void setRealPower(double realPower) {
		RealPower = realPower;
	}
	/**
	 * @return the roomName
	 */
	public String getRoomName() {
		return RoomName;
	}
	/**
	 * @param roomName the roomName to set
	 */
	public void setRoomName(String roomName) {
		RoomName = roomName;
	}
	/**
	 * @return the isActionPending
	 */
	public boolean isIsActionPending() {
		return IsActionPending;
	}
	/**
	 * @param isActionPending the isActionPending to set
	 */
	public void setIsActionPending(boolean isActionPending) {
		IsActionPending = isActionPending;
	}
	

	
	
}
