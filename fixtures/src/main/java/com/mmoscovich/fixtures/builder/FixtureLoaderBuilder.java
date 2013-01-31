package com.mmoscovich.fixtures.builder;

import com.mmoscovich.fixtures.FixtureLoader;
import com.mmoscovich.fixtures.config.FixtureLoaderConfig;
import com.mmoscovich.fixtures.config.XmlConfiguredFixtureLoaderConfig;
import com.mmoscovich.fixtures.exception.FixturesConfigurationException;

public class FixtureLoaderBuilder implements FixtureBuilder<FixtureLoader> {
	protected FixtureLoaderConfig config;
	
	public static FixtureBuilder<FixtureLoader> fromXml(String xmlLocation) throws FixturesConfigurationException {
		FixtureLoaderBuilder builder = new FixtureLoaderBuilder();
		XmlConfiguredFixtureLoaderConfig config = new XmlConfiguredFixtureLoaderConfig(xmlLocation);
		config.init();
		builder.config = config;
		return builder;
	}
	
	public static FixtureManualAbsoluteBuilder fromAbsoluteLocation() {
		return new FixtureManualAbsoluteBuilder();
	}
	
	public static FixtureManualRelativeBuilder fromRelativeLocation(String baseUri) {
		return new FixtureManualRelativeBuilder(baseUri);
	}
	
	@Override
	public FixtureLoader build() {
		return new FixtureLoader(config);
	}
	
}
