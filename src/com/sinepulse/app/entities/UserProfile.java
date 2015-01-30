package com.sinepulse.app.entities;

import java.util.Date;

/**
 * @author Tanvir
 *
 */

public class UserProfile {
	
	public int UserId ;
	public String UserName ;
	public String FirstName ;
	public String MiddleName ;
	public String LastName ;
	public String Sex ;
	public int AddressId;
	public Date DateOfBirth ;
	public String SocialSecurityNumber ;
	public String Email ;
	public String CellPhone ;
	public String AccNo ;
	public String OldAcc ;
	public String LocalID ;
	public int MasterId ;
	public boolean EmailRecipient ;
	public boolean SMSRecipient ;
	public UserStatus Status ;
	public boolean IsLoggedIn ;
	public Date LastLogIn ;
	public Date CreatedAt ;
	public Date UpdatedAt ;
	public Address  address ;
	
//	public List<UserLog> UserLogs ;
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
	 * @return the userName
	 */
	public String getUserName() {
		return UserName;
	}
	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		UserName = userName;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return FirstName;
	}
	/**
	 * @param firstName the firstName to set
	 */
	public void setFirstName(String firstName) {
		FirstName = firstName;
	}
	/**
	 * @return the middleName
	 */
	public String getMiddleName() {
		return MiddleName;
	}
	/**
	 * @param middleName the middleName to set
	 */
	public void setMiddleName(String middleName) {
		MiddleName = middleName;
	}
	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return LastName;
	}
	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		LastName = lastName;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return Sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		Sex = sex;
	}
	/**
	 * @return the addressId
	 */
	public int getAddressId() {
		return AddressId;
	}
	/**
	 * @param addressId the addressId to set
	 */
	public void setAddressId(int addressId) {
		AddressId = addressId;
	}
	/**
	 * @return the dateOfBirth
	 */
	public Date getDateOfBirth() {
		return DateOfBirth;
	}
	/**
	 * @param dateOfBirth the dateOfBirth to set
	 */
	public void setDateOfBirth(Date dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}
	/**
	 * @return the socialSecurityNumber
	 */
	public String getSocialSecurityNumber() {
		return SocialSecurityNumber;
	}
	/**
	 * @param socialSecurityNumber the socialSecurityNumber to set
	 */
	public void setSocialSecurityNumber(String socialSecurityNumber) {
		SocialSecurityNumber = socialSecurityNumber;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return Email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		Email = email;
	}
	/**
	 * @return the cellPhone
	 */
	public String getCellPhone() {
		return CellPhone;
	}
	/**
	 * @param cellPhone the cellPhone to set
	 */
	public void setCellPhone(String cellPhone) {
		CellPhone = cellPhone;
	}
	/**
	 * @return the accNo
	 */
	public String getAccNo() {
		return AccNo;
	}
	/**
	 * @param accNo the accNo to set
	 */
	public void setAccNo(String accNo) {
		AccNo = accNo;
	}
	/**
	 * @return the oldAcc
	 */
	public String getOldAcc() {
		return OldAcc;
	}
	/**
	 * @param oldAcc the oldAcc to set
	 */
	public void setOldAcc(String oldAcc) {
		OldAcc = oldAcc;
	}
	/**
	 * @return the localID
	 */
	public String getLocalID() {
		return LocalID;
	}
	/**
	 * @param localID the localID to set
	 */
	public void setLocalID(String localID) {
		LocalID = localID;
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
	 * @return the emailRecipient
	 */
	public boolean isEmailRecipient() {
		return EmailRecipient;
	}
	/**
	 * @param emailRecipient the emailRecipient to set
	 */
	public void setEmailRecipient(boolean emailRecipient) {
		EmailRecipient = emailRecipient;
	}
	/**
	 * @return the sMSRecipient
	 */
	public boolean isSMSRecipient() {
		return SMSRecipient;
	}
	/**
	 * @param sMSRecipient the sMSRecipient to set
	 */
	public void setSMSRecipient(boolean sMSRecipient) {
		SMSRecipient = sMSRecipient;
	}
	/**
	 * @return the status
	 */
	public UserStatus getStatus() {
		return Status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(UserStatus status) {
		Status = status;
	}
	/**
	 * @return the isLoggedIn
	 */
	public boolean isIsLoggedIn() {
		return IsLoggedIn;
	}
	/**
	 * @param isLoggedIn the isLoggedIn to set
	 */
	public void setIsLoggedIn(boolean isLoggedIn) {
		IsLoggedIn = isLoggedIn;
	}
	/**
	 * @return the lastLogIn
	 */
	public Date getLastLogIn() {
		return LastLogIn;
	}
	/**
	 * @param lastLogIn the lastLogIn to set
	 */
	public void setLastLogIn(Date lastLogIn) {
		LastLogIn = lastLogIn;
	}
	/**
	 * @return the createdAt
	 */
	public Date getCreatedAt() {
		return CreatedAt;
	}
	/**
	 * @param createdAt the createdAt to set
	 */
	public void setCreatedAt(Date createdAt) {
		CreatedAt = createdAt;
	}
	/**
	 * @return the updatedAt
	 */
	public Date getUpdatedAt() {
		return UpdatedAt;
	}
	/**
	 * @param updatedAt the updatedAt to set
	 */
	public void setUpdatedAt(Date updatedAt) {
		UpdatedAt = updatedAt;
	}
	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}
	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}


}
