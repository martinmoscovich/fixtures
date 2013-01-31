package com.mmoscovich.fixtures.persistence;

import java.util.List;

import com.mmoscovich.fixtures.exception.PersistenceException;
import com.mmoscovich.fixtures.persistence.config.EntityPersisterConfig;
import com.mmoscovich.fixtures.persistence.config.EntityPersisterXmlConfigurationParser;

/**
 * Interface that must be implemented by the classes reponsible for persisting the entities
 * @author Martin.Moscovich
 *
 */
public interface EntityPersister {

	/**
	 * Persist a single entity in the database
	 * @param entity entity to persist
	 * @throws PersistenceException if there's an error while persisting
	 */
	void persist(Object entity) throws PersistenceException;

	/**
	 * Persists a list of entities in the database
	 * @param list
	 * @throws PersistenceException if there's an error while persisting
	 */
	void persist(List<?> list) throws PersistenceException;
	
	/**
	 * Deletes all the entities of the provided class in the database
	 * @param clazz
	 * @throws PersistenceException if there's an error while deleting
	 */
	void deleteEntities(Class<?> clazz) throws PersistenceException;
	
	/**
	 * Starts the session and transaction
	 * @throws PersistenceException if there's an error while starting the session or tx
	 */
	void start() throws PersistenceException;
	
	/**
	 * Commits the tx and closes the session
	 * @throws PersistenceException if there's an error
	 */
	void finish() throws PersistenceException;
	
	/**
	 * Sets the configuration
	 * @param config
	 */
	void setConfig(EntityPersisterConfig config);
	
	/**
	 * Returns the class responsible for parsing the XML configuration
	 * @return
	 * 
	 */
	EntityPersisterXmlConfigurationParser getParser();
}