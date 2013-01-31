package com.mmoscovich.fixtures.persistence.hibernate.config;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.dialect.Dialect;

/**
 * Implements {@link HibernateEntityLoaderConfig} and creates an hibernate config directly using hibernate (without Spring or
 * any other help).
 * The mapped classes and database connection parameters must be passed
 * @author Martin.Moscovich
 *
 */
public class DirectHibernateEntityLoaderConfig extends HibernateEntityLoaderConfigBase {

	private List<Class<?>> classesToLoad = new ArrayList<Class<?>>();
	private boolean generateTables = true;
	private String dbUrl;
	private String dialect;
	private String dbUser;
	private String dbPass;
	private String jdbcClassName;
	
	
	/**
	 * Registers a persistent entity (annotated with @{@link Entity})
	 * @param clazz
	 */
	public void registerClass(Class<?> clazz) {
		this.classesToLoad.add(clazz);
	}
	
	/**
	 * Flag that indicates if the schema must be recreated
	 * @param value
	 */
	public void setGenerateTables(boolean value) {
		this.generateTables = value;
	}
	
	public DirectHibernateEntityLoaderConfig(String driver, String url, String user, String pass, Class<? extends Dialect> dialect) {
		this.jdbcClassName = driver;
		this.dbUrl = url;
		this.dbUser = user;
		this.dbPass = pass;
		this.dialect = dialect.getName();
	}
	
	private Configuration getConfiguration() {
		Configuration cfg = new Configuration()
	    .setProperty("hibernate.dialect", dialect)
	    .setProperty("hibernate.connection.url", dbUrl)
	    .setProperty("hibernate.connection.username", dbUser)
	    .setProperty("hibernate.connection.password", dbPass)
	    .setProperty("hibernate.connection.driver_class", jdbcClassName)
	    .setProperty("hibernate.show_sql", "true");
	    
		// Add persistent entities
		for(Class<?> c : this.classesToLoad) {
			cfg.addAnnotatedClass(c);
		}
		
		if(generateTables) {
			cfg.setProperty("hibernate.hbm2ddl.auto", "create");
		}
		return cfg;
	}
	
	@Override
	public SessionFactory getSessionFactory() {
		Configuration conf = getConfiguration();
		return conf.buildSessionFactory();
	}

	@Override
	public boolean clientManagedTransactions() {
		return false;
	}
}
