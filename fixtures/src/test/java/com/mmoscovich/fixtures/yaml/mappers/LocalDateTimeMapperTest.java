package com.mmoscovich.fixtures.yaml.mappers;

import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.esotericsoftware.yamlbeans.YamlException;
import com.mmoscovich.fixtures.yaml.mappers.LocalDateTimeYamlMapper;

/**
 * Test class for {@link LocalDateTimeMapper}
 * @author Martin.Moscovich
 *
 */
public class LocalDateTimeMapperTest {

	private static LocalDateTime NOW;
	
	private LocalDateTimeYamlMapper mapper = new LocalDateTimeYamlMapper();
	
	@Before
	public void setUp() {
		// Fix "now" to make tests easier
		DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis());
	
		NOW = LocalDateTime.now().withSecondOfMinute(0).withMillisOfSecond(0);
	}
	
	private LocalDateTime build(int day, int month, int year, int hours, int minutes, int seconds) {
		return new LocalDateTime(year, month, day, hours, minutes, seconds);
	}
	
	@Test
	public void testNormal() throws YamlException {
		Assert.assertEquals(build(30, 1, 2010, 22, 30, 0), mapper.read("30/01/2010 22.30"));
		Assert.assertEquals(build(30, 1, 2010, 22, 30, 45), mapper.read("30/01/2010 22.30.45"));
		Assert.assertEquals(build(30, 1, 2012, 12, 20, 10), mapper.read("30/01/2012 12.20.10"));
	}
	
	@Test(expected=YamlException.class)
	public void testInvalidDate() throws YamlException {
		mapper.read("31/06/2009");
	}
	
	@Test
	public void testNow() throws YamlException {
		Assert.assertEquals(NOW, mapper.read("now"));
		
		Assert.assertEquals(NOW.plusDays(5), mapper.read("now+5d"));
		Assert.assertEquals(NOW.minusDays(12), mapper.read("now-12d"));
		
		Assert.assertEquals(NOW.minusHours(12), mapper.read("now-12h"));
		
		Assert.assertEquals(NOW.plusMinutes(32), mapper.read("now+32m"));
		
		Assert.assertEquals(NOW.plusSeconds(22), mapper.read("now+22s"));
		
		Assert.assertEquals(NOW, mapper.read("now+0d"));
	}
	
	@Test(expected=YamlException.class)
	public void testWrongExpression() throws YamlException {
		mapper.read("asd");
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
