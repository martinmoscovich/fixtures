package com.mmoscovich.fixtures.model;


public abstract class Model {
	private Class<?> clazz;
	private String alias;
	private Group group;
	
	public abstract boolean isRoot();
	public abstract String getLocation(String version, String extension);

	public void setGroup(Group group) {
		this.group = group;
	}
	public Group getGroup() {
		return group;
	}
	
	public String toString() {
		return "Model of class " + clazz.getSimpleName() + " and " + group;
	}
	
	public Class<?> getClazz() {
		return clazz;
	}
	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	protected Model(Class<?> clazz, String alias) {
		this.clazz = clazz;
		this.alias = alias;
	}
	
	public static Model buildRootRelative(Class<?> clazz, String alias, String baseUri) {
		return new RelativeRootModel(clazz, alias, baseUri);
	}
	public static Model buildRootRelative(Class<?> clazz, String baseUri) {
		return new RelativeRootModel(clazz, null, baseUri);
	}
	
	public static Model buildRootAbsolute(Class<?> clazz, String alias, String uri) {
		return new AbsoluteRootModel(clazz, alias, uri);
	}
	public static Model buildRootAbsolute(Class<?> clazz, String uri) {
		return new AbsoluteRootModel(clazz, null, uri);
	}
	
	public static Model buildInline(Class<?> clazz, String alias) {
		return new InlineModel(clazz, alias);
	}
	
	public static Model buildInline(Class<?> clazz) {
		return new InlineModel(clazz, null);
	}
		
	public boolean hasAlias() {
		return (alias != null);
	}
}