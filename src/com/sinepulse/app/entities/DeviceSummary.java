/**
 * 
 */
package com.sinepulse.app.entities;

/**
 * @author tanvir.ahmed
 *
 */
public class DeviceSummary {
	
	public int DeviceTypeId ;
	public int DeviceCount ;
	public int RunningDeviceCount ;
	public double PowerUsage ;
	public double PowerUsagePerc ;
	
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
	 * @return the deviceCount
	 */
	public int getDeviceCount() {
		return DeviceCount;
	}
	/**
	 * @param deviceCount the deviceCount to set
	 */
	public void setDeviceCount(int deviceCount) {
		DeviceCount = deviceCount;
	}
	/**
	 * @return the runningDeviceCount
	 */
	public int getRunningDeviceCount() {
		return RunningDeviceCount;
	}
	/**
	 * @param runningDeviceCount the runningDeviceCount to set
	 */
	public void setRunningDeviceCount(int runningDeviceCount) {
		RunningDeviceCount = runningDeviceCount;
	}
	/**
	 * @return the powerUsage
	 */
	public double getPowerUsage() {
		return PowerUsage;
	}
	/**
	 * @param powerUsage the powerUsage to set
	 */
	public void setPowerUsage(double powerUsage) {
		PowerUsage = powerUsage;
	}
	/**
	 * @return the powerUsagePerc
	 */
	public double getPowerUsagePerc() {
		return PowerUsagePerc;
	}
	/**
	 * @param powerUsagePerc the powerUsagePerc to set
	 */
	public void setPowerUsagePerc(double powerUsagePerc) {
		PowerUsagePerc = powerUsagePerc;
	}
	


}
