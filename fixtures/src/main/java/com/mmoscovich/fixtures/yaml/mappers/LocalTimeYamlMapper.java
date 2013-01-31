package com.mmoscovich.fixtures.yaml.mappers;

import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.scalar.ScalarSerializer;

/**
 * Maps a {@link LocalTime} to YAML and viceversa
 * @author Martin.Moscovich
 *
 */
public class LocalTimeYamlMapper implements ScalarSerializer<LocalTime> {
	
	@Override
	public LocalTime read(String arg0) throws YamlException {
		DateNowExpressionParser nowParser = new DateNowExpressionParser();
			
		LocalTime time = nowParser.parseTime(arg0);
			
		if(time == null) {
			try {
				time = DateTimeFormat.forPattern("HH.mm.ss").parseLocalTime(arg0);
			} catch (Exception e) {
				try {
					time = DateTimeFormat.forPattern("HH.mm").parseLocalTime(arg0);
				} catch (Exception ex) {
					throw new YamlException("Invalid date format", ex);
				}
			}
		}
		return time;
	}

	@Override
	public String write(LocalTime arg0) throws YamlException {
		return arg0.toString("HH.mm");
	}
}