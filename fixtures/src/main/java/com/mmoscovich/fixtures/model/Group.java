package com.mmoscovich.fixtures.model;

import java.util.ArrayList;
import java.util.Collection;

import com.mmoscovich.fixtures.config.ModelContainer;
import com.mmoscovich.fixtures.exception.FixturesConfigurationException;

/**
 * Group of models.
 * Two groups are equal if they have the same name.
 * @author Martin.Moscovich
 *
 */
public class Group implements ModelContainer {
	private final Collection<Model> models = new ArrayList<Model>();
	private final String name;
	
	public Group(String name) {
		this.name = name;
	}
	
	public void addModel(Model model) throws FixturesConfigurationException {
		if(this.containsModel(model.getClazz())) throw new FixturesConfigurationException("More than one model for class " + model.getClazz().getSimpleName() + " was defined");
			
		model.setGroup(this);
		this.models.add(model);
	}
	
	public Collection<Model> getModels() {
		return models;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public String toString() {
		return "Group '" + name + "'";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Group other = (Group) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public boolean containsModel(Model model) {
		return this.models.contains(model);
	}

	@Override
	public boolean containsModel(Class<?> clazz) {
		for(Model model : models) {
			if(model.getClazz().equals(clazz)) return true;
		}
		return false;
	}

	@Override
	public Model getModel(Class<?> clazz) {
		for(Model model : models) {
			if(model.getClazz().equals(clazz)) return model;
		}
		return null;
	}
	
	
}
