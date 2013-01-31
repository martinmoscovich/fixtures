package com.mmoscovich.fixtures;

import com.mmoscovich.fixtures.config.FixtureLoaderConfig;
import com.mmoscovich.fixtures.exception.EntityReaderException;
import com.mmoscovich.fixtures.exception.FixturesException;
import com.mmoscovich.fixtures.model.Group;
import com.mmoscovich.fixtures.store.EntityStore;

public class FixtureLoader {
	private FixtureLoaderConfig config;
	
	public FixtureLoader(FixtureLoaderConfig config) {
		this.config = config;
	}
	
	public FixtureLoaderConfig getConfig() {
		return this.config;
	}
	
	public EntityStore load() throws FixturesException {
		return this.load("default");
	}
	public EntityStore load(String version) throws FixturesException {
		return this.loadGroup(FixtureLoaderConfig.DEFAULT_GROUP_NAME, version);
	}
	
	public EntityStore loadGroup(String groupName) throws FixturesException {
		return this.loadGroup(groupName, "default");
	}
	
	public EntityStore loadGroup(String groupName, String version) throws FixturesException {
		if(config.getReader() == null) throw new EntityReaderException("No reader was configured"); 
		Group group = config.getGroup(groupName);
		if(group == null) throw new EntityReaderException("The group " + groupName + " does not exist");
		return config.getReader().load(group.getModels(), version);
	}
}