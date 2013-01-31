package com.mmoscovich.fixtures.exception;

public class EntityStoreException extends FixturesException {
	private static final long serialVersionUID = 1L;

	public EntityStoreException(String message) {
		super(message);
	}
	
	public EntityStoreException(String message, Throwable e) {
		super(message,e);
	}
}
