package com.mmoscovich.fixtures.exception;

public class FixturesTestException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FixturesTestException(FixturesException cause) {
		super(cause);
	}
	public FixturesTestException(String message) {
		super(message);
	}
}
