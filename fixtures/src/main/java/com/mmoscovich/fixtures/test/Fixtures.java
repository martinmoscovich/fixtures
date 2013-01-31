package com.mmoscovich.fixtures.test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mmoscovich.fixtures.FixtureLoader;
import com.mmoscovich.fixtures.config.FixtureLoaderConfig;
import com.mmoscovich.fixtures.exception.EntityNotFoundException;
import com.mmoscovich.fixtures.exception.EntityReaderException;
import com.mmoscovich.fixtures.exception.EntityStoreException;
import com.mmoscovich.fixtures.exception.FixturesException;
import com.mmoscovich.fixtures.exception.FixturesTestException;
import com.mmoscovich.fixtures.exception.FixturesVersionNotFound;
import com.mmoscovich.fixtures.exception.ModelNotDefined;
import com.mmoscovich.fixtures.model.Model;
import com.mmoscovich.fixtures.store.EntityStore;
import com.mmoscovich.fixtures.store.VersionedEntityGroupStore;

public class Fixtures {
	private static VersionedEntityGroupStore store = new VersionedEntityGroupStore();
	private static ThreadLocal<Map<String, String>> currentVersions = new ThreadLocal<Map<String, String>>(); 
	private static ThreadLocal<FixtureLoaderConfig> currentConfig = new ThreadLocal<FixtureLoaderConfig>();
	
	/**
	 * Loads the entities from the fixtures using the provided version.
	 * The version is set as "current version" and used when calling any getEntity* static method
	 * @param loader loader instance to use to load the entities
	 * @param groupVersions version of the entities' groups to be used
	 * @throws EntityReaderException if there's a problem reading the fixtures
	 */
	public static void initFixtures(FixtureLoader loader, Map<String, String> groupVersions) throws FixturesException {
		FixtureLoaderConfig config = loader.getConfig();
		for(Entry<String, String> entry : groupVersions.entrySet()) {
			if(!store.containsGroupAndVersion(entry.getKey(), entry.getValue())) {
				EntityStore entityStore = loader.loadGroup(entry.getKey(), entry.getValue());
				store.addStore(config.getGroup(entry.getKey()), entry.getValue(), entityStore);
			}
		}
		currentConfig.set(config);
		currentVersions.set(groupVersions);
	}
	
	public static void initFixtures(FixtureLoader loader, String version) throws FixturesException {
		 Map<String, String> groupVersions = new HashMap<String, String>();
		 groupVersions.put(FixtureLoaderConfig.DEFAULT_GROUP_NAME, "default");
		 initFixtures(loader, groupVersions);
	}
	
	private static Model getModel(Class<?> clazz) throws ModelNotDefined {
		Model model = currentConfig.get().getModel(clazz);
		if(model == null) throw new ModelNotDefined(clazz);
		return model;
	}
	
	private static String getVersion(Class<?> clazz) throws EntityNotFoundException {
		Model model = getModel(clazz);
		String version = currentVersions.get().get(model.getGroup().getName());
		if(version == null) throw new FixturesVersionNotFound(model);
		return version;
	}
	
	/**
	 * Retrieves an entity of the provided class using the passed name.
	 * @param clazz
	 * @param name
	 * @throws EntityNotFoundException if the entity is not found
	 * @throws FixturesTestVersion if the version is not found
	 * @return
	 */
	public static <T> T getEntity(Class<T> clazz, String name) {
		try {
			String version = getVersion(clazz);
			T entity = store.getEntity(version, clazz, name);
		if(entity == null) throw new EntityNotFoundException(clazz, name, version);
		return entity;
		} catch (EntityStoreException e) {
			throw new FixturesTestException(e);
		}
	}
	static <T> T getOptionalEntity(Class<T> clazz, String name) throws EntityStoreException {
		try {
			return store.getEntity(getVersion(clazz), clazz, name);
		} catch(EntityNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * Retrieves a list of entities of the provided class using the passed names.
	 * @param clazz
	 * @param name
	 * @throws EntityNotFoundException if any of the entities is not found
	 * @throws FixturesTestVersion if the version is not found
	 * @return
	 */
	public static <T> List<T> getEntities(Class<T> clazz, String... names) {
		try {
			String version = getVersion(clazz);
			List<T> result = store.getEntities(version, clazz, names);
			if(result.size() < names.length) throw new EntityNotFoundException(clazz, names, version);
			return result;
		} catch (EntityStoreException e) {
			throw new FixturesTestException(e);
		}
	}
	static <T> List<T> getOptionalEntities(Class<T> clazz, String... names) throws EntityStoreException {
		try {
			return store.getEntities(getVersion(clazz), clazz, names);
		} catch(EntityNotFoundException e) {
			return Collections.emptyList();
		}
	}

	/**
	 * Retrieves any entity of the provided class.
	 * @param clazz
	 * @param name
	 * @throws EntityNotFoundException if no entity is found for that class
	 * @throws FixturesTestVersion if the version is not found
	 * @return
	 */
	public static <T> T getAnyEntity(Class<T> clazz) {
		try {
			String version = getVersion(clazz);
			T entity = store.getAnyEntity(version, clazz);
			if(entity == null) throw new EntityNotFoundException(clazz, version);
			return entity;
		} catch (EntityStoreException e) {
			throw new FixturesTestException(e);
		}
	}
	static <T> T getOptionalAnyEntity(Class<T> clazz) throws EntityStoreException {
		try {
			return store.getAnyEntity(getVersion(clazz), clazz);
		} catch(EntityNotFoundException e) {
			return null;
		}
	}
	
	/**
	 * Retrieves a list of entities of the provided class.
	 * @param clazz
	 * @param name
	 * @throws EntityNotFoundException if no entity is found for that class
	 * @throws FixturesTestVersion if the version is not found
	 * @return
	 */
	public static <T> List<T> getAnyEntities(Class<T> clazz, int maxNumber) {
		try {
			String version = getVersion(clazz);
			List<T> result = store.getAnyEntities(version, clazz, maxNumber);
			if(result.isEmpty()) throw new EntityNotFoundException(clazz, version);
			return result;
		} catch (EntityStoreException e) {
			throw new FixturesTestException(e);
		}
	}
	static <T> List<T> getOptionalAnyEntities(Class<T> clazz, int maxNumber) throws EntityStoreException {
		try {
			return store.getAnyEntities(getVersion(clazz), clazz, maxNumber);
		} catch(EntityNotFoundException e) {
			return Collections.emptyList();
		}
	}
}
