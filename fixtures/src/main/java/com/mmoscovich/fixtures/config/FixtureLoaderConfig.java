package com.mmoscovich.fixtures.config;

import java.util.HashMap;
import java.util.Map;

import com.mmoscovich.fixtures.EntityReader;
import com.mmoscovich.fixtures.exception.EntityReaderException;
import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.model.Group;
import com.mmoscovich.fixtures.model.Model;

public class FixtureLoaderConfig implements ModelContainer {
	public static final String DEFAULT_GROUP_NAME = "all";
	
	private Map<String, Group> groups = new HashMap<String, Group>();
	private EntityReader reader;
	
	public Group getGroup(String name) throws EntityReaderException {
		Group group = groups.get(name);
		if(group == null) throw new EntityReaderException("The group " + name + " does not exist");
		return group;
	}
	
	public Model getModel(Class<?> clazz) {
		for(Group group : this.groups.values()) {
			Model model = group.getModel(clazz);
			if(model != null) return model;
		}
		return null;
	}
	
	public EntityReader getReader() {
		return reader;
	}
	public void setReader(EntityReader reader) {
		this.reader = reader;
	}
	
	public void addGroup(Group group) throws FixturesConfigurationException {
		validateGroup(group);
		this.groups.put(group.getName(), group);
	}
	
	private void validateGroup(Group group) throws FixturesConfigurationException {
		if(DEFAULT_GROUP_NAME.equals(group.getName())) throw new FixturesConfigurationException("Group name " + DEFAULT_GROUP_NAME + " is reserved. Use a different name");

		if(groups.containsKey(group.getName())) {
			throw new FixturesConfigurationException("The group " + group.getName() + " is defined more than once");
		}
		
		for(Model model : group.getModels()) {
			if(this.containsModel(model.getClazz())) throw new FixturesConfigurationException("More than one model for class " + model.getClazz().getSimpleName() + " was defined");
		}
	}
	
	/* (non-Javadoc)
	 * @see com.mmoscovich.fixtures.config.ModelContainer#addModel(com.mmoscovich.fixtures.model.Model)
	 */
	@Override
	public void addModel(Model model) throws FixturesConfigurationException {
		if(this.containsModel(model.getClazz())) throw new FixturesConfigurationException("More than one model for class " + model.getClazz().getSimpleName() + " was defined");
		Group all = (groups.containsKey(DEFAULT_GROUP_NAME)? groups.get(DEFAULT_GROUP_NAME): new Group(DEFAULT_GROUP_NAME));
		groups.put(DEFAULT_GROUP_NAME, all);
		all.addModel(model);
	}

	@Override
	public boolean containsModel(Model model) {
		for(Group group : this.groups.values()) {
			if(group.containsModel(model)) return true;
		}
		return false;
	}

	@Override
	public boolean containsModel(Class<?> clazz) {
		for(Group group : this.groups.values()) {
			if(group.containsModel(clazz)) return true;
		}
		return false;
	} 
	
}
