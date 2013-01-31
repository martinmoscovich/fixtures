package com.mmoscovich.fixtures.test;

import org.junit.runner.Runner;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;

import com.mmoscovich.fixtures.exception.FixturesConfigurationException;


/**
 * Fixtures Junit {@link Runner}
 * @author Martin.Moscovich
 *
 */
public class FixturesJUnitRunner extends BlockJUnit4ClassRunner {
	FixturesJUnitRunnerHelper helper = new FixturesJUnitRunnerHelper();
	
	public FixturesJUnitRunner(Class<?> klass) throws InitializationError, FixturesConfigurationException {
		super(klass);
		helper.initRunner(klass);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected Statement withBefores(FrameworkMethod frameworkMethod, Object testInstance, Statement statement) {
		helper.beforeTestMethod(testInstance, frameworkMethod);
		return super.withBefores(frameworkMethod, testInstance, statement);
	}
	
}
