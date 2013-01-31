package com.mmoscovich.fixtures.model;

public class AbsoluteRootModel extends Model {
	private String uri;
	
	AbsoluteRootModel(Class<?> clazz, String alias, String uri) {
		super(clazz, alias);
		this.uri = uri;
	}
	
	@Override
	public boolean isRoot() {
		return true;
	}

	@Override
	public String getLocation(String version, String extension) {
		return this.uri;
	}

}
