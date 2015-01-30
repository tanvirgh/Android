package com.sinepulse.app.entities;

public class Room {
	public int RowId;//for internal use...
	public int Id ;
	public String Name ;
	public int RoomNumber ;
	public boolean Status ;
	
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
	 * @return the roomNumber
	 */
	public int getRoomNumber() {
		return RoomNumber;
	}
	/**
	 * @param roomNumber the roomNumber to set
	 */
	public void setRoomNumber(int roomNumber) {
		RoomNumber = roomNumber;
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
	
	
}
