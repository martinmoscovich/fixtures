package com.mmoscovich.fixtures.exception;

public class FixturesConfigurationException extends FixturesException {

	private static final long serialVersionUID = 1L;

	public FixturesConfigurationException(String message, Throwable e) {
		super(message, e);
	}
	
	public FixturesConfigurationException(String message) {
		super(message);
	}

}
