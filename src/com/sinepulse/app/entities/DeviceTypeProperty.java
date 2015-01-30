package com.sinepulse.app.entities;

/**
 * @author Tanvir
 *
 */

public class DeviceTypeProperty {
	
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
	 * @return the homeVersionId
	 */
	public int getHomeVersionId() {
		return HomeVersionId;
	}
	/**
	 * @param homeVersionId the homeVersionId to set
	 */
	public void setHomeVersionId(int homeVersionId) {
		HomeVersionId = homeVersionId;
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
	 * @return the propertyId
	 */
	public int getPropertyId() {
		return PropertyId;
	}
	/**
	 * @param propertyId the propertyId to set
	 */
	public void setPropertyId(int propertyId) {
		PropertyId = propertyId;
	}
	/**
	 * @return the defaultValue
	 */
	public String getDefaultValue() {
		return DefaultValue;
	}
	/**
	 * @param defaultValue the defaultValue to set
	 */
	public void setDefaultValue(String defaultValue) {
		DefaultValue = defaultValue;
	}
	/**
	 * @return the auditId
	 */
	public int getAuditId() {
		return AuditId;
	}
	/**
	 * @param auditId the auditId to set
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
	 * @param audit the audit to set
	 */
	public void setAudit(Audit audit) {
		Audit = audit;
	}
	/**
	 * @return the masterId
	 */
	public int getMasterId() {
		return MasterId;
	}
	/**
	 * @param masterId the masterId to set
	 */
	public void setMasterId(int masterId) {
		MasterId = masterId;
	}
	/**
	 * @return the versionHomeVersion
	 */
	public Home getVersionHomeVersion() {
		return VersionHomeVersion;
	}
	/**
	 * @param versionHomeVersion the versionHomeVersion to set
	 */
	public void setVersionHomeVersion(Home versionHomeVersion) {
		VersionHomeVersion = versionHomeVersion;
	}
	/**
	 * @return the property
	 */
	public Property getProperty() {
		return Property;
	}
	/**
	 * @param property the property to set
	 */
	public void setProperty(Property property) {
		Property = property;
	}
	public int Id ;
	public int HomeVersionId  ;
	public int DeviceTypeId  ;
	public int PropertyId  ;
	public String DefaultValue  ;
	public int  AuditId  ;
	public Audit Audit  ;
	public int MasterId  ;
	public Home VersionHomeVersion  ;
	public Property Property  ;


}
