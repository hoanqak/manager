package com.manager.data;

public enum Role {
	EMPLOYEE(0),
	ADMIN(1),
	MANAGER(2);

	private int value;
	Role(int value) {
		this.value = value;
	}
}
