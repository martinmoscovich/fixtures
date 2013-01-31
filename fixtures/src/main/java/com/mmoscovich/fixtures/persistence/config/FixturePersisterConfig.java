package com.mmoscovich.fixtures.persistence.config;

import com.mmoscovich.fixtures.FixtureLoader;
import com.mmoscovich.fixtures.persistence.EntityPersister;

public class FixturePersisterConfig {
	private EntityPersister persister;
	private EntityPersisterConfig config;
	private FixtureLoader loader;
	
	public EntityPersister getPersister() {
		return persister;
	}

	public void setPersister(EntityPersister persister) {
		this.persister = persister;
		if(config != null) this.persister.setConfig(config);
	}

	public EntityPersisterConfig getConfig() {
		return config;
	}

	public void setConfig(EntityPersisterConfig config) {
		this.config = config;
		if(persister != null) this.persister.setConfig(config);
	}

	public FixtureLoader getLoader() {
		return loader;
	}

	public void setLoader(FixtureLoader loader) {
		this.loader = loader;
	}
	
}
