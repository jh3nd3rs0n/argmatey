package argmatey;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Optional;

import org.junit.Test;

import argmatey.ArgMatey.Annotations.Option;
import argmatey.ArgMatey.Annotations.OptionArgSpec;
import argmatey.ArgMatey.CLI;
import argmatey.ArgMatey.OptionGroupHelpTextParams;
import argmatey.ArgMatey.OptionGroupHelpTextProvider;
import argmatey.ArgMatey.OptionType;

public class Base64CLITest {

	public static class Base64CLI extends CLI {

		private int columnLimit;
		private boolean decodingMode;
		private String file;
		private boolean garbageIgnored;

		public Base64CLI(String[] args) {
			super(args, false);
			this.columnLimit = 76;
			this.decodingMode = false;
			this.file = null;
			this.garbageIgnored = false;
			this.programDoc = new StringBuilder()
					.append("Base64 encode or decode FILE, ")
					.append("or standard input, to standard output.")
					.append(String.format("%n%n"))
					.append("With no FILE, or when FILE is -, ")
					.append("read standard input.")
					.toString();
			this.programName = "base64";
			this.programOperandsUsage = "[FILE]";
			this.programVersion = "base64 1.0";
		}
		
		@Override
		protected Optional<Integer> afterHandleArgs() {
			System.out.printf("columnLimit: %s%n", this.columnLimit);
			System.out.printf("decodingMode: %s%n", this.decodingMode);
			System.out.printf("file: %s%n", this.file);
			System.out.printf("garbageIgnored: %s%n", this.garbageIgnored);
			return Optional.of(Integer.valueOf(0));
		}
		
		@Override
		protected void handleNonparsedArg(final String nonparsedArg) {
			if (this.file != null) {
				throw new IllegalArgumentException(
						String.format("extra operand: `%s'", nonparsedArg));
			}
			this.file = nonparsedArg;
		}

		@Option(
				doc = "Wrap encoded lines after COLS character (default is 76)", 
				name = "w", 
				optionArgSpec = @OptionArgSpec(name = "COLS"), 
				type = OptionType.POSIX
		)
		@Option(
				name = "wrap", 
				type = OptionType.GNU_LONG
		)
		private void setColumnLimit(Integer i) {
			int intValue = i.intValue();
			if (intValue < 0) {
				throw new IllegalArgumentException(
						"must be a non-negative integer");
			}
			this.columnLimit = intValue;
		}

		@Option(
				doc = "Decode data", 
				name = "d", 
				type = OptionType.POSIX
		)
		@Option(
				name = "decode", 
				type = OptionType.GNU_LONG
		)
		private void setDecodingMode(boolean b) {
			this.decodingMode = b;
		}

		@Option(
				doc = "When decoding, ignore non-alphabet characters", 
				name = "i", 
				type = OptionType.POSIX)
		@Option(
				name = "ignore-garbage", 
				type = OptionType.GNU_LONG
		)
		private void setGarbageIgnored(boolean b) {
			this.garbageIgnored = b;
		}

	}

	private static class SingleLineOptionGroupHelpTextProvider 
		extends OptionGroupHelpTextProvider {

		@Override
		public String getOptionGroupHelpText(OptionGroupHelpTextParams params) {
			String optionGroupHelpText = null;
			StringBuilder sb = null;
			String doc = null;
			for (ArgMatey.Option option : params.getDisplayableOptions()) {
				if (sb == null) {
					sb = new StringBuilder();
					sb.append("  ");
				} else {
					sb.append(", ");
				}
				sb.append(option.getUsage());
				if (doc == null) {
					doc = option.getDoc();
				}
			}
			if (sb != null) {
				if (doc != null) {
					final int docStart = 24;
					int length = sb.length();
					for (int i = 0; i < docStart - length; i++) {
						sb.append(' ');
					}
					sb.append(doc);
				}
				optionGroupHelpText = sb.toString();
			}
			return optionGroupHelpText;
		}

	}

	private static final String CUSTOM_PROGRAM_HELP;

	private static final String PROGRAM_HELP;

	private static final String PROGRAM_VERSION;

	static {
		StringBuilder sb1 = new StringBuilder();
		sb1.append(String.format("Usage: base64 [OPTION]... [FILE]%n"));
		sb1.append(String.format("Base64 encode or decode FILE, or standard input, to standard output.%n"));
		sb1.append(String.format("%n"));
		sb1.append(String.format("With no FILE, or when FILE is -, read standard input.%n"));
		sb1.append(String.format("%n"));
		sb1.append(String.format("OPTIONS:%n"));
		sb1.append(String.format("  -d, --decode          Decode data%n"));
		sb1.append(String.format("  --help                Display this help and exit%n"));
		sb1.append(String.format("  -i, --ignore-garbage  When decoding, ignore non-alphabet characters%n"));
		sb1.append(String.format("  --version             Display version information and exit%n"));
		sb1.append(String.format("  -w COLS, --wrap=COLS  Wrap encoded lines after COLS character (default is 76)%n"));
		sb1.append(String.format("%n"));
		CUSTOM_PROGRAM_HELP = sb1.toString();
		StringBuilder sb2 = new StringBuilder();
		sb2.append(String.format("Usage: base64 [OPTION]... [FILE]%n"));
		sb2.append(String.format("Base64 encode or decode FILE, or standard input, to standard output.%n"));
		sb2.append(String.format("%n"));
		sb2.append(String.format("With no FILE, or when FILE is -, read standard input.%n"));
		sb2.append(String.format("%n"));
		sb2.append(String.format("OPTIONS:%n"));
		sb2.append(String.format("  -d, --decode%n"));
		sb2.append(String.format("      Decode data%n"));
		sb2.append(String.format("  --help%n"));
		sb2.append(String.format("      Display this help and exit%n"));
		sb2.append(String.format("  -i, --ignore-garbage%n"));
		sb2.append(String.format("      When decoding, ignore non-alphabet characters%n"));
		sb2.append(String.format("  --version%n"));
		sb2.append(String.format("      Display version information and exit%n"));
		sb2.append(String.format("  -w COLS, --wrap=COLS%n"));
		sb2.append(String.format("      Wrap encoded lines after COLS character (default is 76)%n"));
		sb2.append(String.format("%n"));
		PROGRAM_HELP = sb2.toString();
		PROGRAM_VERSION = String.format("base64 1.0%n");
	}

	private static void handle(
			final String[] args, 
			final PrintStream err, 
			final InputStream in, 
			final PrintStream out)
			throws IOException {
		PrintStream formerErr = System.err;
		InputStream formerIn = System.in;
		PrintStream formerOut = System.out;
		if (err != null) {
			System.setErr(err);
		}
		if (out != null) {
			System.setOut(out);
		}
		if (in != null) {
			System.setIn(in);
		}
		CLI cli = new Base64CLI(args);
		try {
			cli.handleArgs();
		} finally {
			if (err != null) {
				System.setErr(formerErr);
			}
			if (out != null) {
				System.setOut(formerOut);
			}
			if (in != null) {
				System.setIn(formerIn);
			}
		}
	}

	@Test
	public void test01() throws IOException {
		String expectedString = new StringBuilder()
				.append(String.format("columnLimit: %s%n", 76))
				.append(String.format("decodingMode: %s%n", false))
				.append(String.format("file: %s%n", (String) null))
				.append(String.format("garbageIgnored: %s%n", false))
				.toString();
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}

	@Test
	public void test02() throws IOException {
		String expectedString = new StringBuilder()
				.append(String.format("columnLimit: %s%n", 10))
				.append(String.format("decodingMode: %s%n", false))
				.append(String.format("file: %s%n", "-"))
				.append(String.format("garbageIgnored: %s%n", false))
				.toString();
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "-w10", "-" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}

	@Test
	public void test03() throws IOException {
		String expectedString = new StringBuilder()
				.append(String.format("columnLimit: %s%n", 100))
				.append(String.format("decodingMode: %s%n", false))
				.append(String.format("file: %s%n", "input.txt"))
				.append(String.format("garbageIgnored: %s%n", false))
				.toString();
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "-w", "100", "input.txt" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}

	@Test
	public void test04() throws IOException {
		String expectedString = new StringBuilder()
				.append(String.format("columnLimit: %s%n", 76))
				.append(String.format("decodingMode: %s%n", true))
				.append(String.format("file: %s%n", "text.txt"))
				.append(String.format("garbageIgnored: %s%n", true))
				.toString();
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "-di", "text.txt" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}

	@Test
	public void test05() throws IOException {
		String expectedString = new StringBuilder()
				.append(String.format("columnLimit: %s%n", 10))
				.append(String.format("decodingMode: %s%n", false))
				.append(String.format("file: %s%n", "-"))
				.append(String.format("garbageIgnored: %s%n", false))
				.toString();
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--wrap=10", "-" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}

	@Test
	public void test06() throws IOException {
		String expectedString = new StringBuilder()
				.append(String.format("columnLimit: %s%n", 100))
				.append(String.format("decodingMode: %s%n", false))
				.append(String.format("file: %s%n", "input.txt"))
				.append(String.format("garbageIgnored: %s%n", false))
				.toString();
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--wrap", "100", "input.txt" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}

	@Test
	public void test07() throws IOException {
		String expectedString = new StringBuilder()
				.append(String.format("columnLimit: %s%n", 76))
				.append(String.format("decodingMode: %s%n", true))
				.append(String.format("file: %s%n", "text.txt"))
				.append(String.format("garbageIgnored: %s%n", true))
				.toString();
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--decode", "--ignore-garbage", "text.txt" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}
	
	@Test
	public void testCustomProgramHelp() throws IOException {
		OptionGroupHelpTextProvider provider1 = OptionGroupHelpTextProvider.getDefault();
		OptionGroupHelpTextProvider provider2 = new SingleLineOptionGroupHelpTextProvider();
		OptionGroupHelpTextProvider.setDefault(provider2);
		try {
			String expectedString = CUSTOM_PROGRAM_HELP;
			ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
			PrintStream out = new PrintStream(bytesOut);
			handle(new String[] { "--help" }, null, null, out);
			String actualString = new String(bytesOut.toByteArray());
			assertEquals(expectedString, actualString);
		} finally {
			OptionGroupHelpTextProvider.setDefault(provider1);
		}
	}

	@Test
	public void testProgramHelp() throws IOException {
		String expectedString = PROGRAM_HELP;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--help" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}

	@Test
	public void testProgramHelpFirst() throws IOException {
		String expectedString = PROGRAM_HELP;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--help", "--version" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}

	@Test
	public void testProgramVersion() throws IOException {
		String expectedString = PROGRAM_VERSION;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--version" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}

	@Test
	public void testProgramVersionFirst() throws IOException {
		String expectedString = PROGRAM_VERSION;
		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
		PrintStream out = new PrintStream(bytesOut);
		handle(new String[] { "--version", "--help" }, null, null, out);
		String actualString = new String(bytesOut.toByteArray());
		assertEquals(expectedString, actualString);
	}

}
