package argmatey;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

import argmatey.ArgMatey.OptionalBoolean;

public class OptionalBooleanTest {

	@Test
	public void testOptionalBooleanValueFalse() {
		assertEquals(
				OptionalBoolean.FALSE.optionalBooleanValue(), 
				Optional.ofNullable(Boolean.FALSE));
	}

	@Test
	public void testOptionalBooleanValueTrue() {
		assertEquals(
				OptionalBoolean.TRUE.optionalBooleanValue(), 
				Optional.ofNullable(Boolean.TRUE));
	}

	@Test
	public void testOptionalBooleanValueUnspecified() {
		assertEquals(
				OptionalBoolean.UNSPECIFIED.optionalBooleanValue(), 
				Optional.ofNullable(null));
	}

}
