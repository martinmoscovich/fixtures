package com.mmoscovich.fixtures.persistence.hibernate.config;

import org.hibernate.SessionFactory;

/**
 * Base class to all {@link HibernateEntityLoaderConfig} implementations
 * 
 * @author Martin.Moscovich
 *
 */
public abstract class HibernateEntityLoaderConfigBase implements HibernateEntityLoaderConfig {
	
	/**
	 * Variables to store the UTC dates flag
	 */
	private boolean utcDates = true;

	@Override
	abstract public SessionFactory getSessionFactory();

	@Override
	public void useUTCForDates(boolean value) {
		utcDates = value;
	}

	@Override
	public boolean useUTC() {
		return utcDates;
	}

}
