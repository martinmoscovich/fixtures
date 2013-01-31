package com.mmoscovich.fixtures.exception;

public class RequiredConfigAttributeMissing extends	FixturesConfigurationException {
	private static final long serialVersionUID = 1L;

	public RequiredConfigAttributeMissing(String attribute) {
		super("The configuration attribute " + attribute + " is missing");
	}

}
