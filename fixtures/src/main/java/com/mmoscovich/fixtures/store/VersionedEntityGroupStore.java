package com.mmoscovich.fixtures.store;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmoscovich.fixtures.exception.EntityStoreException;
import com.mmoscovich.fixtures.model.Group;
import com.mmoscovich.fixtures.model.Model;

public class VersionedEntityGroupStore implements VersionedEntityStore {
	private static final Logger log = LoggerFactory.getLogger(VersionedEntityGroupStore.class);
	
	private Map<String, VersionedEntityStoreImpl> stores = new ConcurrentHashMap<String, VersionedEntityStoreImpl>();
	private Map<Class<?>, String> groups = new ConcurrentHashMap<Class<?>, String>();
	
	public boolean containsGroup(String name) {
		return stores.containsKey(name);
	}
	
	public boolean containsGroupAndVersion(String groupName, String version) {
		VersionedEntityStore versionedStore = stores.get(groupName);
		return (versionedStore != null && versionedStore.containsVersion(version));
	}
	
	public void addStore(Group group, String version, EntityStore store) {
		log.info("Adding entities for group {} and version {}", group.getName(), version);
		VersionedEntityStoreImpl versionedStore = stores.get(group.getName());
		if(versionedStore == null) {
			versionedStore = new VersionedEntityStoreImpl();
			stores.put(group.getName(), versionedStore);
			this.addGroup(group);
		}
		if(!versionedStore.containsVersion(version)) versionedStore.addStore(version, store);
	}
	
	private void addGroup(Group group) {
		for(Model model : group.getModels()) {
			this.groups.put(model.getClazz(), group.getName());
		}
	}
	
	private VersionedEntityStore getVersionedStore(Class<?> clazz) throws EntityStoreException {
		String groupName = this.groups.get(clazz);
		if(groupName == null) throw new EntityStoreException("No model for class " + clazz.getSimpleName() + " was loaded");
		return this.stores.get(groupName);
	}
	
	public EntityStore getStore(String version) {
		List<EntityStore> result = new ArrayList<EntityStore>();
		
		for(VersionedEntityStore versionedStore : stores.values()) {
			try {
				EntityStore store = versionedStore.getStore(version);
				result.add(store);
			} catch (EntityStoreException e) {
				// Version not found on group, ignore and continue
			}
		}
		return new MultiEntityStore(result);
	}

	@Override
	public List<Object> getAll(String version) throws EntityStoreException {
		List<Object> result = new ArrayList<Object>();
		
		for(VersionedEntityStore store : stores.values()) {
			if(store.containsVersion(version)) {
				result.addAll(store.getAll(version));
			}
		}
		return result;
	}

	@Override
	public <T> List<T> getEntities(String version, Class<T> clazz) throws EntityStoreException {
		VersionedEntityStore store = getVersionedStore(clazz);
		
		return store.getEntities(version, clazz);
	}

	@Override
	public <T> List<T> getEntities(String version, Class<T> clazz, String... names) throws EntityStoreException {
		VersionedEntityStore store = getVersionedStore(clazz);
		
		return store.getEntities(version, clazz, names);
	}

	@Override
	public <T> T getEntity(String version, Class<T> clazz, String name) throws EntityStoreException {
		VersionedEntityStore store = getVersionedStore(clazz);
		return store.getEntity(version, clazz, name);
	}

	@Override
	public <T> T getAnyEntity(String version, Class<T> clazz) throws EntityStoreException {
		VersionedEntityStore store = getVersionedStore(clazz);
		return store.getAnyEntity(version, clazz);
	}

	@Override
	public <T> List<T> getAnyEntities(String version, Class<T> clazz, int maxNumber) throws EntityStoreException {
		VersionedEntityStore store = getVersionedStore(clazz);
		return store.getAnyEntities(version, clazz, maxNumber);
	}

	@Override
	public List<Class<?>> getEntityClasses() {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(VersionedEntityStore store : stores.values()) {
			result.addAll(store.getEntityClasses());
		}
		return result;
	}
	
	@Override
	public boolean containsVersion(String version) {
		for(VersionedEntityStore store : stores.values()) {
			if(store.containsVersion(version)) return true;
		}
		return false;
	}
}
