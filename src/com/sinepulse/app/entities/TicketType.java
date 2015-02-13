/**
 * 
 */
package com.sinepulse.app.entities;

/**
 * @author tanvir.ahmed
 *
 */
public class TicketType {
	
//	public virtualAudit Audit ;
	public int AuditId;
	public String Description ;
	public int Id ;
	public String TypeName ;
	
	
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
	 * @return the description
	 */
	public String getDescription() {
		return Description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		Description = description;
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
	 * @return the typeName
	 */
	public String getTypeName() {
		return TypeName;
	}
	/**
	 * @param typeName the typeName to set
	 */
	public void setTypeName(String typeName) {
		TypeName = typeName;
	}
	


}
