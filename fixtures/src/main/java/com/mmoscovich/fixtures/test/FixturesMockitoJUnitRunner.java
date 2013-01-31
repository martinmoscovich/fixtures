package com.mmoscovich.fixtures.test;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.runners.util.FrameworkUsageValidator;

import com.mmoscovich.fixtures.exception.FixturesConfigurationException;

public class FixturesMockitoJUnitRunner extends BlockJUnit4ClassRunner {
	FixturesJUnitRunnerHelper helper = new FixturesJUnitRunnerHelper();

	public FixturesMockitoJUnitRunner(Class<?> klass) throws FixturesConfigurationException, InitializationError {
		super(klass);
		helper.initRunner(klass);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	protected Statement withBefores(FrameworkMethod frameworkMethod, Object testInstance, Statement statement) {
		MockitoAnnotations.initMocks(testInstance);
		helper.beforeTestMethod(testInstance, frameworkMethod);
		
		return super.withBefores(frameworkMethod, testInstance, statement);
	}
	
	@Override
	public void run(final RunNotifier notifier) {
		// add listener that validates mockito framework usage at the end of each test
        notifier.addListener(new FrameworkUsageValidator(notifier));
        
		super.run(notifier);
	}

}
