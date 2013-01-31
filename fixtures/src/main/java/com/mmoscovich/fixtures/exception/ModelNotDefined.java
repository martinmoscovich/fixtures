package com.mmoscovich.fixtures.exception;

public class ModelNotDefined extends EntityNotFoundException {
	private static final long serialVersionUID = 1L;

	public ModelNotDefined(Class<?> clazz) {
		super("No model was defined for class " + clazz.getSimpleName() + " in the configuration");
	}
}
