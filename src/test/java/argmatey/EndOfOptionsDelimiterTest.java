package argmatey;

import static org.junit.Assert.*;

import org.junit.Test;

public class EndOfOptionsDelimiterTest {

	@Test
	public void testToString() {
		assertEquals("--", EndOfOptionsDelimiter.INSTANCE.toString());
	}

}
