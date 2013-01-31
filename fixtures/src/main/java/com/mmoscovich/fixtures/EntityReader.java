package com.mmoscovich.fixtures;

import java.io.IOException;
import java.util.Collection;

import com.mmoscovich.fixtures.exception.EntityReaderException;
import com.mmoscovich.fixtures.model.Model;
import com.mmoscovich.fixtures.store.EntityStore;

public interface EntityReader {

	/**
	 * Loads entities from a list of files
	 * @param entities the yaml entities to load
	 * @param version the version to load
	 * @return the results
	 * @throws IOException if something went wrong
	 */
	EntityStore load(Collection<Model> entities, String version)	throws EntityReaderException;
	
	
	/**
	 * Loads entities from a list of files using the default version
	 * @param entities the yaml entities to load
	 * @return the results
	 * @throws IOException if something went wrong
	 */
	EntityStore load(Collection<Model> entities) throws EntityReaderException;
}