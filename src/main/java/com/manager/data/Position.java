package com.manager.data;

public enum Position {
	MANAGER(0),
	ADMIN(1),
	DEVELOPER(2),
	INTERNSHIP(3),
	FRESHER_BACKEND(4),
	FRESHER_FRONTEND(5),
	ACCOUNTANT(6),
	BA(7),
	SALE(8),
	TESTER(9);

	private int value;

	Position(int value) {
		this.value = value;
	}
}
