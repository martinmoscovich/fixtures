package com.mmoscovich.fixtures.exception;

public class EntityReaderException extends FixturesException {
	private static final long serialVersionUID = 1L;

	public EntityReaderException(Throwable e) {
		super("Unable to load fixtures", e);
	}
	
	public EntityReaderException(String message) {
		super("Unable to load fixtures. " + message);
	}



}
