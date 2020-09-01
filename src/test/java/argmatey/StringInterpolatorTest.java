package argmatey;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Test;

import argmatey.ArgMatey.StringInterpolator;

public class StringInterpolatorTest {

	@Test
	public void testInterpolate01() {
		Properties properties = new Properties();
		properties.setProperty("name", "Jonathan");
		String expected = "My name is Jonathan!";
		String actual = StringInterpolator.interpolate(
				"My name is ${name}!", properties);
		assertEquals(expected, actual);
	}

	@Test
	public void testInterpolate02() {
		Properties properties = new Properties();
		properties.setProperty("1", "one");
		properties.setProperty("2", "two");
		properties.setProperty("3", "three");
		String expected = "one plus two equals three";
		String actual = StringInterpolator.interpolate(
				"${1} plus ${2} equals ${3}", properties);
		assertEquals(expected, actual);
	}

	@Test
	public void testInterpolate03() {
		Properties properties = new Properties();
		properties.setProperty("${five}", "5");
		String expected = "The value from ${$${five$}} is 5.";
		String actual = StringInterpolator.interpolate(
				"The value from $${$$$${five$$}} is ${$${five$}}.", properties);
		assertEquals(expected, actual);
	}

	@Test
	public void testInterpolate04() {
		Properties properties = new Properties();
		String expected = "By themselves, these symbols '$', '{', '}' don't need interpolation.";
		String actual = StringInterpolator.interpolate(
				"By themselves, these symbols '$', '{', '}' don't need interpolation.", 
				properties);
		assertEquals(expected, actual);
	}

	@Test
	public void testInterpolate05() {
		Properties properties = new Properties();
		properties.setProperty("nestedProperty", "6");
		properties.setProperty("property", "nestedProperty");
		String expected = "The value from ${${property}} is 6.";
		String actual = StringInterpolator.interpolate(
				"The value from $${$${property}} is ${${property}}.", 
				properties);
		assertEquals(expected, actual);
	}

	@Test
	public void testInterpolate06() {
		Properties properties = new Properties();
		properties.setProperty("value", "7");
		String expected = "An incomplete ${value";
		String actual = StringInterpolator.interpolate(
				"An incomplete ${value", properties);
		assertEquals(expected, actual);
	}

	@Test
	public void testInterpolate07() {
		Properties properties = new Properties();
		String expected = "A bogus property: ${bogus}";
		String actual = StringInterpolator.interpolate(
				"A bogus property: ${bogus}", properties);
		assertEquals(expected, actual);
	}

	@Test
	public void testInterpolate08() {
		Properties properties = new Properties();
		properties.setProperty("empty", "");
		String expected = "An empty property: ''";
		String actual = StringInterpolator.interpolate(
				"An empty property: '${empty}'", properties);
		assertEquals(expected, actual);
	}

	@Test
	public void testInterpolate09() {
		Properties properties = new Properties();
		properties.setProperty("to", "2");
		properties.setProperty("get", "3");
		properties.setProperty("her", "4");
		properties.setProperty("234", "5");
		String expected = "The value from ${${to}${get}${her}} is 5.";
		String actual = StringInterpolator.interpolate(
				"The value from $${$${to}$${get}$${her}} is ${${to}${get}${her}}.", 
				properties);
		assertEquals(expected, actual);
	}

	@Test
	public void testInterpolate10() {
		Properties properties = new Properties();
		properties.setProperty("all", "1");
		properties.setProperty("to", "2");
		properties.setProperty("get", "3");
		properties.setProperty("her", "4");
		properties.setProperty("234", "5");
		properties.setProperty("now", "6");
		properties.setProperty("156", "7");
		String expected = "The value from ${${all}${${to}${get}${her}}${now}} is 7.";
		String actual = StringInterpolator.interpolate(
				"The value from $${$${all}$${$${to}$${get}$${her}}$${now}} "
				+ "is ${${all}${${to}${get}${her}}${now}}.", 
				properties);
		assertEquals(expected, actual);
	}

}
