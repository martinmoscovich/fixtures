package com.mmoscovich.fixtures.persistence.builder;

import com.mmoscovich.fixtures.FixtureLoader;
import com.mmoscovich.fixtures.persistence.EntityPersister;
import com.mmoscovich.fixtures.persistence.config.EntityPersisterConfig;

public class ManualFixturePersisterBuilder extends FixturePersisterBuilder {
	public void setPersister(EntityPersister persister) {
		this.config.setPersister(persister);
	}
	public void setConfig(EntityPersisterConfig config) {
		this.config.setConfig(config);
	}
	public void setLoader(FixtureLoader loader) {
		this.config.setLoader(loader);
	}
}
