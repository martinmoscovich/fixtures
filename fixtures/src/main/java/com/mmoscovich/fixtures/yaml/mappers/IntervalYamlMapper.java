package com.mmoscovich.fixtures.yaml.mappers;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.scalar.ScalarSerializer;

/**
 * Maps an {@link Interval} to YAML and viceversa
 * @author Martin.Moscovich
 *
 */
public class IntervalYamlMapper implements ScalarSerializer<Interval> {
	private DateTimeYamlMapper dateTimeMapper = new DateTimeYamlMapper();
	
	@Override
	public Interval read(String arg0) throws YamlException {
		String[] dates = arg0.split("\\|");
		
		if(dates.length != 2) throw new YamlException("Invalid interval format. The interval must contain two dates separated by '|'");
		
		DateTime dateFrom = dateTimeMapper.read(dates[0].trim());
		DateTime dateTo = dateTimeMapper.read(dates[1].trim());
		
		try {
			return new Interval(dateFrom, dateTo);
		} catch(IllegalArgumentException e) {
			throw new YamlException("Invalid dates for the interval", e);
		}
	}

	@Override
	public String write(Interval arg0) throws YamlException {
		return dateTimeMapper.write(arg0.getStart()) + "|" + dateTimeMapper.write(arg0.getEnd());
	}

}
