package com.mmoscovich.fixtures.yaml.mappers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;

public class DateNowExpressionParser {
	
	public LocalDateTime parseDateTime(String expr) {
		LocalDateTime now = LocalDateTime.now().withSecondOfMinute(0).withMillisOfSecond(0);

		if(expr.trim().equals("now")) return now; 
		
		Pattern pattern = Pattern.compile("\\bnow\\b(?:(\\+|-)(\\d{1,4})(m|h|d|s)){0,1}$");
		Matcher matcher = pattern.matcher(expr);
		
		// Using now expression
		if(matcher.matches()) {
			String operator = matcher.group(1);
			int amount = Integer.parseInt(matcher.group(2));
			if("-".equals(operator)) {
				amount = amount * -1;
			}
			String unit = matcher.group(3);
			
			if("m".equals(unit)) {
				return now.plusMinutes(amount);	
			} else if("h".equals(unit)) {
				return now.plusHours(amount);
			} else if("d".equals(unit)) {
				return now.plusDays(amount);
			} else {
				return now.plusSeconds(amount);
			}
		} else {
			return null;
		}
	}
	
	/**
	 * Parses a "now" expression to a local time.
	 * e.g. now -> current time | now+20m -> in 20 minutes | now-1h -> an hour ago
	 * @param expr string expression
	 * @return the local time or null if the expression cant be parsed
	 */
	public LocalTime parseTime(String expr) {
		Pattern pattern = Pattern.compile("\\bnow\\b(?:(\\+|-)(\\d{1,4})(m|h|s)){0,1}$");
		Matcher matcher = pattern.matcher(expr);
		
		// Using now expression
		if(!matcher.matches())  return null;
		
		LocalDateTime datetime = this.parseDateTime(expr);
		
		return datetime.toLocalTime();
	}
	
	/**
	 * Parses a "now" expression to a local date.
	 * e.g. now -> today | now+20d -> in 20 days | now-1d -> yesterday
	 * @param expr string expression
	 * @return the local date or null if the expression cant be parsed
	 */
	public LocalDate parseDate(String expr) {
		Pattern pattern = Pattern.compile("\\bnow\\b(?:(\\+|-)(\\d{1,4})(d)){0,1}$");
		Matcher matcher = pattern.matcher(expr);
		
		// Using now expression
		if(!matcher.matches())  return null;
		
		LocalDateTime datetime = this.parseDateTime(expr);
		
		return datetime.toLocalDate();
	}
}
