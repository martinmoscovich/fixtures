package com.mmoscovich.fixtures.store;

import java.util.List;

import com.mmoscovich.fixtures.exception.EntityStoreException;

public interface VersionedEntityStore {
	/**
	 * Returns a list of all the objects loaded
	 * @return
	 * @throws EntityStoreException 
	 */
	List<Object> getAll(String version) throws EntityStoreException;

	/**
	 * Gets all the entities of a particular class
	 * @param clazz
	 * @return
	 * @throws EntityStoreException 
	 */
	<T> List<T> getEntities(String version, Class<T> clazz) throws EntityStoreException;
	
	/**
	 * Gets the entities of a particular class with the names provided
	 * @param clazz
	 * @param names
	 * @return
	 * @throws EntityStoreException 
	 */
	<T> List<T> getEntities(String version, Class<T> clazz, String...names) throws EntityStoreException;

	/**
	 * Returns the specific entity of a given class and id
	 * @param clazz
	 * @param name
	 * @return
	 * @throws EntityStoreException 
	 */
	<T> T getEntity(String version, Class<T> clazz, String name) throws EntityStoreException;
	
	/**
	 * Return any entity of that class
	 * @param clazz
	 * @return
	 * @throws EntityStoreException 
	 */
	<T> T getAnyEntity(String version, Class<T> clazz) throws EntityStoreException;
	
	/**
	 * Return a list of any entities of the provided class with the given max number of items
	 * @param clazz
	 * @param maxNumber
	 * @return
	 * @throws EntityStoreException 
	 */
	<T> List<T> getAnyEntities(String version, Class<T> clazz, int maxNumber) throws EntityStoreException;
	
	/**
	 * Returns the classes of the entities in the order they were defined
	 * @return
	 */
	List<Class<?>> getEntityClasses();
	
	/**
	 * Returns the entity store for that version
	 * @param version
	 * @return
	 * @throws EntityStoreException 
	 */
	EntityStore getStore(String version) throws EntityStoreException;
	
	/**
	 * Indicates whether this store contains a particular version loaded
	 * @param version
	 * @return
	 */
	boolean containsVersion(String version);
}
