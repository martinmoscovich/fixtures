package com.mmoscovich.fixtures.config;

import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.model.Model;

public interface ModelContainer {

	void addModel(Model model) throws FixturesConfigurationException;

	boolean containsModel(Model model);
	
	boolean containsModel(Class<?> clazz);
	
	Model getModel(Class<?> clazz);
}