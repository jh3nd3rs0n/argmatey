package argmatey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import argmatey.ArgMatey.DefaultStringConverter;
import argmatey.ArgMatey.StringConverter;

public class DefaultStringConverterTest {

	public static enum TestEnum { ONE, TWO, THREE }
	
	public static final class TestObject {
		
		public TestObject() { }
		
	}
	
	public static final class TestObjectWithStaticStringConversionMethod {
		
		public static TestObjectWithStaticStringConversionMethod valueOf(
				final String string) {
			String newString = "from static method: ".concat(string);
			return new TestObjectWithStaticStringConversionMethod(newString);
		}
		
		private final String string;
		
		public TestObjectWithStaticStringConversionMethod(final String str) {
			this.string = str;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [string=")
				.append(this.string)
				.append("]");
			return sb.toString();
		}
		
		
	}
	
	public static final class TestObjectWithStringParameterConstructor {
		
		private final String string;
		
		public TestObjectWithStringParameterConstructor(final String str) {
			this.string = str;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(this.getClass().getSimpleName())
				.append(" [string=")
				.append(this.string)
				.append("]");
			return sb.toString();
		}
		
	}
	
	@Test
	public void testConvert01() {
		StringConverter converter = new DefaultStringConverter(TestEnum.class);
		assertEquals(TestEnum.ONE, converter.convert("ONE"));
	}
	
	@Test
	public void testConvert02() {
		StringConverter converter = new DefaultStringConverter(TestEnum.class);
		assertEquals(TestEnum.TWO, converter.convert("TWO"));
	}
	
	@Test
	public void testConvert03() {
		StringConverter converter = new DefaultStringConverter(TestEnum.class);
		assertEquals(TestEnum.THREE, converter.convert("THREE"));
	}
	
	@Test
	public void testConvert04() {
		StringConverter converter = new DefaultStringConverter(
				TestObjectWithStaticStringConversionMethod.class);
		String string = "Hello, World";
		String toString = String.format("%s [string=from static method: %s]", 
				TestObjectWithStaticStringConversionMethod.class.getSimpleName(),
				string);
		Object object = converter.convert(string);
		assertEquals(toString, object.toString());
	}
	
	@Test
	public void testConvert05() {
		StringConverter converter = new DefaultStringConverter(
				TestObjectWithStringParameterConstructor.class);
		String string = "Hello, World";
		String toString = String.format("%s [string=%s]", 
				TestObjectWithStringParameterConstructor.class.getSimpleName(),
				string);
		Object object = converter.convert(string);
		assertEquals(toString, object.toString());
	}
	
	@Test
	public void testConvert06() {
		StringConverter converter = new DefaultStringConverter(String.class);
		String string = "Hello, World";
		assertTrue(string == converter.convert(string));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testConvertForIllegalArgumentException() {
		StringConverter converter = new DefaultStringConverter(TestEnum.class);
		converter.convert("FOUR");
	}

	@Test()
	public void testDefaultStringConverter01() {
		DefaultStringConverter converter = new DefaultStringConverter(
				TestEnum.class);
		assertEquals(TestEnum.class, converter.getConvertedType());
	}

	@Test()
	public void testDefaultStringConverter02() {
		DefaultStringConverter converter = new DefaultStringConverter(
				TestObjectWithStaticStringConversionMethod.class);
		assertEquals(TestObjectWithStaticStringConversionMethod.class, 
				converter.getConvertedType());
	}

	@Test()
	public void testDefaultStringConverter03() {
		DefaultStringConverter converter = new DefaultStringConverter(
				TestObjectWithStringParameterConstructor.class);
		assertEquals(TestObjectWithStringParameterConstructor.class, 
				converter.getConvertedType());
	}

	@Test(expected=IllegalArgumentException.class)
	public void testDefaultStringConverterForIllegalArgumentException() {
		new DefaultStringConverter(TestObject.class);
	}
	

}
