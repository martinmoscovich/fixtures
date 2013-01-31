package com.mmoscovich.fixtures.builder;

import com.mmoscovich.fixtures.EntityReader;
import com.mmoscovich.fixtures.config.FixtureLoaderConfig;
import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.model.Model;

public class FixtureManualRelativeBuilder extends FixtureLoaderBuilder {
	private String baseUri;
	
	public FixtureManualRelativeBuilder(String baseUri) {
		this.config = new FixtureLoaderConfig();
		this.baseUri = baseUri;
	}
	
	public FixtureManualRelativeBuilder addRootModel(Class<?> clazz) throws FixturesConfigurationException {
		config.addModel(Model.buildRootRelative(clazz, this.baseUri));
		return this;
	}
	
	public FixtureManualRelativeBuilder addRootModel(Class<?> clazz, String alias) throws FixturesConfigurationException {
		config.addModel(Model.buildRootRelative(clazz, alias, this.baseUri));
		return this;
	}
	
	public FixtureManualRelativeBuilder addInlineModel(Class<?> clazz) throws FixturesConfigurationException {
		config.addModel(Model.buildInline(clazz));
		return this;
	}
	
	public FixtureManualRelativeBuilder addInlineModel(Class<?> clazz, String alias) throws FixturesConfigurationException{
		config.addModel(Model.buildInline(clazz, alias));
		return this;
	}
	
	public FixtureManualRelativeBuilder setBaseUri(String uri) {
		this.baseUri = uri;
		return this;
	}
	
	public FixtureManualRelativeBuilder setReader(EntityReader reader){
		config.setReader(reader);
		return this;
	}
	
}
