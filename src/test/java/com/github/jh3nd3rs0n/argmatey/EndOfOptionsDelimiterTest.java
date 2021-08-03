package com.github.jh3nd3rs0n.argmatey;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.jh3nd3rs0n.argmatey.ArgMatey.EndOfOptionsDelimiter;

public class EndOfOptionsDelimiterTest {

	@Test
	public void testToString() {
		assertEquals("--", EndOfOptionsDelimiter.INSTANCE.toString());
	}

}
