/**
 * 
 */
package com.sinepulse.app.entities;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tanvir.ahmed
 *
 */
public class Summary {
	
	public int UserId ;
	public double TotalPower ;
	public int RoomCount ;
	public List<DeviceSummary> DeviceSummary  ;
	public ArrayList<DeviceSummary> deviceSummaryArray = new ArrayList<DeviceSummary>();
	
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
	 * @return the totalPower
	 */
	public double getTotalPower() {
		return TotalPower;
	}
	/**
	 * @param totalPower the totalPower to set
	 */
	public void setTotalPower(double totalPower) {
		TotalPower = totalPower;
	}
	/**
	 * @return the roomCount
	 */
	public int getRoomCount() {
		return RoomCount;
	}
	/**
	 * @param roomCount the roomCount to set
	 */
	public void setRoomCount(int roomCount) {
		RoomCount = roomCount;
	}
	/**
	 * @return the deviceSummary
	 */
	public List<DeviceSummary> getDeviceSummary() {
		return DeviceSummary;
	}
	/**
	 * @param deviceSummary the deviceSummary to set
	 */
	public void setDeviceSummary(List<DeviceSummary> deviceSummary) {
		DeviceSummary = deviceSummary;
	}
	


}
