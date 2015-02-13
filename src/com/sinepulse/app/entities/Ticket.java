/**
 * 
 */
package com.sinepulse.app.entities;

import java.util.Date;

/**
 * @author tanvir.ahmed
 *
 */
public class Ticket {
	
	public int Id ;
	public String Details ;
	public String Status ;
	public String Subject ;
	public Date SubmissionDate ;
	public int TicketTypeId ;
	
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
	 * @return the details
	 */
	public String getDetails() {
		return Details;
	}
	/**
	 * @param details the details to set
	 */
	public void setDetails(String details) {
		Details = details;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return Status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		Status = status;
	}
	/**
	 * @return the subject
	 */
	public String getSubject() {
		return Subject;
	}
	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		Subject = subject;
	}
	/**
	 * @return the submissionDate
	 */
	public Date getSubmissionDate() {
		return SubmissionDate;
	}
	/**
	 * @param submissionDate the submissionDate to set
	 */
	public void setSubmissionDate(Date submissionDate) {
		SubmissionDate = submissionDate;
	}
	/**
	 * @return the ticketTypeId
	 */
	public int getTicketTypeId() {
		return TicketTypeId;
	}
	/**
	 * @param ticketTypeId the ticketTypeId to set
	 */
	public void setTicketTypeId(int ticketTypeId) {
		TicketTypeId = ticketTypeId;
	}
	


}
