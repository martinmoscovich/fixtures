package com.mmoscovich.fixtures.exception;

public class FixturesJUnitRunnerException extends FixturesException {
	private static final long serialVersionUID = 1L;

	public FixturesJUnitRunnerException(Throwable e) {
		super("Internal JUnit Runner exception", e);
	}

}
