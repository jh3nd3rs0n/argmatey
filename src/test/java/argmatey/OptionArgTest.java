package argmatey;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import argmatey.ArgMatey.OptionArg;
import argmatey.ArgMatey.DefaultStringConverter;

public class OptionArgTest {

	@Test
	public void testGetObjectValue01() {
		String name = "Aladdin";
		OptionArg optionArg = OptionArg.newInstance(
				name, 
				"[^\\w\\W]", 
				new DefaultStringConverter(String.class));
		assertEquals(name, optionArg.getObjectValue());
	}

	@Test
	public void testGetObjectValue02() {
		String name = "Aladdin";
		OptionArg optionArg = OptionArg.newInstance(
				"Aladdin,Jasmine,Apu", 
				",", 
				new DefaultStringConverter(String.class));
		assertEquals(name, optionArg.getObjectValue());
	}
	
	@Test
	public void testGetObjectValues() {
		List<String> list = Arrays.asList("Aladdin", "Jasmine", "Apu");
		OptionArg optionArg = OptionArg.newInstance(
				"Aladdin;Jasmine;Apu", 
				";", 
				new DefaultStringConverter(String.class));
		assertEquals(list, optionArg.getObjectValues());
	}
	
	@Test
	public void testGetTypeValue01() {
		final int i = 42;
		OptionArg optionArg = OptionArg.newInstance(
				"42", 
				"[^\\w\\W]", 
				new DefaultStringConverter(Integer.class));
		assertEquals(
				Integer.valueOf(i), optionArg.getTypeValue(Integer.class));
	}
	
	@Test
	public void testGetTypeValue02() {
		final int i = 42;
		OptionArg optionArg = OptionArg.newInstance(
				"42;34;97", 
				";", 
				new DefaultStringConverter(Integer.class));
		assertEquals(
				Integer.valueOf(i), optionArg.getTypeValue(Integer.class));
	}
	
	@Test(expected=ClassCastException.class)
	public void testGetTypeValueForClassCastException() {
		OptionArg optionArg = OptionArg.newInstance(
				"42", 
				"[^\\w\\W]", 
				new DefaultStringConverter(Integer.class));
		optionArg.getTypeValue(String.class);
	}
	
	@Test
	public void testGetTypeValues() {
		final int i1 = 31;
		final int i2 = 27;
		final int i3 = 36;
		List<Integer> list = Arrays.asList(
				Integer.valueOf(i1), Integer.valueOf(i2), Integer.valueOf(i3));
		OptionArg optionArg = OptionArg.newInstance(
				"31 27 36", 
				" ", 
				new DefaultStringConverter(Integer.class));
		assertEquals(list, optionArg.getTypeValues(Integer.class));
	}

	@Test(expected=ClassCastException.class)
	public void testGetTypeValuesForClassCastException() {
		OptionArg optionArg = OptionArg.newInstance(
				"31 27 36", 
				" ", 
				new DefaultStringConverter(Integer.class));
		optionArg.getTypeValues(String.class);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNewInstanceForIllegalArgumentException01() {
		OptionArg.newInstance(
				"Aladdin", 
				"[^\\w\\W]", 
				new DefaultStringConverter(Integer.class));
	}

	@Test(expected=IllegalArgumentException.class)
	public void testNewInstanceForIllegalArgumentException02() {
		OptionArg.newInstance(
				"1,2,3,Gus,5,6", 
				",", 
				new DefaultStringConverter(Integer.class));
	}

	@Test(expected=NullPointerException.class)
	public void testNewInstanceForNullPointerException01() {
		OptionArg.newInstance(
				null, 
				"[^\\w\\W]", 
				new DefaultStringConverter(String.class));
		
	}

	@Test(expected=NullPointerException.class)
	public void testNewInstanceForNullPointerException02() {
		OptionArg.newInstance(
				"Jasmine", 
				null, 
				new DefaultStringConverter(String.class));
		
	}

	@Test(expected=NullPointerException.class)
	public void testNewInstanceForNullPointerException03() {
		OptionArg.newInstance(
				"Apu", 
				"[^\\w\\W]", 
				null);
		
	}
	
	@Test
	public void testToString01() {
		String string = "Aladdin";
		OptionArg optionArg = OptionArg.newInstance(
				string, 
				"[^\\w\\W]", 
				new DefaultStringConverter(String.class));
		assertEquals(string, optionArg.toString());
	}
		
	@Test
	public void testToString02() {
		String string = "Jasmine,42,status=single";
		OptionArg optionArg = OptionArg.newInstance(
				string, 
				"[^\\w\\W]", 
				new DefaultStringConverter(String.class));
		assertEquals(string, optionArg.toString());
	}
		
	@Test
	public void testToString03() {
		String string = "96 39 82";
		OptionArg optionArg = OptionArg.newInstance(
				string, 
				" ", 
				new DefaultStringConverter(Integer.class));
		assertEquals(string, optionArg.toString());
	}
}
