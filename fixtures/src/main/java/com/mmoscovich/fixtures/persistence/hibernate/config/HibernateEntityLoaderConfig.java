package com.mmoscovich.fixtures.persistence.hibernate.config;

import org.hibernate.SessionFactory;

import com.mmoscovich.fixtures.persistence.config.EntityPersisterConfig;
import com.mmoscovich.fixtures.persistence.hibernate.HibernateEntityLoader;

/**
 * Interface that must be implemented by all hibernate entity loader's configurers
 * @author Martin.Moscovich
 *
 */
public interface HibernateEntityLoaderConfig extends EntityPersisterConfig {
	/**
	 * Returns the hibernate session factory
	 * @return
	 */
	SessionFactory getSessionFactory();
	
	
	/**
	 * Sets a flag indicating if UTC should be used as the timezone (required sometimes to avoid problems with tz differences 
	 * between app server and db server)
	 * @param value
	 */
	void useUTCForDates(boolean value);
	
	/**
	 * Returns the flag indicating if UTC should be used as the default timezone
	 * @return
	 */
	boolean useUTC();
	
	/**
	 * Indicates whether the client manages transactions or not. If false, a new session and tx will be created by the loader
	 * and {@link HibernateEntityLoader#start()} and {@link HibernateEntityLoader#finish()} must be used
	 * @return
	 */
	boolean clientManagedTransactions();
}
