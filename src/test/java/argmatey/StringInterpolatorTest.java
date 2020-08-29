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
		StringBuilder sb = new StringBuilder("My name is ${name}!");
		StringInterpolator.interpolate(sb, properties);
		assertEquals(expected, sb.toString());
	}

	@Test
	public void testInterpolate02() {
		Properties properties = new Properties();
		properties.setProperty("1", "one");
		properties.setProperty("2", "two");
		properties.setProperty("3", "three");
		String expected = "one plus two equals three";
		StringBuilder sb = new StringBuilder("${1} plus ${2} equals ${3}");
		StringInterpolator.interpolate(sb, properties);
		assertEquals(expected, sb.toString());
	}

	@Test
	public void testInterpolate03() {
		Properties properties = new Properties();
		properties.setProperty("${five}", "5");
		String expected = "The value from ${${five$}} is 5.";
		StringBuilder sb = new StringBuilder("The value from $${$${five$$}} is ${${five$}}.");
		StringInterpolator.interpolate(sb, properties);
		assertEquals(expected, sb.toString());
	}

	@Test
	public void testInterpolate04() {
		Properties properties = new Properties();
		String expected = "By themselves, these symbols '$', '{', '}' don't need interpolation.";
		StringBuilder sb = new StringBuilder("By themselves, these symbols '$', '{', '}' don't need interpolation.");
		StringInterpolator.interpolate(sb, properties);
		assertEquals(expected, sb.toString());
	}

	@Test
	public void testInterpolate05() {
		Properties properties = new Properties();
		properties.setProperty("nestedProperty", "6");
		properties.setProperty("property", "nestedProperty");
		String expected = "The value from ${${property$}} is 6.";
		StringBuilder sb = new StringBuilder("The value from $${$${property$$}} is ${${property$}}.");
		StringInterpolator.interpolate(sb, properties);
		assertEquals(expected, sb.toString());
	}

	@Test
	public void testInterpolate06() {
		Properties properties = new Properties();
		properties.setProperty("value", "7");
		String expected = "An incomplete ${value";
		StringBuilder sb = new StringBuilder("An incomplete ${value");
		StringInterpolator.interpolate(sb, properties);
		assertEquals(expected, sb.toString());
	}

	@Test
	public void testInterpolate07() {
		Properties properties = new Properties();
		String expected = "A bogus property: ${bogus}";
		StringBuilder sb = new StringBuilder("A bogus property: ${bogus}");
		StringInterpolator.interpolate(sb, properties);
		assertEquals(expected, sb.toString());
	}

	@Test
	public void testInterpolate08() {
		Properties properties = new Properties();
		properties.setProperty("empty", "");
		String expected = "An empty property: ''";
		StringBuilder sb = new StringBuilder("An empty property: '${empty}'");
		StringInterpolator.interpolate(sb, properties);
		assertEquals(expected, sb.toString());
	}

	@Test
	public void testInterpolate09() {
		Properties properties = new Properties();
		properties.setProperty("to", "2");
		properties.setProperty("get", "3");
		properties.setProperty("her", "4");
		properties.setProperty("234", "5");
		String expected = "The value from ${${to$}${get$}${her$}} is 5.";
		StringBuilder sb = new StringBuilder(
				"The value from $${$${to$$}$${get$$}$${her$$}} is ${${to$}${get$}${her$}}.");
		StringInterpolator.interpolate(sb, properties);
		assertEquals(expected, sb.toString());
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
		String expected = "The value from ${${all$}${${to$$$}${get$$$}${her$$$}$}${now$}} is 7.";
		StringBuilder sb = new StringBuilder(
				"The value from $${$${all$$}$${$${to$$$$$$}$${get$$$$$$}$${her$$$$$$}$$}$${now$$}} "
				+ "is ${${all$}${${to$$$}${get$$$}${her$$$}$}${now$}}.");
		StringInterpolator.interpolate(sb, properties);
		assertEquals(expected, sb.toString());
	}

}
