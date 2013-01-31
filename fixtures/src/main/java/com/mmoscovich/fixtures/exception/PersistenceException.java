package com.mmoscovich.fixtures.exception;

/**
 * An exception when persisting entities
 * @author Martin.Moscovich
 *
 */
public class PersistenceException extends FixturesException {
	private static final long serialVersionUID = 1L;

	public PersistenceException(String message) {
		super(message);
	}
	
	public PersistenceException(String message, Throwable e) {
		super(message,e);
	}
}
