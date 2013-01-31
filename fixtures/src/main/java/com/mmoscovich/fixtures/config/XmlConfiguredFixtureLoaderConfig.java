package com.mmoscovich.fixtures.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.mmoscovich.fixtures.EntityReader;
import com.mmoscovich.fixtures.exception.FixturesConfigurationException;
import com.mmoscovich.fixtures.exception.RequiredConfigAttributeMissing;
import com.mmoscovich.fixtures.model.Group;
import com.mmoscovich.fixtures.model.Model;


public class XmlConfiguredFixtureLoaderConfig extends FixtureLoaderConfig {
	private static final String READER_TAG = "reader";
	private static final String CLASS_ATTRIBUTE = "class";
	private static final String BASE_URI = "baseUri";
	private static final String MODELS_TAG = "models";
	private static final String MODEL_TAG = "model";
	private static final String MODEL_TYPE = "type";
	private static final String MODEL_ALIAS = "alias";
	private static final String MODEL_URI = "uri";
	private static final String MODEL_GROUP_TAG = "group";
	private static final String MODEL_GROUP_NAME = "name";
	
	private String xmlLocation;
	private String baseUri;
	
	
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
	
	public XmlConfiguredFixtureLoaderConfig(String xmlLocation) throws FixturesConfigurationException {
		this.xmlLocation = xmlLocation;
		if(xmlLocation == null || xmlLocation.isEmpty()) throw new FixturesConfigurationException("XML location must be defined");
	}
	
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
	
	/**
	 * Reads the configuration from the xml file
	 * @throws FixturesConfigurationException if there's a problem in the configuration file
	 */
	public void init() throws FixturesConfigurationException {
		try {
			SAXBuilder builder = new SAXBuilder();
			InputStream is = getInputStream(xmlLocation);
			try {
				
				Document document = (Document) builder.build(is);
				Element rootNode = document.getRootElement();
		
				Element readerElement = getChild(rootNode, READER_TAG);
				
				try {
					this.setReader((EntityReader) Class.forName(readValue(readerElement, CLASS_ATTRIBUTE)).newInstance());
				} catch (Exception e) {
					throw new FixturesConfigurationException("There was a problem instantiating the reader", e);
				}			
				this.baseUri = readValue(readerElement, BASE_URI, "fixtures");

				Element modelsElem = getChild(rootNode, MODELS_TAG);
				
				this.buildModels(this, modelsElem.getChildren(MODEL_TAG));
				
				for(Element groupElem : modelsElem.getChildren(MODEL_GROUP_TAG)) {
					Group group = new Group(readValue(groupElem, MODEL_GROUP_NAME));
					this.buildModels(group, groupElem.getChildren(MODEL_TAG));
					if(group.getModels().isEmpty()) throw new FixturesConfigurationException("The group " + group.getName() + " is empty");
					this.addGroup(group);
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
	
	private void buildModels(ModelContainer container, List<Element> elements) throws FixturesConfigurationException {
		for(Element elem : elements) {
			String type = readValue(elem, MODEL_TYPE, "root");
			String alias = readValue(elem, MODEL_ALIAS, null);
			Class<?> clazz;
			try {
				clazz = Class.forName(readValue(elem, CLASS_ATTRIBUTE));
			} catch (ClassNotFoundException e) {
				throw new FixturesConfigurationException("The model class " + elem.getAttributeValue(CLASS_ATTRIBUTE) + " could not be located");
			}
			String uri = readValue(elem, MODEL_URI, null);
			if("inline".equalsIgnoreCase(type)) {
				container.addModel(Model.buildInline(clazz, alias));
			} else {
				if(uri != null) {
					container.addModel(Model.buildRootAbsolute(clazz, alias, uri));
				} else {
					container.addModel(Model.buildRootRelative(clazz, alias, this.baseUri));
				}
			}
		}
	}
}
