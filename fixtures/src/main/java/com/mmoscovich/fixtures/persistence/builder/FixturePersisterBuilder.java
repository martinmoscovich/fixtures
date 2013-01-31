package com.mmoscovich.fixtures.persistence.builder;

import com.mmoscovich.fixtures.builder.FixtureBuilder;
import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.persistence.FixturePersister;
import com.mmoscovich.fixtures.persistence.config.FixturePersisterConfig;
import com.mmoscovich.fixtures.persistence.config.XmlConfiguredFixturePersisterConfig;

public class FixturePersisterBuilder implements FixtureBuilder<FixturePersister> {
	protected FixturePersisterConfig config;
	
	public static FixtureBuilder<FixturePersister> fromXml(String xmlLocation) throws FixturesConfigurationException {
		FixturePersisterBuilder builder = new FixturePersisterBuilder();
		XmlConfiguredFixturePersisterConfig config = new XmlConfiguredFixturePersisterConfig(xmlLocation);
		config.init();
		builder.config = config;
		return builder;
	}
	
	public static ManualFixturePersisterBuilder manually() {
		ManualFixturePersisterBuilder builder = new ManualFixturePersisterBuilder();
		builder.config = new FixturePersisterConfig();
		return builder;
	}
	
	@Override
	public FixturePersister build() {
		return new FixturePersister(config);
	}

}
