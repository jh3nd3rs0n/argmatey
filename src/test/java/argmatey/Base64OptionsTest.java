package argmatey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Base64OptionsTest {

	public static final class Base64Options {
		
		public static final Option DECODE_OPTION = new PosixOption.Builder('d')
				.doc("decode data")
				.optionHelpTextProvider(new OptionHelpTextProvider() {

					@Override
					public String getOptionHelpText(
							final OptionHelpTextParams params) {
						StringBuilder sb = new StringBuilder();
						sb.append("OPTIONS:");
						sb.append(System.getProperty("line.separator"));
						OptionHelpTextProvider provider =
								DefaultOptionHelpTextProvider.INSTANCE;
						sb.append(provider.getOptionHelpText(params));
						return sb.toString();
					}
					
				})
				.ordinal(0)
				.otherBuilders(
						new LongOption.Builder("decode"),
						new GnuLongOption.Builder("decode"))
				.build();
		
		public static final Option IGNORE_GARBAGE_OPTION = 
				new PosixOption.Builder('i')
				.doc("when decoding, ignore non-alphabet characters")
				.ordinal(1)
				.otherBuilders(
						new LongOption.Builder("ignore-garbage"),
						new GnuLongOption.Builder("ignore-garbage"))
				.build(); 
		
		public static final Option WRAP_OPTION = new PosixOption.Builder('w')
				.doc(String.format(
						"wrap encoded lines after COLS character (default 76)."
						+ "%n      Use 0 to disable line wrapping"))
				.optionArgSpec(new OptionArgSpec.Builder()
						.name("COLS")
						.type(Integer.class)
						.build())
				.ordinal(2)
				.otherBuilders(
						new LongOption.Builder("wrap"),
						new GnuLongOption.Builder("wrap"))
				.build(); 
		
		public static final Option HELP_OPTION = new GnuLongOption.Builder(
				"help")
				.doc("display this help and exit")
				.ordinal(3)
				.special(true)
				.build(); 
		
		public static final Option VERSION_OPTION = new GnuLongOption.Builder(
				"version")
				.doc("display version information and exit")
				.optionHelpTextProvider(new OptionHelpTextProvider() {

					@Override
					public String getOptionHelpText(
							final OptionHelpTextParams params) {
						StringBuilder sb = new StringBuilder();
						OptionHelpTextProvider provider =
								DefaultOptionHelpTextProvider.INSTANCE;
						sb.append(provider.getOptionHelpText(params));
						sb.append(System.getProperty("line.separator"));
						return sb.toString();
					}
					
				})
				.ordinal(4)
				.special(true)
				.build(); 
		
		private Base64Options() { }
		
	}
	
	private final String[] args = { 
			"-diw1", "-d", "-i", "-w21", "-w", "321", "-diw", "4321", 
			"-decode", "-ignore-garbage", "-wrap", "54321",
			"file1.txt", "file2.txt",
			"--decode", "--ignore-garbage", "--wrap", "654321", "--wrap=7654321",
			"--", "--help", "--version", "file3.txt"
	};
	
	private final Options options = Options.newInstance(Base64Options.class);
	
	@Test
	public void testArgs() {
		
		Option decodeOption = Base64Options.DECODE_OPTION;
		Option ignoreGarbageOption = Base64Options.IGNORE_GARBAGE_OPTION;
		Option wrapOption = Base64Options.WRAP_OPTION;
		Option decodeLongOption = decodeOption.getOtherOptions().get(0);
		Option ignoreGarbageLongOption = 
				ignoreGarbageOption.getOtherOptions().get(0);
		Option wrapLongOption = wrapOption.getOtherOptions().get(0);
		Option decodeGnuLongOption = decodeOption.getOtherOptions().get(1);
		Option ignoreGarbageGnuLongOption =
				ignoreGarbageOption.getOtherOptions().get(1);
		Option wrapGnuLongOption = wrapOption.getOtherOptions().get(1);
		
		List<Object> expected = new ArrayList<Object>();
		expected.add(new OptionOccurrence(decodeOption, null));
		expected.add(new OptionOccurrence(ignoreGarbageOption, null));
		expected.add(new OptionOccurrence(
				wrapOption, wrapOption.newOptionArg("1")));
		expected.add(new OptionOccurrence(decodeOption, null));
		expected.add(new OptionOccurrence(ignoreGarbageOption, null));
		expected.add(new OptionOccurrence(
				wrapOption, wrapOption.newOptionArg("21")));
		expected.add(new OptionOccurrence(
				wrapOption, wrapOption.newOptionArg("321")));
		expected.add(new OptionOccurrence(decodeOption, null));
		expected.add(new OptionOccurrence(ignoreGarbageOption, null));
		expected.add(new OptionOccurrence(
				wrapOption, wrapOption.newOptionArg("4321")));
		expected.add(new OptionOccurrence(decodeLongOption, null));
		expected.add(new OptionOccurrence(ignoreGarbageLongOption, null));
		expected.add(new OptionOccurrence(
				wrapLongOption, wrapLongOption.newOptionArg("54321")));
		expected.add("file1.txt");
		expected.add("file2.txt");
		expected.add(new OptionOccurrence(decodeGnuLongOption, null));
		expected.add(new OptionOccurrence(ignoreGarbageGnuLongOption, null));
		expected.add(new OptionOccurrence(
				wrapGnuLongOption, wrapGnuLongOption.newOptionArg("654321")));
		expected.add(new OptionOccurrence(
				wrapGnuLongOption, wrapGnuLongOption.newOptionArg("7654321")));
		expected.add(EndOfOptionsDelimiter.INSTANCE);
		expected.add("--help");
		expected.add("--version");
		expected.add("file3.txt");
		
		ArgsParser argsParser = ArgsParser.newInstance(
				this.args, this.options, false);
		
		List<Object> actual = new ArrayList<Object>();
		
		while (argsParser.hasNext()) {
			ParseResultHolder parseResultHolder = argsParser.parseNext();
			actual.add(parseResultHolder.getParseResult());
		}
		
		assertEquals(expected, actual);
	}
	
	@Test
	public void testFinalArgCharIndex() {
		
		ArgsParser argsParser = ArgsParser.newInstance(
				this.args, this.options, false);
		
		while (argsParser.hasNext()) {
			argsParser.parseNext();
		}
		
		assertTrue(argsParser.getArgCharIndex() == -1);
	}
	
	@Test
	public void testFinalArgIndex() {
		
		ArgsParser argsParser = ArgsParser.newInstance(
				this.args, this.options, false);
		
		while (argsParser.hasNext()) {
			argsParser.parseNext();
		}
		
		assertTrue(argsParser.getArgIndex() == this.args.length - 1);
	}
	
	@Test
	public void testOptionsHelpText() {

		String lineSeparator = System.getProperty("line.separator");
		
		StringWriter sw1 = new StringWriter();
		sw1.write("OPTIONS:");
		sw1.write(lineSeparator);
		sw1.write("  -d, -decode, --decode");
		sw1.write(lineSeparator);
		sw1.write("      decode data");
		sw1.write(lineSeparator);
		sw1.write("  -i, -ignore-garbage, --ignore-garbage");
		sw1.write(lineSeparator);
		sw1.write("      when decoding, ignore non-alphabet characters");
		sw1.write(lineSeparator);
		sw1.write("  -w COLS, -wrap COLS, --wrap=COLS");
		sw1.write(lineSeparator);
		sw1.write("      wrap encoded lines after COLS character (default 76).");
		sw1.write(lineSeparator);
		sw1.write("      Use 0 to disable line wrapping");
		sw1.write(lineSeparator);
		sw1.write("  --help");
		sw1.write(lineSeparator);
		sw1.write("      display this help and exit");
		sw1.write(lineSeparator);
		sw1.write("  --version");
		sw1.write(lineSeparator);
		sw1.write("      display version information and exit");
		sw1.write(lineSeparator);
		sw1.flush();
		
		StringWriter sw2 = new StringWriter();
		PrintWriter pw = new PrintWriter(sw2);
		this.options.printHelpText(pw);
		pw.flush();
		sw2.flush();
		
		assertEquals(sw1.toString(), sw2.toString());
	}
	
	@Test
	public void testOptionsUsage() {
		
		StringWriter sw1 = new StringWriter();
		sw1.write("[-d] [-i] [-w COLS]");
		sw1.flush();
		
		StringWriter sw2 = new StringWriter();
		PrintWriter pw = new PrintWriter(sw2);
		this.options.printUsage(pw);
		pw.flush();
		sw2.flush();
		
		assertEquals(sw1.toString(), sw2.toString());		
	}

}
