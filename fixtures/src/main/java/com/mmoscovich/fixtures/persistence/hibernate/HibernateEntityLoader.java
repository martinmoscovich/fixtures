package com.mmoscovich.fixtures.persistence.hibernate;

import java.util.Arrays;
import java.util.List;
import java.util.TimeZone;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmoscovich.fixtures.exception.PersistenceException;
import com.mmoscovich.fixtures.persistence.EntityPersister;
import com.mmoscovich.fixtures.persistence.config.EntityPersisterConfig;
import com.mmoscovich.fixtures.persistence.config.EntityPersisterXmlConfigurationParser;
import com.mmoscovich.fixtures.persistence.hibernate.config.HibernateEntityLoaderConfig;
import com.mmoscovich.fixtures.persistence.hibernate.config.HibernateXmlConfigurationParser;
import com.mmoscovich.fixtures.persistence.hibernate.session.ClientManagedSessionManager;
import com.mmoscovich.fixtures.persistence.hibernate.session.HibernateSessionManager;
import com.mmoscovich.fixtures.persistence.hibernate.session.ManualSessionManager;


/**
 * Implementation of {@link EntityPersister} that uses Hibernate to persist.
 * The entities must be mappeable by Hibernate (mapping files or annotations)
 * @author Martin.Moscovich
 *
 */
public class HibernateEntityLoader implements EntityPersister {
	private static final Logger log = LoggerFactory.getLogger(HibernateEntityLoader.class);
	
	private SessionFactory sessionFactory;
	private HibernateEntityLoaderConfig config;
	private boolean initialized;
	private Session session;
	private HibernateSessionManager sessionManager;
	
	public HibernateEntityLoader() {
	}
	
	private void initialize() {
		log.debug("Building SessionFactory");
		
		this.sessionFactory = config.getSessionFactory();
		this.initialized = true;
	}
	
	
	
	/**
	 * Persist a single entity in the database
	 * @param entity entity to persist (must be mappeable by hibernate)
	 * @throws PersistenceException if there's an error while persisting
	 */
	@Override
	public void persist(Object entity) throws PersistenceException {
		this.persist(Arrays.asList(entity));
	}
	
	private void doPersist(List<?> list) throws PersistenceException {
		this.validateSession();
		
		try {
			// Iterate the list and save
			for(Object o : list) {
				saveEntity(session, o);
			}
		} catch(PersistenceException e) {
			this.rollback();
			
			throw e;
		} catch(Exception e) {
			this.rollback();
			
			throw new PersistenceException("Error while persisting", e);
		}
	}
	
	private void saveEntity(Session session, Object entity) throws PersistenceException {
		try {
			log.debug("Persisting {}", entity);
			session.save(entity);
		} catch(Exception e) {
			throw new PersistenceException("Error while persisting " + entity.toString(), e);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.globallogic.fixtures.persistence.PersistenceManager#persist(java.util.List)
	 */
	@Override
	public void persist(List<?> list) throws PersistenceException {
		TimeZone tz = TimeZone.getDefault();
		try {
			// Check if we need to switch the timezone
			if(this.config.useUTC()) {
				// Set timezone for UTC
				TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
				DateTimeZone.setDefault(DateTimeZone.UTC);
			}
			
			// Do the actual persisting
			this.doPersist(list);
			
		} finally {
			// Change the timezone back (even if there was an exception)
			if(this.config.useUTC()) {
				TimeZone.setDefault(tz);
				DateTimeZone.setDefault(DateTimeZone.forTimeZone(tz));
			}
		}
	}

	@Override
	public void deleteEntities(Class<?> clazz) throws PersistenceException {
		this.validateSession();
		
		try {
			log.debug("Deleting entities of class {}", clazz.getSimpleName());
			Query q = session.createQuery("DELETE FROM " + clazz.getSimpleName() + " WHERE id > 0");
			q.executeUpdate();
		} catch(Exception e) {
			this.rollback();
			
			throw new PersistenceException("Error while deleting", e);
		} 
	}

	@Override
	public void start() throws PersistenceException {
		if(!initialized) this.initialize();
		
		try {
			this.session = this.sessionManager.start(this.sessionFactory);
		} catch(Exception e) {
			throw new PersistenceException("Error while opening the session", e);
		}
		
	}

	@Override
	public void finish() throws PersistenceException {
		try {
			this.sessionManager.finish();
		} catch(Exception e) {
			throw new PersistenceException("Error while commiting", e);
		}
	}
	
	private boolean isSessionOpen() {
		return this.sessionManager.isSessionOpen();
	}
	
	private void rollback() {
		this.sessionManager.rollback();
	}
	
	private void validateSession() throws PersistenceException {
		// Throw an error if the session was not started
		if(!isSessionOpen()) throw new PersistenceException("No session was started. Call start() before any operation and finish() when done");
	}

	@Override
	public void setConfig(EntityPersisterConfig config) {
		this.config = (HibernateEntityLoaderConfig) config;
		this.initialized = false;
		this.sessionManager = (this.config.clientManagedTransactions()? new ClientManagedSessionManager() : new ManualSessionManager());
		
	}

	@Override
	public EntityPersisterXmlConfigurationParser getParser() {
		return new HibernateXmlConfigurationParser();
	}
	
}
