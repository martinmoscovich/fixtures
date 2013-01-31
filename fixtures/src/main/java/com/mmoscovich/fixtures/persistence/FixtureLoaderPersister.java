package com.mmoscovich.fixtures.persistence;

import com.mmoscovich.fixtures.FixtureLoader;
import com.mmoscovich.fixtures.exception.FixturesException;
import com.mmoscovich.fixtures.persistence.config.FixturePersisterConfig;
import com.mmoscovich.fixtures.store.EntityStore;

public class FixtureLoaderPersister extends FixtureLoader {
	private FixturePersisterConfig persisterConfig;

	public FixtureLoaderPersister(FixturePersisterConfig config) {
		super(config.getLoader().getConfig());
		this.persisterConfig = config;
	}
	
	public EntityStore loadGroup(String groupName, String version) throws FixturesException {
		EntityStore store = super.loadGroup(groupName, version);
		
		persisterConfig.getPersister().start();
		for(Class<?> clazz : store.getEntityClasses()) {
			persisterConfig.getPersister().persist(store.getEntities(clazz));
		}
		persisterConfig.getPersister().finish();
		
		return store;
	}
}
