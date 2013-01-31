package com.mmoscovich.fixtures.persistence.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.mmoscovich.fixtures.builder.FixtureLoaderBuilder;
import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.exception.RequiredConfigAttributeMissing;
import com.mmoscovich.fixtures.persistence.EntityPersister;

public class XmlConfiguredFixturePersisterConfig extends FixturePersisterConfig {
	private static final String PERSISTER_TAG = "persister";
	private static final String CONFIG_TAG = "config";
	private static final String CLASS_ATTRIBUTE = "class";
	
	private String xmlLocation;
	
	public XmlConfiguredFixturePersisterConfig(String xmlLocation) throws FixturesConfigurationException {
		this.xmlLocation = xmlLocation;
		if(xmlLocation == null || xmlLocation.isEmpty()) throw new FixturesConfigurationException("XML location must be defined");
	}
	
	private String readValue(Element elem, String attributeName) throws RequiredConfigAttributeMissing {
		String value = elem.getAttributeValue(attributeName);
		if(value == null) throw new RequiredConfigAttributeMissing(attributeName);
		return value;
	}
		
	private Element getChild(Element parent, String childName) throws RequiredConfigAttributeMissing {
		Element child = parent.getChild(childName);
		if(child == null) throw new RequiredConfigAttributeMissing(childName);
		return child;
	}
	
	@SuppressWarnings("resource")
	private InputStream getInputStream(String path) throws FileNotFoundException {
		InputStream is;
		try {
			is = new FileInputStream(path);
		} catch(FileNotFoundException e) {
			is = ClassLoader.getSystemResourceAsStream(path);
			if(is == null) {
				throw new FileNotFoundException("The XML file was found in the classpath using the URI: " + path);
			}
		}
		
		return is;
	}
	
	/**
	 * Reads the configuration from the xml file
	 * @throws FixturesConfigurationException if there's a problem in the configuration file
	 */
	public void init() throws FixturesConfigurationException {
		// Init loader first
		this.setLoader(FixtureLoaderBuilder.fromXml(xmlLocation).build());
		
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream is = getInputStream(xmlLocation);
			try {
				
				Document document = (Document) builder.build(is);
				Element rootNode = document.getRootElement();
		
				Element persisterElement = getChild(rootNode, PERSISTER_TAG);
				
				try {
					this.setPersister((EntityPersister) Class.forName(readValue(persisterElement, CLASS_ATTRIBUTE)).newInstance());
				} catch (Exception e) {
					throw new FixturesConfigurationException("There was a problem instantiating the persister", e);
				}			
				EntityPersisterXmlConfigurationParser parser = this.getPersister().getParser();
				if(parser != null) {
					EntityPersisterConfig entityConfig = parser.buildConfig(getChild(persisterElement, CONFIG_TAG));
					this.setConfig(entityConfig);
				}
				
			} finally {
				if(is!= null) is.close();
			}
		} catch (JDOMException e) {
			throw new FixturesConfigurationException("There was a problem parsing the XML config file", e);
		} catch (IOException e) {
			throw new FixturesConfigurationException("There was a problem reading the XML config file", e);
		}
	}
}
