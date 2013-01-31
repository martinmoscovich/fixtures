package com.mmoscovich.fixtures.yaml.mappers;

import org.joda.time.DateTime;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormat;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.scalar.ScalarSerializer;

/**
 * Maps a {@link DateTime} to YAML and viceversa
 * @author Martin.Moscovich
 *
 */
public class DateTimeYamlMapper implements ScalarSerializer<DateTime> {
	private static final String format = "dd/MM/yyyy HH.mm.ss";
	private static final String format2 = "dd/MM/yyyy HH.mm";
	
	@Override
	public DateTime read(String arg0) throws YamlException {
		DateNowExpressionParser nowParser = new DateNowExpressionParser();
		
		LocalDateTime localDate = nowParser.parseDateTime(arg0);
		
		DateTime date = null;
		
		if(localDate != null) {
			date = localDate.toDateTime();
		}
		
		if(date == null) {
			try {
				date = DateTimeFormat.forPattern(format).parseDateTime(arg0);
			} catch (Exception e) {
				try {
					date = DateTimeFormat.forPattern(format2).parseDateTime(arg0);
				} catch (Exception ex) {
					throw new YamlException("Invalid datetime format", e);
				}
			}
		}
		
		return date;
	}

	@Override
	public String write(DateTime arg0) throws YamlException {
		return arg0.toString(format);
	}
}