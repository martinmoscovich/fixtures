package com.mmoscovich.fixtures.yaml;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.esotericsoftware.yamlbeans.YamlConfig;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.scalar.ScalarSerializer;
import com.mmoscovich.fixtures.EntityReader;
import com.mmoscovich.fixtures.exception.EntityReaderException;
import com.mmoscovich.fixtures.model.Model;
import com.mmoscovich.fixtures.store.EntityStore;
import com.mmoscovich.fixtures.store.EntityStoreImpl;
import com.mmoscovich.fixtures.yaml.mappers.DateTimeYamlMapper;
import com.mmoscovich.fixtures.yaml.mappers.IntervalYamlMapper;
import com.mmoscovich.fixtures.yaml.mappers.LocalDateTimeYamlMapper;
import com.mmoscovich.fixtures.yaml.mappers.LocalDateYamlMapper;
import com.mmoscovich.fixtures.yaml.mappers.LocalTimeYamlMapper;


/**
 * This class reads entities from yaml files
 * @author Martin.Moscovich
 *
 */
public class YamlEntityReader implements EntityReader {
	private static final Logger log = LoggerFactory.getLogger(YamlEntityReader.class);
	
	private Map<Class<?>, ScalarSerializer<?>> mappers = new HashMap<Class<?>, ScalarSerializer<?>>();
	
	/**
	 * Register a custom type mapper to create non standard classes 
	 * @param clazz class to create
	 * @param mapper Serializer implementation
	 */
	public void registerTypeMapper(Class<?> clazz, ScalarSerializer<?> mapper) {
		log.info("Registered mapper {} for class {}", mapper, clazz);
		this.mappers.put(clazz, mapper);
	}
	
	private YamlConfig buildReaderConfig(Collection<Model> entities) {
		log.debug("Creating YAMLReader configuration");
		YamlConfig config = new YamlConfig();
		
		// Set custom deserializers
		for(Entry<Class<?>, ScalarSerializer<?>> entry : this.mappers.entrySet()) {
			config.setScalarSerializer(entry.getKey(), entry.getValue());
		}
		
		// Create alias for inline classes
		for(Model entity: entities) {
			if(entity.hasAlias()) {
				config.setClassTag(entity.getAlias(), entity.getClazz());
			}
		}
		
		// Joda time serializers come for free :)
		config.setScalarSerializer(LocalTime.class, new LocalTimeYamlMapper());
		config.setScalarSerializer(LocalDate.class, new LocalDateYamlMapper());
		config.setScalarSerializer(LocalDateTime.class, new LocalDateTimeYamlMapper());
		config.setScalarSerializer(DateTime.class, new DateTimeYamlMapper());
		config.setScalarSerializer(Interval.class, new IntervalYamlMapper());
		
		addGenericsToConfig(config, entities);
		
		return config;
	}
	
	/**
	 * Avoids the type erasure problem and set the List<> and Set<> attribute types
	 * @param config
	 * @param entities
	 */
	private void addGenericsToConfig(YamlConfig config, Collection<Model> entities) {
		for(Model entity : entities) {
			for(Field field : entity.getClazz().getDeclaredFields()) {
				if(field.getGenericType() instanceof ParameterizedType) {
					// If the type is parametrized, add a config to establish the right class to use
					ParameterizedType t = (ParameterizedType)field.getGenericType();
					Class<?> c = (Class<?>)t.getActualTypeArguments()[0];
					
					config.setPropertyElementType(entity.getClazz(), field.getName(), c);
				}
			}
		}
	}
	
	/**
	 * Retrieve the anchors from the reader in order to get the entities' names used in the yaml file
	 * Since the anchors property is private, we hack it using reflection
	 * @param reader
	 * @return Map which key in the entity name and the value is the entity itself
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Object> getAnchors(YamlReader reader) {
		try {
			Field f = reader.getClass().getDeclaredField("anchors");
			f.setAccessible(true);
			return (Map<String, Object>) f.get(reader);
		} catch (Exception e) {
			// If something goes wrong, return empty results
			return new HashMap<String, Object>();
		}
	}
	
	/**
	 * Merges the results from the named entities and the anonymous entities (the ones with no name).
	 * The anonymous entities get a generic name (<class name>index)
	 * @param namedEntities map containing entities with a name
	 * @param allEntities list of all the entities found (including named and anonymous)
	 * @return A map which key is the class and the value is a map (name, entity)
	 */
	private Map<Class<?>,  Map<String, Object>> mergeNamedAndAnonymousResults(Map<String, Object> namedEntities, List<Object> allEntities) {
		log.debug("Merging named and anonymous entities");
		Map<Class<?>,  Map<String, Object>> results = new HashMap<Class<?>,  Map<String, Object>>();
		
		// Add named entries first
		for(Entry<String, Object> entry : namedEntities.entrySet()) {
			// Remove the class name (eg: "profile-music" to just "music")
			String name = entry.getKey().split("-")[1];
			
			// Add to results map
			getClassEntities(results, entry.getValue().getClass()).put(name, entry.getValue());
		}
		
		// Add anonymous entities
		for(Object entity : allEntities) {
			Map<String, Object> entities = getClassEntities(results, entity.getClass());
			
			// Only add if it's not already there (because it's named)
			if(!entities.values().contains(entity)) {
				// the name is class name + index
				entities.put(entity.getClass().getSimpleName().toLowerCase() + (entities.values().size() + 1 ), entity);
			}
		}
		return results;
	}
	
	/**
	 * Helper method that returns the map (name, entity) for a Class or creates a new one if it doesnt exists and
	 * adds it to the map
	 * @param map map where we should look
	 * @param clazz class used as key
	 * @return Map which exists on the map as a value or a new one
	 */
	private Map<String, Object> getClassEntities(Map<Class<?>, Map<String, Object>> map, Class<?> clazz) {
		// Get the map using the class as key
		Map<String, Object> entities = map.get(clazz);
		if(entities == null) {
			// if no map was found, create it and add it to the big map
			entities = new HashMap<String, Object>();
			map.put(clazz, entities);
		}
		return entities;
	}
	
	/**
	 * Creates a list with the classes of the entities passed in the same order
	 * @param entities
	 * @return
	 */
	private List<Class<?>> getEntityClasses(Collection<Model> entities) {
		List<Class<?>> result = new ArrayList<Class<?>>();
		for(Model entity : entities) {
			result.add(entity.getClazz());
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see com.globallogic.fixtures.yaml.EntityReader#load(java.util.List)
	 */
	@Override
	public EntityStore load(Collection<Model> entities, String version) throws EntityReaderException {
		try {
			// Build the configuration
			YamlConfig config = buildReaderConfig(entities);
			
			// Preprocess the files and return them as one big string
			YamlReader reader = null;
			try {
				String content;
				content = YamlPreProcessor.preprocess(entities, version);
			
				// Creates the yaml reader
				reader = new YamlReader(content, config);
				
				// Store the results
				List<Object> allEntities = new ArrayList<Object>();
						
				// Reads each entity
				Object o;
				while((o = reader.read()) != null) {
					log.debug("Read entity from YAML: {}", o);
					if(!(o instanceof String)) {
						allEntities.add(o);
					}
				}
				
				// Merges the results and create a yamlResult object
				EntityStore result = new EntityStoreImpl(getEntityClasses(entities), mergeNamedAndAnonymousResults(getAnchors(reader), allEntities));		
				
				return result;
			} finally {
				if(reader != null) {
					reader.close();
				}
			}
		} catch (IOException e) {
			throw new EntityReaderException(e);
		}
	}

	@Override
	public EntityStore load(Collection<Model> entities) throws EntityReaderException {
		return this.load(entities, "default");
	}
}
