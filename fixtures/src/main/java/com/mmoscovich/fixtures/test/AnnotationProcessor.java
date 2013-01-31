package com.mmoscovich.fixtures.test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mmoscovich.fixtures.FixtureLoader;
import com.mmoscovich.fixtures.builder.FixtureLoaderBuilder;
import com.mmoscovich.fixtures.config.FixtureLoaderConfig;
import com.mmoscovich.fixtures.exception.EntityStoreException;
import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.exception.FixturesJUnitRunnerException;
import com.mmoscovich.fixtures.exception.FixturesTestException;
import com.mmoscovich.fixtures.test.annotation.FixtureConfig;
import com.mmoscovich.fixtures.test.annotation.FixtureEntity;
import com.mmoscovich.fixtures.test.annotation.FixtureGroup;
import com.mmoscovich.fixtures.test.annotation.FixtureGroups;
import com.mmoscovich.fixtures.test.annotation.FixtureVersion;

public class AnnotationProcessor {
	
	public Map<String, String> processVersion(Class<?> clazz) {
		Map<String, String> result = new HashMap<String, String>();
		FixtureVersion fixtureVersion = clazz.getAnnotation(FixtureVersion.class);
		FixtureGroups groups = clazz.getAnnotation(FixtureGroups.class);
		
		return processGroups(result, fixtureVersion, groups);
	}
	
	private Map<String, String> processGroups(Map<String, String> groupMap, FixtureVersion fixtureVersion, FixtureGroups groups) throws FixturesTestException {
		if(fixtureVersion != null) {
			if(groups != null) throw new FixturesTestException("@FixtureVersion and @FixtureGroups annotations cannot be used in the same class or in the same method");
			groupMap.put(FixtureLoaderConfig.DEFAULT_GROUP_NAME, fixtureVersion.value());
			return groupMap;
		} 
		
		if(groups != null) {
			for(FixtureGroup group : groups.value()) {
				groupMap.put(group.value(), group.version());
			}
		}
		return groupMap;
	}
	
	public FixtureLoader buildFromConfig(Class<?> clazz) throws FixturesConfigurationException {
		String xml = this.processConfig(clazz);
		return FixtureLoaderBuilder.fromXml(xml).build();
	}
	
	public Map<String, String> processVersion(Method method) {
		FixtureVersion fixtureVersion = method.getAnnotation(FixtureVersion.class);
		FixtureGroups groups = method.getAnnotation(FixtureGroups.class);

		Map<String, String> groupMap = processVersion(method.getDeclaringClass());
		
		processGroups(groupMap, fixtureVersion, groups);
	
		if(groupMap.isEmpty()) {
			groupMap.put(FixtureLoaderConfig.DEFAULT_GROUP_NAME, "default");
		}
		return groupMap;
	}
	
	/**
	 * Inject the annotated entities to the test class attributes
	 * @param fields 
	 * @param instance test class instance
	 * @throws FixturesJUnitRunnerException if there's a problem with the reflection logic
	 * @throws EntityStoreException if the version of the fixtures doesn't exist
	 */
	public void injectEntities(Map<Field, FixtureEntity> fields, Object instance) throws FixturesJUnitRunnerException, EntityStoreException {
		for(Entry<Field, FixtureEntity> entry : fields.entrySet()) {
			Field f = entry.getKey();
			FixtureEntity fe = entry.getValue();
			f.setAccessible(true);
			Object value = isGenericList(f)? getList(f, fe) : getValue(f, fe);
			try {
				f.set(instance, value);
			} catch (IllegalArgumentException e) {
				throw new FixturesJUnitRunnerException(e);
			} catch (IllegalAccessException e) {
				throw new FixturesJUnitRunnerException(e);
			}
		}
	}
	
	private boolean isGenericList(Field f) {
		return (f.getType().isAssignableFrom(List.class)  && f.getGenericType() instanceof ParameterizedType);
	}
	
	private List<?> getList(Field f, FixtureEntity fe) throws EntityStoreException {
		// If the type is parametrized, add a config to establish the right class to use
		ParameterizedType t = (ParameterizedType)f.getGenericType();
		Class<?> c = (Class<?>)t.getActualTypeArguments()[0];
		
		if(fe.value().length == 0) {
			return Fixtures.getOptionalAnyEntities(c, fe.maxNumber());
		} else {
			return Fixtures.getOptionalEntities(c, fe.value());
		}
	}
	
	private Object getValue(Field f, FixtureEntity fe) throws EntityStoreException {
		if(fe.value().length == 0) {
			return Fixtures.getOptionalAnyEntity(f.getType());
		} else {
			return Fixtures.getOptionalEntity(f.getType(), fe.value()[0]);
		}
	}
	
	protected String processConfig(Class<?> clazz) {
		FixtureConfig fixtureConfig = clazz.getAnnotation(FixtureConfig.class);
//		classes.put(clazz, (fixtureConfig == null)?"fixtures.xml":fixtureConfig.xmlLocation());
		return (fixtureConfig == null)?"fixtures.xml":fixtureConfig.xmlLocation();
	}
	
	public Map<Field, FixtureEntity> processFields(Class<?> clazz) {
		Map<Field, FixtureEntity> result = new HashMap<Field, FixtureEntity>();
		
		for(Field field : clazz.getDeclaredFields()) {
			FixtureEntity fe = field.getAnnotation(FixtureEntity.class);
			if(fe != null) {
				result.put(field, fe);
			}
		}
		return result;
	}
}
