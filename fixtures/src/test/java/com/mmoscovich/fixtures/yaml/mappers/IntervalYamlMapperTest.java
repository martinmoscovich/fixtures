package com.mmoscovich.fixtures.yaml.mappers;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.Interval;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.esotericsoftware.yamlbeans.YamlException;
import com.mmoscovich.fixtures.yaml.mappers.IntervalYamlMapper;

/**
 * Test class for {@link IntervalYamlMapper}
 * @author Martin.Moscovich
 *
 */
public class IntervalYamlMapperTest {

	private static DateTime NOW;
	
	private IntervalYamlMapper mapper = new IntervalYamlMapper();
	
	@Before
	public void setUp() {
		// Fix "now" to make tests easier
		DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis());
	
		NOW = DateTime.now().withSecondOfMinute(0).withMillisOfSecond(0);
	}
	
	private DateTime build(int day, int month, int year, int hours, int minutes, int seconds) {
		return new DateTime(year, month, day, hours, minutes, seconds);
	}
	
	@Test
	public void testNormal() throws YamlException {
		Interval interval = new Interval(build(30, 1, 2010, 22, 30, 0), build(30, 1, 2010, 23, 30, 35));
		Assert.assertEquals(interval, mapper.read("30/01/2010 22.30 | 30/01/2010 23.30.35"));
		
		interval = new Interval(build(26, 1, 2010, 22, 30, 10), build(29, 1, 2010, 11, 15,0));
		Assert.assertEquals(interval, mapper.read("26/01/2010 22.30.10 | 29/01/2010 11.15"));
	}
	
	@Test(expected=YamlException.class)
	public void testInvalidReverseInterval() throws YamlException {
		mapper.read("30/07/2009 10.30 | 02/07/2009 12.15");
	}
	
	@Test(expected=YamlException.class)
	public void testInvalidFromDate() throws YamlException {
		mapper.read("31/06/2009 10.30 | 02/07/2009 12.15");
	}
	
	@Test(expected=YamlException.class)
	public void testInvalidFromHour() throws YamlException {
		mapper.read("30/06/2009 25.30 | 02/07/2009 12.15");
	}
	
	@Test(expected=YamlException.class)
	public void testInvalidToDate() throws YamlException {
		mapper.read("30/06/2009 21.30 | 32/07/2009 12.15");
	}
	
	@Test(expected=YamlException.class)
	public void testInvalidToHour() throws YamlException {
		mapper.read("30/06/2009 21.30 | 22/07/2009 12.65");
	}
	
	@Test
	public void testNow() throws YamlException {
		Assert.assertEquals(new Interval(NOW, NOW), mapper.read("now | now"));
		
		Assert.assertEquals(new Interval(NOW.minusDays(12), NOW.plusDays(5)), mapper.read("now-12d | now+5d"));
		
		Assert.assertEquals(new Interval(NOW.minusHours(12), NOW.plusMinutes(32)), mapper.read("now-12h | now+32m"));
		
	}
	
	@Test(expected=YamlException.class)
	public void testWrongFromExpression() throws YamlException {
		mapper.read("asd | 20/10/2011 10.30");
	}
	
	@Test(expected=YamlException.class)
	public void testWrongToExpression() throws YamlException {
		mapper.read("20/10/2011 10.30 | asd");
	}
	
	@Test(expected=YamlException.class)
	public void testWrongNowExpression() throws YamlException {
		mapper.read("now+");
	}
	
	@Test(expected=YamlException.class)
	public void testWrongNowExpression2() throws YamlException {
		mapper.read("now+20");
	}
	
}
