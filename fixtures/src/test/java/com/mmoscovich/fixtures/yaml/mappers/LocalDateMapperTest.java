package com.mmoscovich.fixtures.yaml.mappers;

import org.joda.time.DateTimeUtils;
import org.joda.time.LocalDate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.esotericsoftware.yamlbeans.YamlException;
import com.mmoscovich.fixtures.yaml.mappers.LocalDateYamlMapper;

/**
 * Test class for {@link LocalDateMapper}
 * @author Martin.Moscovich
 *
 */
public class LocalDateMapperTest {

	private static LocalDate NOW;
	
	private LocalDateYamlMapper mapper = new LocalDateYamlMapper();
	
	@Before
	public void setUp() {
		// Fix "now" to make tests easier
		DateTimeUtils.setCurrentMillisFixed(DateTimeUtils.currentTimeMillis());
	
		NOW = LocalDate.now();
	}
	
	private LocalDate build(int day, int month, int year) {
		return new LocalDate(year, month, day);
	}
	
	@Test
	public void testNormal() throws YamlException {
		Assert.assertEquals(build(14, 6, 2009), mapper.read("14/06/2009"));
		Assert.assertEquals(build(30, 1, 2010), mapper.read("30/01/2010"));
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
	
	@Test(expected=YamlException.class)
	public void testWrongNowExpression3() throws YamlException {
		mapper.read("now+20m");
	}
}
