package com.sinepulse.app.entities;

/**
 * @author Tanvir
 *
 */

public class Property {
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
	 * @return the displayName
	 */
	public String getDisplayName() {
		return DisplayName;
	}
	/**
	 * @param displayName the displayName to set
	 */
	public void setDisplayName(String displayName) {
		DisplayName = displayName;
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
	 * @return the unit
	 */
	public PropertyUnit getUnit() {
		return Unit;
	}
	/**
	 * @param unit the unit to set
	 */
	public void setUnit(PropertyUnit unit) {
		Unit = unit;
	}
	/**
	 * @return the dataType
	 */
	public DataType getDataType() {
		return DataType;
	}
	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(DataType dataType) {
		DataType = dataType;
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
	public int Id ;
	public String Name  ;
	public String DisplayName  ;
	public String Comment  ;
	public PropertyUnit Unit  ;
	public DataType DataType  ;
	public int AuditId  ;
	public Audit Audit  ;
	public int HomeVersionId  ;
	public int MasterId  ;
	public HomeVersion HomeVersion  ;


}
