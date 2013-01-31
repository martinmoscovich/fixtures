package com.mmoscovich.fixtures.persistence.test;

import com.mmoscovich.fixtures.FixtureLoader;
import com.mmoscovich.fixtures.builder.FixtureLoaderBuilder;
import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.persistence.builder.FixturePersisterBuilder;
import com.mmoscovich.fixtures.test.AnnotationProcessor;

public class PersistenceAnnotationProcessor extends AnnotationProcessor {

	@Override
	public FixtureLoader buildFromConfig(Class<?> clazz) throws FixturesConfigurationException {
		String xml = this.processConfig(clazz);
		return FixturePersisterBuilder.fromXml(xml).build();
	}
}
