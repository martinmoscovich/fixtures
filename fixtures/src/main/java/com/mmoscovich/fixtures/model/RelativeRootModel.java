package com.mmoscovich.fixtures.model;

public class RelativeRootModel extends Model {
	private String baseUri;
	
	RelativeRootModel(Class<?> clazz, String alias, String baseUri) {
		super(clazz, alias);
		this.baseUri = baseUri;
	}
	
	@Override
	public boolean isRoot() {
		return true;
	}

	@Override
	public String getLocation(String version, String extension) {
		return this.baseUri + "/" + version + "/" + getClazz().getSimpleName().toLowerCase() + "." + extension;
	}

}
