package com.mmoscovich.fixtures.persistence.hibernate.config;

import org.hibernate.dialect.Dialect;
import org.jdom2.Element;

import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.exception.RequiredConfigAttributeMissing;
import com.mmoscovich.fixtures.persistence.config.EntityPersisterConfig;
import com.mmoscovich.fixtures.persistence.config.EntityPersisterXmlConfigurationParser;

public class HibernateXmlConfigurationParser implements
		EntityPersisterXmlConfigurationParser {
	
	private static final String CONFIG_TYPE = "type";
	
	private static final String TYPE_SPRING = "spring";
	private static final String SPRING_CONTEXT_LOCATION = "contextLocation";
	private static final String SPRING_BEAN_NAME = "sessionFactoryBean";
	private static final String SPRING_TX = "springTransactions";

	private static final String TYPE_DIRECT = "direct";
	private static final String DIRECT_TABLES_TAG = "generateTables";
	private static final String DIRECT_URL_TAG = "url";
	private static final String DIRECT_DIALECT_TAG = "dialect";
	private static final String DIRECT_USERNAME_TAG = "username";
	private static final String DIRECT_PASSWORD_TAG = "password" ;
	private static final String DIRECT_DRIVER_TAG = "driver";
	
	private String readValue(Element elem, String attributeName) throws RequiredConfigAttributeMissing {
		String value = elem.getAttributeValue(attributeName);
		if(value == null) throw new RequiredConfigAttributeMissing(attributeName);
		return value;
	}
	private String readValue(Element elem, String attributeName, String defaultValue) {
		try {
			return this.readValue(elem, attributeName);
		} catch (RequiredConfigAttributeMissing e) {
			return defaultValue;
		}
	}
	
	private Element getChild(Element parent, String childName) throws RequiredConfigAttributeMissing {
		Element child = parent.getChild(childName);
		if(child == null) throw new RequiredConfigAttributeMissing(childName);
		return child;
	}
	
	private String getChildValue(Element parent, String childName) throws RequiredConfigAttributeMissing {
		Element child = parent.getChild(childName);
		if(child == null) throw new RequiredConfigAttributeMissing(childName);
		return child.getValue();
	}

	private String getChildValue(Element parent, String childName, String defaultValue) {
		try {
			return this.getChildValue(parent, childName);
		} catch (RequiredConfigAttributeMissing e) {
			return defaultValue;
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public EntityPersisterConfig buildConfig(Element root) throws FixturesConfigurationException {
		String type = readValue(root, CONFIG_TYPE);
		
		if(TYPE_SPRING.equalsIgnoreCase(type)) {
			String contextLocation = getChildValue(root, SPRING_CONTEXT_LOCATION);
			String bean = getChildValue(root, SPRING_BEAN_NAME, null);
			boolean springTx = ("true".equalsIgnoreCase(getChildValue(root, SPRING_TX)));
			return new SpringStandaloneHibernateEntityLoaderConfig(contextLocation, bean, springTx);
		} else if(TYPE_DIRECT.equalsIgnoreCase(type)) {
			String url = getChildValue(root, DIRECT_URL_TAG);
			Class<? extends Dialect> dialectClass;
			String dialect = getChildValue(root, DIRECT_DIALECT_TAG);
			try {
				dialectClass = (Class<? extends Dialect>) Class.forName(dialect);
			} catch (ClassNotFoundException e) {
				throw new FixturesConfigurationException("Cannot find the dialect class " + dialect);
			}
			String driver = getChildValue(root, DIRECT_DRIVER_TAG);
			String user = getChildValue(root, DIRECT_USERNAME_TAG);
			String pass = getChildValue(root, DIRECT_PASSWORD_TAG, "");
			boolean generateTables = ("true".equalsIgnoreCase(getChildValue(root, DIRECT_TABLES_TAG, "true")));
			
			DirectHibernateEntityLoaderConfig config = new DirectHibernateEntityLoaderConfig(driver, url, user, pass, dialectClass);
			config.setGenerateTables(generateTables);
			
			return config;
		}
		return null;
	}

}
