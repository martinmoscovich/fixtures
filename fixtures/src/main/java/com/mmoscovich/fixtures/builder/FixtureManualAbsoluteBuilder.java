package com.mmoscovich.fixtures.builder;

import com.mmoscovich.fixtures.EntityReader;
import com.mmoscovich.fixtures.config.FixtureLoaderConfig;
import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.model.Model;

public class FixtureManualAbsoluteBuilder extends FixtureLoaderBuilder {
	
	public FixtureManualAbsoluteBuilder() {
		this.config = new FixtureLoaderConfig();
	}
	
	public FixtureManualAbsoluteBuilder addRootModel(Class<?> clazz, String uri) throws FixturesConfigurationException {
		config.addModel(Model.buildRootAbsolute(clazz, uri));
		return this;
	}
	
	public FixtureManualAbsoluteBuilder addRootModel(Class<?> clazz, String uri, String alias) throws FixturesConfigurationException {
		config.addModel(Model.buildRootAbsolute(clazz, alias, uri));
		return this;
	}
	
	public FixtureManualAbsoluteBuilder addInlineModel(Class<?> clazz) throws FixturesConfigurationException {
		config.addModel(Model.buildInline(clazz));
		return this;
	}
	
	public FixtureManualAbsoluteBuilder addInlineModel(Class<?> clazz, String alias) throws FixturesConfigurationException {
		config.addModel(Model.buildInline(clazz, alias));
		return this;
	}
	
	public FixtureManualAbsoluteBuilder setReader(EntityReader reader) {
		config.setReader(reader);
		return this;
	}
}
