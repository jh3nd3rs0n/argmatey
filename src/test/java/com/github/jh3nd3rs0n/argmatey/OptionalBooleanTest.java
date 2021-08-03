package com.github.jh3nd3rs0n.argmatey;

import static org.junit.Assert.assertEquals;

import java.util.Optional;

import org.junit.Test;

import com.github.jh3nd3rs0n.argmatey.ArgMatey.OptionalBoolean;

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
