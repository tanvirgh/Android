/**
 * 
 */
package com.sinepulse.app.entities;

/**
 * @author Tanvir
 *
 */
public enum PropertyUnit {
	None (0),Percent(1),Factor(2),RPM(3);
	
	 private int value;

		private PropertyUnit(int value) {

			this.value = value;

		}

		public int getValue() {
			return this.value;
		}

}
