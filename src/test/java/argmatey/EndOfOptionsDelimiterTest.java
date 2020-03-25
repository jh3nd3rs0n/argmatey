package argmatey;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import argmatey.ArgMatey.EndOfOptionsDelimiter;

public class EndOfOptionsDelimiterTest {

	@Test
	public void testToString() {
		assertEquals("--", EndOfOptionsDelimiter.INSTANCE.toString());
	}

}
