package com.mmoscovich.fixtures.test;

import java.lang.reflect.Field;
import java.util.Map;

import org.junit.runner.Runner;
import org.junit.runners.model.FrameworkMethod;

import com.mmoscovich.fixtures.FixtureLoader;
import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.exception.FixturesException;
import com.mmoscovich.fixtures.exception.FixturesTestException;
import com.mmoscovich.fixtures.test.annotation.FixtureEntity;

/**
 * Class that provides the required logic to add Fixtures capabilities to any JUnit {@link Runner}
 * @author Martin.Moscovich
 *
 */
public class FixturesJUnitRunnerHelper {
	private Map<Field, FixtureEntity> fields;
	private FixtureLoader loader;
	private AnnotationProcessor processor = new AnnotationProcessor();
	
	public FixturesJUnitRunnerHelper() {
		this(new AnnotationProcessor());
	}
	public FixturesJUnitRunnerHelper(AnnotationProcessor processor) {
		this.processor = processor;
	}
	
	/**
	 * Reads and caches fixtures configuration from the class annotations
	 * @param klass test class
	 * @throws FixturesConfigurationException if there's a problem loading the configuration
	 */
	public void initRunner(Class<?> klass) throws FixturesConfigurationException {
		this.fields = processor.processFields(klass);
		this.loader = processor.buildFromConfig(klass);
	}
	
	/**
	 * Loads the entities and version before each test method is executed
	 * @param testInstance
	 * @param frameworkMethod
	 */
	public void beforeTestMethod(Object testInstance, FrameworkMethod frameworkMethod) {
		try {
			Map<String, String> groups = processor.processVersion(frameworkMethod.getMethod());
			Fixtures.initFixtures(loader, groups);
			
			processor.injectEntities(fields, testInstance);
		} catch (FixturesException e) {
			throw new FixturesTestException(e);
		}		
	}
}
