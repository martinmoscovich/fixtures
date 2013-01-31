package com.mmoscovich.fixtures.store;

import java.util.List;

/**
 * Store of entities.
 * Implements iterable to iterate through the whole list.
 * Also allows to filter by class type and to retrieve one entity using its class and name
 * @author Martin.Moscovich
 *
 */
public interface EntityStore extends Iterable<Object> {

	/**
	 * Returns a list of all the objects loaded
	 * @return
	 */
	List<Object> getAll();

	/**
	 * Gets all the entities of a particular class
	 * @param clazz
	 * @return
	 */
	<T> List<T> getEntities(Class<T> clazz);
	
	/**
	 * Gets the entities of a particular class with the names provided
	 * @param clazz
	 * @param names
	 * @return
	 */
	<T> List<T> getEntities(Class<T> clazz, String...names);

	/**
	 * Returns the specific entity of a given class and id
	 * @param clazz
	 * @param name
	 * @return
	 */
	<T> T getEntity(Class<T> clazz, String name);
	
	/**
	 * Return any entity of that class
	 * @param clazz
	 * @return
	 */
	<T> T getAnyEntity(Class<T> clazz);
	
	/**
	 * Return a list of any entities of the provided class with the given max number of items
	 * @param clazz
	 * @param maxNumber
	 * @return
	 */
	<T> List<T> getAnyEntities(Class<T> clazz, int maxNumber);
	
	/**
	 * Returns the classes of the entities in the order they were defined
	 * @return
	 */
	List<Class<?>> getEntityClasses();

}