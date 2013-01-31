package com.mmoscovich.fixtures.persistence.hibernate.config;

import org.hibernate.SessionFactory;


/**
 * Implementation of {@link HibernateEntityLoaderConfig} that receives a {@link SessionFactory} configured externally and
 * just uses that one
 * @author Martin.Moscovich
 *
 */
public class ManualHibernateEntityLoaderConfig extends HibernateEntityLoaderConfigBase {
	private SessionFactory factory;
	private boolean clientTx;
	
	public ManualHibernateEntityLoaderConfig(SessionFactory factory, boolean clientManagedTransactions) {
		this.factory = factory;
		this.clientTx = clientManagedTransactions;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		return factory;
	}

	@Override
	public boolean clientManagedTransactions() {
		return this.clientTx;
	}
}
