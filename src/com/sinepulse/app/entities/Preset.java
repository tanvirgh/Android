/**
 * 
 */
package com.sinepulse.app.entities;

/**
 * @author tanvir.ahmed
 *
 */
public class Preset {
	
			public int Id ;
			public String Name ;
			public String DisplayName ;
			public String Description ;
			public int Value ;

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
	 * @return the value
	 */
	public int getValue() {
		return Value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		Value = value;
	}
		

}
