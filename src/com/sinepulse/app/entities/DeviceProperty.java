package com.sinepulse.app.entities;

import java.util.Collection;

/**
 * @author Tanvir
 * 
 */

public class DeviceProperty {

	public int Id;
	public int DeviceId;
	public boolean IsActionPending;
	public String PendingValue;
	public int PropertyId;
	public String Value;
	public int AuditId;
	public Audit Audit;
	public Collection<DevicePropertyDetail> DevicePropertyDetails;
	public Property Property;

	/**
	 * @return the isActionPending
	 */
	public boolean isIsActionPending() {
		return IsActionPending;
	}

	/**
	 * @param isActionPending
	 *            the isActionPending to set
	 */
	public void setIsActionPending(boolean isActionPending) {
		IsActionPending = isActionPending;
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
	 * @return the deviceId
	 */
	public int getDeviceId() {
		return DeviceId;
	}

	/**
	 * @param deviceId
	 *            the deviceId to set
	 */
	public void setDeviceId(int deviceId) {
		DeviceId = deviceId;
	}

	/**
	 * @return the propertyId
	 */
	public int getPropertyId() {
		return PropertyId;
	}

	/**
	 * @param propertyId
	 *            the propertyId to set
	 */
	public void setPropertyId(int propertyId) {
		PropertyId = propertyId;
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
	 * @return the auditId
	 */
	public int getAuditId() {
		return AuditId;
	}

	/**
	 * @param auditId
	 *            the auditId to set
	 */
	public void setAuditId(int auditId) {
		AuditId = auditId;
	}

	/**
	 * @return the audit
	 */
	public Audit getAudit() {
		return Audit;
	}

	/**
	 * @param audit
	 *            the audit to set
	 */
	public void setAudit(Audit audit) {
		Audit = audit;
	}

	/**
	 * @return the devicePropertyDetails
	 */
	public Collection<DevicePropertyDetail> getDevicePropertyDetails() {
		return DevicePropertyDetails;
	}

	/**
	 * @param devicePropertyDetails
	 *            the devicePropertyDetails to set
	 */
	public void setDevicePropertyDetails(
			Collection<DevicePropertyDetail> devicePropertyDetails) {
		DevicePropertyDetails = devicePropertyDetails;
	}

	/**
	 * @return the property
	 */
	public Property getProperty() {
		return Property;
	}

	/**
	 * @param property
	 *            the property to set
	 */
	public void setProperty(Property property) {
		Property = property;
	}

	/**
	 * @return the pendingValue
	 */
	public String getPendingValue() {
		return PendingValue;
	}

	/**
	 * @param pendingValue
	 *            the pendingValue to set
	 */
	public void setPendingValue(String pendingValue) {
		PendingValue = pendingValue;
	}

}
