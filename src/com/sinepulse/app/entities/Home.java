package com.sinepulse.app.entities;

import java.util.Collection;

/**
 * @author Tanvir
 *
 */

public class Home {
	
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
	 * @return the gUID
	 */
	public String getGUID() {
		return GUID;
	}
	/**
	 * @param gUID the gUID to set
	 */
	public void setGUID(String gUID) {
		GUID = gUID;
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
	 * @return the numberOfRooms
	 */
	public int getNumberOfRooms() {
		return NumberOfRooms;
	}
	/**
	 * @param numberOfRooms the numberOfRooms to set
	 */
	public void setNumberOfRooms(int numberOfRooms) {
		NumberOfRooms = numberOfRooms;
	}
	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return Enabled;
	}
	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		Enabled = enabled;
	}
	/**
	 * @return the status
	 */
	public HomeStatus getStatus() {
		return Status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(HomeStatus status) {
		Status = status;
	}
	/**
	 * @return the zone
	 */
	public String getZone() {
		return Zone;
	}
	/**
	 * @param zone the zone to set
	 */
	public void setZone(String zone) {
		Zone = zone;
	}
	/**
	 * @return the block
	 */
	public String getBlock() {
		return Block;
	}
	/**
	 * @param block the block to set
	 */
	public void setBlock(String block) {
		Block = block;
	}
	/**
	 * @return the timeZone
	 */
	public String getTimeZone() {
		return TimeZone;
	}
	/**
	 * @param timeZone the timeZone to set
	 */
	public void setTimeZone(String timeZone) {
		TimeZone = timeZone;
	}
	/**
	 * @return the registrationKey
	 */
	public String getRegistrationKey() {
		return RegistrationKey;
	}
	/**
	 * @param registrationKey the registrationKey to set
	 */
	public void setRegistrationKey(String registrationKey) {
		RegistrationKey = registrationKey;
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
	 * @return the homeCommands
	 */
	public Collection<HomeCommand> getHomeCommands() {
		return HomeCommands;
	}
	/**
	 * @param homeCommands the homeCommands to set
	 */
	public void setHomeCommands(Collection<HomeCommand> homeCommands) {
		HomeCommands = homeCommands;
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
	public int HomeVersionId ;
	public String Name ;
	public String GUID ;
	public String Comment ;
	public int NumberOfRooms ;
	public boolean Enabled ;
	public HomeStatus Status ;

	public String Zone ;
	public String Block ;
	public String TimeZone ;
	public String RegistrationKey ;
	public String HardwareId ;
	public int AuditId ;
	public Collection<HomeCommand> HomeCommands ;
	public Audit Audit ;
	public HomeVersion HomeVersion ;


}
