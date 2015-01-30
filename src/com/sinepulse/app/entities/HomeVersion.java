package com.sinepulse.app.entities;

import java.util.Collection;
import java.util.Date;

/**
 * @author Tanvir
 *
 */

public class HomeVersion {
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
	 * @return the code
	 */
	public String getCode() {
		return Code;
	}
	/**
	 * @param code the code to set
	 */
	public void setCode(String code) {
		Code = code;
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
	 * @return the launchDate
	 */
	public Date getLaunchDate() {
		return LaunchDate;
	}
	/**
	 * @param launchDate the launchDate to set
	 */
	public void setLaunchDate(Date launchDate) {
		LaunchDate = launchDate;
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
	 * @return the homeVersionCommands
	 */
	public Collection<HomeVersionCommand> getHomeVersionCommands() {
		return HomeVersionCommands;
	}
	/**
	 * @param homeVersionCommands the homeVersionCommands to set
	 */
	public void setHomeVersionCommands(
			Collection<HomeVersionCommand> homeVersionCommands) {
		HomeVersionCommands = homeVersionCommands;
	}
	public int Id ;
	public String Name ;
	public String Code ;
	public String Description ;
	public Date LaunchDate ;
	public int AuditId ;
	public Audit Audit ;
	public Collection<HomeVersionCommand> HomeVersionCommands ;


}
