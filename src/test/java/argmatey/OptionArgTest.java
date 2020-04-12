package argmatey;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import argmatey.ArgMatey.OptionArg;

public class OptionArgTest {

	@Test
	public void testGetObjectValue01() {
		String optionArg = "Aladdin";
		Object objectValue = optionArg;
		OptionArg optArg = new OptionArg(optionArg, objectValue);
		assertEquals(objectValue, optArg.getObjectValue());
	}

	@Test
	public void testGetObjectValue02() {
		String optionArg = "Aladdin,Jasmine,Apu";
		Object objectValue = "Aladdin";
		OptionArg optArg = new OptionArg(
				optionArg, 
				Arrays.asList(
						new OptionArg("Aladdin", "Aladdin"), 
						new OptionArg("Jasmine", "Jasmine"),
						new OptionArg("Apu", "Apu")));
		assertEquals(objectValue, optArg.getObjectValue());
	}
	
	@Test
	public void testGetObjectValues() {
		String optionArg = "Aladdin;Jasmine;Apu";
		List<String> objectValues = Arrays.asList("Aladdin", "Jasmine", "Apu");
		OptionArg optArg = new OptionArg(
				optionArg, 
				Arrays.asList(
						new OptionArg("Aladdin", "Aladdin"), 
						new OptionArg("Jasmine", "Jasmine"),
						new OptionArg("Apu", "Apu")));
		assertEquals(objectValues, optArg.getObjectValues());
	}
	
	@Test
	public void testGetTypeValue01() {
		String optionArg = "42";
		final int i = 42;
		OptionArg optArg = new OptionArg(optionArg, Integer.valueOf(i));
		assertEquals(Integer.valueOf(i), optArg.getTypeValue(Integer.class));
	}
	
	@Test
	public void testGetTypeValue02() {
		String optionArg = "42;34;97"; 
		final int i1 = 42;
		final int i2 = 34;
		final int i3 = 97;
		OptionArg optArg = new OptionArg(
				optionArg, 
				Arrays.asList(
						new OptionArg("42", Integer.valueOf(i1)), 
						new OptionArg("34", Integer.valueOf(i2)), 
						new OptionArg("97", Integer.valueOf(i3))));
		assertEquals(Integer.valueOf(i1), optArg.getTypeValue(Integer.class));
	}
	
	@Test(expected=ClassCastException.class)
	public void testGetTypeValueForClassCastException() {
		String optionArg = "42";
		final int i = 42;
		OptionArg optArg = new OptionArg(optionArg, Integer.valueOf(i));
		optArg.getTypeValue(String.class);
	}
	
	@Test
	public void testGetTypeValues() {
		String optionArg = "31 27 36"; 
		final int i1 = 31;
		final int i2 = 27;
		final int i3 = 36;
		List<Integer> typeValues = Arrays.asList(
				Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(i3));
		OptionArg optArg = new OptionArg(
				optionArg, 
				Arrays.asList(
						new OptionArg("31", Integer.valueOf(i1)),
						new OptionArg("27", Integer.valueOf(i2)),
						new OptionArg("36", Integer.valueOf(i3))));
		assertEquals(typeValues, optArg.getTypeValues(Integer.class));
	}

	@Test(expected=ClassCastException.class)
	public void testGetTypeValuesForClassCastException() {
		String optionArg = "31 27 36";
		final int i1 = 31;
		final int i2 = 27;
		final int i3 = 36;
		OptionArg optArg = new OptionArg(
				optionArg, 
				Arrays.asList(
						new OptionArg("31", Integer.valueOf(i1)), 
						new OptionArg("27", Integer.valueOf(i2)), 
						new OptionArg("36", Integer.valueOf(i3))));
		optArg.getTypeValues(String.class);
	}
	
	@Test
	public void testToString01() {
		String optionArg = "Aladdin";
		Object objectValue = optionArg;
		OptionArg optArg = new OptionArg(optionArg,	objectValue);
		assertEquals(optionArg, optArg.toString());
	}
		
	@Test
	public void testToString02() {
		String optionArg = "Jasmine,42,status=single";
		Object objectValue = optionArg;
		OptionArg optArg = new OptionArg(optionArg,	objectValue);
		assertEquals(optionArg, optArg.toString());
	}
		
	@Test
	public void testToString03() {
		String optionArg = "96 39 82";
		final int i1 = 96;
		final int i2 = 39;
		final int i3 = 82;
		OptionArg optArg = new OptionArg(
				optionArg, 
				Arrays.asList(
						new OptionArg("96", Integer.valueOf(i1)), 
						new OptionArg("39", Integer.valueOf(i2)), 
						new OptionArg("82", Integer.valueOf(i3))));
		assertEquals(optionArg, optArg.toString());
	}
}
