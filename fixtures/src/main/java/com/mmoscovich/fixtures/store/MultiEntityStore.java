package com.mmoscovich.fixtures.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MultiEntityStore implements EntityStore {
	private Collection<EntityStore> stores;

	public MultiEntityStore(EntityStore... stores) {
		this.stores = Arrays.asList(stores);
	}
	
	public MultiEntityStore(Collection<EntityStore> stores) {
		this.stores = stores;
	}
	
	@Override
	public Iterator<Object> iterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getAll() {
		List<Object> result = new ArrayList<Object>();
		for(EntityStore store : stores) {
			result.addAll(store.getAll());
		}
		return result;
	}

	@Override
	public <T> List<T> getEntities(Class<T> clazz) {
		List<T> result = new ArrayList<T>();
		for(EntityStore store : stores) {
			result.addAll(store.getEntities(clazz));
		}
		return result;
	}

	@Override
	public <T> List<T> getEntities(Class<T> clazz, String... names) {
		List<T> result = new ArrayList<T>();
		for(EntityStore store : stores) {
			result.addAll(store.getEntities(clazz, names));
		}
		return result;
	}

	@Override
	public <T> T getEntity(Class<T> clazz, String name) {
		for(EntityStore store : stores) {
			T entity = store.getEntity(clazz, name);
			if(entity != null) return entity;
		}
		return null;
	}

	@Override
	public <T> T getAnyEntity(Class<T> clazz) {
		for(EntityStore store : stores) {
			T entity = store.getAnyEntity(clazz);
			if(entity != null) return entity;
		}
		return null;
	}

	@Override
	public <T> List<T> getAnyEntities(Class<T> clazz, int maxNumber) {
		List<T> result = new ArrayList<T>();
		for(EntityStore store : stores) {
			List<T> temp = store.getAnyEntities(clazz, maxNumber);
			result.addAll(temp);
			maxNumber -= temp.size();
		}
		return result;
	}

	@Override
	public List<Class<?>> getEntityClasses() {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(EntityStore store : stores) {
			result.addAll(store.getEntityClasses());
		}
		return result;
	}

}
