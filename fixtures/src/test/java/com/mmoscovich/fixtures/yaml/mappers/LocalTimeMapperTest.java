package com.mmoscovich.fixtures.yaml.mappers;

import org.joda.time.DateTimeUtils;
import org.joda.time.LocalTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.esotericsoftware.yamlbeans.YamlException;
import com.mmoscovich.fixtures.yaml.mappers.LocalTimeYamlMapper;

/**
 * Test class for {@link LocalTimeMapper}
 * @author Martin.Moscovich
 *
 */
public class LocalTimeMapperTest {

	private static LocalTime NOW;
	
	private LocalTimeYamlMapper mapper = new LocalTimeYamlMapper();
	
	@Before
	public void setUp() {
		// Fix "now" to make tests easier
		DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis());
	
		NOW = LocalTime.now().withSecondOfMinute(0).withMillisOfSecond(0);
	}
	
	private LocalTime build(int hours, int minutes, int seconds) {
		return new LocalTime(hours, minutes, seconds);
	}
	
	@Test
	public void testNormal() throws YamlException {
		Assert.assertEquals(build(15,30,0), mapper.read("15.30"));
		Assert.assertEquals(build(11,00,0), mapper.read("11.00"));
		Assert.assertEquals(build(13,20,10), mapper.read("13.20.10"));
		Assert.assertEquals(build(13,20,59), mapper.read("13.20.59"));
	}
	
	@Test(expected=YamlException.class)
	public void testInvalidTime() throws YamlException {
		mapper.read("12.65");
	}
	
	@Test
	public void testNow() throws YamlException {
		Assert.assertEquals(NOW, mapper.read("now"));
		
		Assert.assertEquals(NOW.plusMinutes(20), mapper.read("now+20m"));
		Assert.assertEquals(NOW.minusMinutes(120), mapper.read("now-120m"));
		
		Assert.assertEquals(NOW.plusHours(2), mapper.read("now+2h"));
		Assert.assertEquals(NOW.minusHours(12), mapper.read("now-12h"));
		
		Assert.assertEquals(NOW.plusSeconds(26), mapper.read("now+26s"));
		Assert.assertEquals(NOW.minusSeconds(16), mapper.read("now-16s"));
		
		Assert.assertEquals(NOW, mapper.read("now+0h"));
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
	
	@Test(expected=YamlException.class)
	public void testWrongNowExpression3() throws YamlException {
		mapper.read("now+20q");
	}
}
