package com.github.jh3nd3rs0n.argmatey;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.argmatey.ArgMatey.OptionalBoolean;

public class OptionalBooleanTest {

	@Test
	public void testOptionalBooleanValueFalse() {
		assertEquals(
				OptionalBoolean.FALSE.optionalBooleanValue(), Boolean.FALSE);
	}

	@Test
	public void testOptionalBooleanValueTrue() {
		assertEquals(
				OptionalBoolean.TRUE.optionalBooleanValue(), Boolean.TRUE);
	}

	@Test
	public void testOptionalBooleanValueUnspecified() {
		assertEquals(
				OptionalBoolean.UNSPECIFIED.optionalBooleanValue(),	null);
	}

}
