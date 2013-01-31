package com.mmoscovich.fixtures.exception;

public class FixturesException extends Exception {
	private static final long serialVersionUID = 1L;

	public FixturesException(String message) {
		super(message);
	}
	
	public FixturesException(String message, Throwable e) {
		super(message,e);
	}
}
