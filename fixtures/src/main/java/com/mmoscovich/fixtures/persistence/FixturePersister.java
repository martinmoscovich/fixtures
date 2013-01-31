package com.mmoscovich.fixtures.persistence;

import java.util.Arrays;
import java.util.List;

import com.mmoscovich.fixtures.config.FixtureLoaderConfig;
import com.mmoscovich.fixtures.exception.EntityReaderException;
import com.mmoscovich.fixtures.exception.FixturesException;
import com.mmoscovich.fixtures.exception.PersistenceException;
import com.mmoscovich.fixtures.model.Model;
import com.mmoscovich.fixtures.persistence.config.FixturePersisterConfig;
import com.mmoscovich.fixtures.store.EntityStore;

public class FixturePersister {
	private FixturePersisterConfig config;
	
	public FixturePersister(FixturePersisterConfig config) {
		this.config = config;
	}

	public EntityStore persist() throws PersistenceException {
		return this.persist("default");
	}
	public EntityStore persist(String version) throws PersistenceException {
		return this.persistGroup(FixtureLoaderConfig.DEFAULT_GROUP_NAME, version);
	}
	
	public EntityStore persistGroup(String groupName) throws PersistenceException {
		return this.persistGroup(groupName, "default");
	}
	
	public EntityStore persistGroup(String groupName, String version) throws PersistenceException {
		if(config.getPersister() == null) throw new PersistenceException("No persister was configured"); 
		if(config.getLoader() == null) throw new PersistenceException("Fixture loader was not configured");
		EntityStore store;
		try {
			store = config.getLoader().loadGroup(groupName, version);
		} catch (FixturesException e) {
			throw new PersistenceException("Cannot persist group " + groupName + "-  version " + version , e);
		}

		config.getPersister().start();
		for(Class<?> clazz : store.getEntityClasses()) {
			config.getPersister().persist(store.getEntities(clazz));
		}
		config.getPersister().finish();
		
		return store;
	}
	
	public void persistEntity(Object entity) throws PersistenceException {
		this.persistEntities(Arrays.asList(entity));
	}
	
	public void persistEntities(List<?> entities) throws PersistenceException {
		config.getPersister().start();
		
		for(Object entity : entities) {
			Model model = config.getLoader().getConfig().getModel(entity.getClass());
			if(model == null) throw new PersistenceException("Model not found for class " + entity.getClass().getSimpleName());
			config.getPersister().persist(entity);
			
		}
		config.getPersister().finish();
	}
}
