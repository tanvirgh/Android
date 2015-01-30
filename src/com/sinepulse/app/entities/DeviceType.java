package com.sinepulse.app.entities;

import java.util.Collection;

/**
 * @author Tanvir
 *
 */

public class DeviceType {
	
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
	 * @return the comment
	 */
	public String getComment() {
		return Comment;
	}
	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		Comment = comment;
	}
	/**
	 * @return the status
	 */
	public boolean isStatus() {
		return Status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(boolean status) {
		Status = status;
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
	 * @return the homeVersion
	 */
	public HomeVersion getHomeVersion() {
		return HomeVersion;
	}
	/**
	 * @param homeVersion the homeVersion to set
	 */
	public void setHomeVersion(HomeVersion homeVersion) {
		HomeVersion = homeVersion;
	}
	/**
	 * @return the deviceTypeProperties
	 */
	public Collection<DeviceTypeProperty> getDeviceTypeProperties() {
		return DeviceTypeProperties;
	}
	/**
	 * @param deviceTypeProperties the deviceTypeProperties to set
	 */
	public void setDeviceTypeProperties(
			Collection<DeviceTypeProperty> deviceTypeProperties) {
		DeviceTypeProperties = deviceTypeProperties;
	}
	public int Id ;
	public String Name ;
	public String Comment ;
	public boolean Status ;
	public int AuditId ;
	public Audit Audit ;
	public int HomeVersionId ;
	public int MasterId ;
	public HomeVersion HomeVersion ;
	public Collection<DeviceTypeProperty> DeviceTypeProperties ;


}
