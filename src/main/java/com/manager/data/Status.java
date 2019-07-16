package com.manager.data;

public enum Status {
	ACTIVE(0),
	BLOCK(1);

	private int value;

	Status(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
