package com.mmoscovich.fixtures.yaml.mappers;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.scalar.ScalarSerializer;

/**
 * Maps a {@link LocalDate} to YAML and viceversa
 * @author Martin.Moscovich
 *
 */
public class LocalDateYamlMapper implements ScalarSerializer<LocalDate> {
	@Override
	public LocalDate read(String arg0) throws YamlException {
		DateNowExpressionParser nowParser = new DateNowExpressionParser();
		
		LocalDate date = nowParser.parseDate(arg0);
		
		if(date == null) {
			try {
				date = DateTimeFormat.forPattern("dd/MM/yyyy").parseLocalDate(arg0);
			} catch (Exception e) {
				throw new YamlException("Invalid date format", e);
			}
		}
		
		return date;
	}

	@Override
	public String write(LocalDate arg0) throws YamlException {
		return arg0.toString("dd/MM/yyyy");
	}
}