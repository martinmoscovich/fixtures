package com.mmoscovich.fixtures.store;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;



/**
 * Implements {@link EntityStore} and contains the result of the YAML parsing
 * @author Martin.Moscovich
 *
 */
public class EntityStoreImpl implements EntityStore {
	private Map<Class<?>, Map<String, Object>> results;
	private List<Class<?>> classes;
	
	public EntityStoreImpl(List<Class<?>> classes, Map<Class<?>, Map<String, Object>> results) {
		this.results = results;
		this.classes = classes;
	}
	
	/* (non-Javadoc)
	 * @see fixtures.yaml.EntityStore#getAll()
	 */
	@Override
	public List<Object> getAll() {
		List<Object> list = new ArrayList<Object>();
		
		for(Map<String, Object> map : results.values()) {
			for(Object value : map.values()) {
				list.add(value);
			}
		}
		
		return list;
	}
	
	/* (non-Javadoc)
	 * @see fixtures.yaml.EntityStore#getEntities(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> List<T> getEntities(Class<T> clazz) {
		if(results.containsKey(clazz)) {
			return new ArrayList<T>((Collection<? extends T>) results.get(clazz).values());
		} else {
			return new ArrayList<T>();
		}
	}
	
	/* (non-Javadoc)
	 * @see fixtures.yaml.EntityStore#getEntity(java.lang.Class, java.lang.String)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public <T> T getEntity(Class<T> clazz, String name) {
		if(!results.containsKey(clazz)) return null;
		Map<String, Object> entities = results.get(clazz);
		return (T) entities.get(name);
	}

	@Override
	public Iterator<Object> iterator() {
		return this.getAll().iterator();
	}

	@Override
	public <T> List<T> getEntities(Class<T> clazz, String... names) {
		List<T> result = new ArrayList<T>();
		
		for(String name : names) {
			T entity = this.getEntity(clazz, name);
			if(entity != null) {
				result.add(entity);
			}
		}
		
		return result;
	}

	@Override
	public List<Class<?>> getEntityClasses() {
		return classes;
	}

	@Override
	public <T> T getAnyEntity(Class<T> clazz) {
		List<T> result = this.getEntities(clazz);
		if(result.size() > 0) return result.get(0);
		return null;
	}

	@Override
	public <T> List<T> getAnyEntities(Class<T> clazz, int maxNumber) {
		List<T> result = this.getEntities(clazz);
		return result.subList(0, Math.min(maxNumber, result.size()));
	}
}
