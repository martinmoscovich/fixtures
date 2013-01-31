package com.mmoscovich.fixtures.yaml.mappers;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.scalar.ScalarSerializer;

/**
 * Maps a {@link LocalDateTime} to YAML and viceversa
 * @author Martin.Moscovich
 *
 */
public class LocalDateTimeYamlMapper implements ScalarSerializer<LocalDateTime> {
	private static final String format = "dd/MM/yyyy HH.mm.ss";
	private static final String format2 = "dd/MM/yyyy HH.mm";
	
	@Override
	public LocalDateTime read(String arg0) throws YamlException {
		DateNowExpressionParser nowParser = new DateNowExpressionParser();
		
		LocalDateTime date = nowParser.parseDateTime(arg0);
		
		if(date == null) {
			try {
				date = DateTimeFormat.forPattern(format).parseLocalDateTime(arg0);
			} catch (Exception e) {
				try {
					date = DateTimeFormat.forPattern(format2).parseLocalDateTime(arg0);
				} catch (Exception ex) {
					throw new YamlException("Invalid datetime format", ex);
				}
			}
		}
		
		return date;
	}

	@Override
	public String write(LocalDateTime arg0) throws YamlException {
		return arg0.toString(format);
	}
}