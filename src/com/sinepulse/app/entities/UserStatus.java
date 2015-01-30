package com.sinepulse.app.entities;

/**
 * @author Tanvir
 *
 */

public enum UserStatus {
	OK(0), Disabled(1), Blocked(2), Deleted(3);

	private int value;

	private UserStatus(int value) {

		this.value = value;

	}

	public int getValue() {
		return this.value;
	}

}
