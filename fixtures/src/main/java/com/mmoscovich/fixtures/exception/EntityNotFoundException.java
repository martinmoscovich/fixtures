package com.mmoscovich.fixtures.exception;

public class EntityNotFoundException extends FixturesTestException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String message) {
		super(message);
	}
	
	public EntityNotFoundException(Class<?> clazz, String entityName, String version) {
		super("No entity named " + entityName + " of class " + clazz.getSimpleName() + " was found (fixtures version: " + version + ")");
	}
	public EntityNotFoundException(Class<?> clazz, String[] entityNames, String version) {
		super("Some of the entities with names (" + entityNames.toString() + ") of class " + clazz.getSimpleName() + " were not found (fixtures version: " + version + ")");
	}
	
	public EntityNotFoundException(Class<?> clazz, String version) {
		super("No entity of class " + clazz.getSimpleName() + " was found (fixtures version: " + version + ")");
	}
	public EntityNotFoundException(Class<?> clazz) {
		super("No version was loaded for model of class " + clazz.getSimpleName());
	}

}
