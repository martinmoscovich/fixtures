package com.mmoscovich.fixtures.test;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mmoscovich.fixtures.exception.FixturesConfigurationException;

public class FixturesSpringJunitRunner extends SpringJUnit4ClassRunner {
	
	FixturesJUnitRunnerHelper helper;
	
	public FixturesSpringJunitRunner(Class<?> clazz) throws InitializationError, FixturesConfigurationException {
		super(clazz);
		this.helper = new FixturesJUnitRunnerHelper();
		helper.initRunner(clazz);
	}
	
	@Override
	protected Statement withBefores(FrameworkMethod frameworkMethod, Object testInstance, Statement statement) {
		helper.beforeTestMethod(testInstance, frameworkMethod);
		return super.withBefores(frameworkMethod, testInstance, statement);
	}
}
