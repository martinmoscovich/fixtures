package com.mmoscovich.fixtures.exception;

import com.mmoscovich.fixtures.model.Group;
import com.mmoscovich.fixtures.model.Model;


public class FixturesVersionNotFound extends EntityNotFoundException {
	private static final long serialVersionUID = 1L;

	public FixturesVersionNotFound(String version) {
		super("Version " + version + " was not loaded");
	}
	
	public FixturesVersionNotFound(Group group) {
		super("No version was loaded for " + group);
	}
	
	public FixturesVersionNotFound(Model model) {
		super("No version was loaded for " + model);
	}
}
