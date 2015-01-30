package com.sinepulse.app.entities;

/**
 * @author Tanvir
 *
 */
public enum DataType {
	
    String(0),Int (1),Float (2), Double (3), Bit (4);
   
    private int value;

	private DataType(int value) {

		this.value = value;

	}

	public int getValue() {
		return this.value;
	}
   

    
    
   
   


}
