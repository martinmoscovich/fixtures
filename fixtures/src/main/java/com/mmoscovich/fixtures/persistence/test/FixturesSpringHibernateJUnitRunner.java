package com.mmoscovich.fixtures.persistence.test;

import org.junit.runners.model.InitializationError;

import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.test.FixturesSpringJunitRunner;

public class FixturesSpringHibernateJUnitRunner extends
		FixturesSpringJunitRunner {

	public FixturesSpringHibernateJUnitRunner(Class<?> clazz)
			throws InitializationError, FixturesConfigurationException {
		super(clazz);
		// TODO Auto-generated constructor stub
	}

}
