package com.mmoscovich.fixtures.persistence.config;

import org.jdom2.Element;

import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.exception.RequiredConfigAttributeMissing;

public interface EntityPersisterXmlConfigurationParser {
	EntityPersisterConfig buildConfig(Element root) throws RequiredConfigAttributeMissing, FixturesConfigurationException;
}
