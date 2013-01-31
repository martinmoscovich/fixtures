package com.mmoscovich.fixtures.yaml;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mmoscovich.fixtures.model.Model;

/**
 * Yaml files preprocessor. Prepares the YAML to be processed
 * 
 * @author Martin.Moscovich
 *
 */
public class YamlPreProcessor {
	private static final Logger log = LoggerFactory.getLogger(YamlPreProcessor.class);
	
	private static final String ENTITY_SEPARATOR = "---";
	
	public static final String EXTENSION = "yml";
	
	/**
	 * Reads a list of files, process then and then merge them together to return a single String
	 * @param files files to process
	 * @return String containing the processed yaml
	 * @throws IOException if fixtures file is not found or there's a problem reading it
	 */
	public static String preprocess(Collection<Model> files, String version) throws IOException {
		StringBuilder sb = new StringBuilder();
		for(Model entity: files) {
			if(entity.isRoot()) {
				sb.append(readFile(entity, version) + "---\n");
			}
		}
		return sb.toString();
	}
	
	/**
	 * If the entity has an id, modify it to prepend the class name, so ids must be unique for a particular class.
	 * Also, the reference anywhere else will be clearer 
	 * (eg: a profile with id "home" -> "profile-home") 
	 * @param name
	 * @param line name of the class
	 * @return the modified line
	 */
	private static String processEntityId(String name, String line) {
		// If the entity overrides the default classname use the one specified (using !)
		if(line.contains("!")) {
			name = line.substring(line.indexOf("!")+1).toLowerCase();
			// Retrieve the class name from the full qualified name (ie remove package)
			if(name.contains(".")) name = name.substring(name.lastIndexOf(".")+1);
		}
		// Replace
		return line.replaceFirst("&", "&"+name+"-");
	}
	
	/**
	 * Add the class of the entity (unless it's already specified).
	 * This will allow to merge all yaml files and still be able to tell the class of each entity
	 * @param className name of the class being processed (used if it's not specified for that entity)
	 * @param line
	 * @return the modified line
	 */
	private static String processClassReference(String className, String line) {
		// If the line contain class info, do nothing
		if(line.contains("!")) return line;
		
		if(line.startsWith("&")) {
			// if the line contains an id, add the class after it
			line += " !" + className;
		} else {
			// Otherwise the line is already an attribute, move it down and add the class info to the current line
			line = "!" + className + "\n" + line;
		}
		return line;
	}
	
	/**
	 * Gets a reader from the path (it might be an uri, a path or a classpath uri)
	 * @param path
	 * @return
	 * @throws FileNotFoundException if file is not found
	 */
	private static BufferedReader getReader(String path) throws FileNotFoundException {
		InputStreamReader streamReader;
		
		try {
			streamReader = new FileReader(path);
		} catch(FileNotFoundException e) {
			InputStream is = ClassLoader.getSystemResourceAsStream(path);
			if(is == null) {
				throw new FileNotFoundException("No YAML file was found in the classpath using the URI: " + path);
			}
			streamReader = new InputStreamReader(is);
		}
		
		return new BufferedReader(streamReader);
	}
	
	/**
	 * PRocess a file line by line
	 * @param entity entity file
	 * @return the processed string
	 * @throws IOException if fixtures file is not found or there's a problem reading it
	 */
	private static String readFile(Model entity, String version) throws IOException {
		log.debug("Reading YAML file {}", entity.getLocation(version, EXTENSION));

		BufferedReader br = getReader(entity.getLocation(version, EXTENSION));
		try {
			
			String className = entity.getClazz().getName();
			String name = entity.getClazz().getSimpleName().toLowerCase();
			boolean first = true;
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while (line != null)
			{
				// Dont process empty lines (remove them)
				if(!line.trim().isEmpty()) {
					if(first) {
						// If it's the first line of the entity, process its headers (id and class)
						line = processEntityId(name, line);
						line = processClassReference(className, line);
						first = false;
					}
					if(line.startsWith(ENTITY_SEPARATOR)) {
						// If there's a separator, the next line will be the first of the new entity
						first = true;
					}
					
					// Append the processed line to the string
					sb.append(line+"\n");
				}
				
				// Get next line
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}
}
