package com.mmoscovich.fixtures.model;

public class InlineModel extends Model {

	InlineModel(Class<?> clazz, String alias) {
		super(clazz, alias);
	}
	
	@Override
	public boolean isRoot() {
		return false;
	}

	@Override
	public String getLocation(String version, String extension) {
		return null;
	}

}
