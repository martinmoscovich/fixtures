package com.mmoscovich.fixtures.store;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.mmoscovich.fixtures.exception.EntityStoreException;

public class VersionedEntityStoreImpl implements VersionedEntityStore {
	private Map<String, EntityStore> stores = new ConcurrentHashMap<String, EntityStore>();
	
	private void validateVersion(String version) throws EntityStoreException {
		if(!stores.containsKey(version)) throw new EntityStoreException("Invalid store version: " + version);
	}
	
	public void addStore(String version, EntityStore store) {
		stores.put(version, store);
	}
	
	@Override
	public List<Object> getAll(String version) throws EntityStoreException {
		validateVersion(version);
		return stores.get(version).getAll();
	}

	@Override
	public <T> List<T> getEntities(String version, Class<T> clazz) throws EntityStoreException {
		validateVersion(version);
		return stores.get(version).getEntities(clazz);
	}

	@Override
	public <T> List<T> getEntities(String version, Class<T> clazz,	String... names) throws EntityStoreException {
		validateVersion(version);
		return stores.get(version).getEntities(clazz, names);
	}

	@Override
	public <T> T getEntity(String version, Class<T> clazz, String name) throws EntityStoreException {
		validateVersion(version);
		return stores.get(version).getEntity(clazz, name);
	}

	@Override
	public <T> T getAnyEntity(String version, Class<T> clazz) throws EntityStoreException {
		validateVersion(version);
		return stores.get(version).getAnyEntity(clazz);
	}

	@Override
	public <T> List<T> getAnyEntities(String version, Class<T> clazz, int maxNumber) throws EntityStoreException {
		validateVersion(version);
		return stores.get(version).getAnyEntities(clazz, maxNumber);
	}

	@Override
	public List<Class<?>> getEntityClasses() {
		for(EntityStore store : stores.values()) {
			return store.getEntityClasses();
		}
		return Collections.emptyList();
	}

	@Override
	public EntityStore getStore(String version) throws EntityStoreException {
		validateVersion(version);
		return stores.get(version);
	}

	@Override
	public boolean containsVersion(String version) {
		return stores.containsKey(version);
	}

}
