package argmatey;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class Base64OptionsTest {

	private final String[] args = { 
			"-diw1", "-d", "-i", "-w21", "-w", "321", "-diw", "4321", 
			"-decode", "-ignore-garbage", "-wrap", "54321",
			"file1.txt", "file2.txt",
			"--decode", "--ignore-garbage", "--wrap", "654321", "--wrap=7654321",
			"--", "--help", "--version", "file3.txt"
	};
	
	private final String lineSeparator = System.getProperty("line.separator");
	
	private final Option dPosixOption = new PosixOption.Builder('d')
			.builders(
					new LongOption.Builder("decode"),
					new GnuLongOption.Builder("decode"))
			.doc("decode data")
			.optionHelpTextProvider(new OptionHelpTextProvider() {

				@Override
				public String getOptionHelpText(final Option option) {
					StringBuilder sb = new StringBuilder();
					sb.append("OPTIONS:");
					sb.append(System.getProperty("line.separator"));
					sb.append(DefaultOptionHelpTextProvider.INSTANCE.getOptionHelpText(option));
					return sb.toString();
				}
				
			})
			.build();
	
	private final Option decodeLongOption = this.dPosixOption.getOptions().get(0);
	
	private final Option decodeGnuLongOption = this.dPosixOption.getOptions().get(1);
	
	private final Option iPosixOption = new PosixOption.Builder('i')
			.builders(
					new LongOption.Builder("ignore-garbage"),
					new GnuLongOption.Builder("ignore-garbage"))
			.doc("when decoding, ignore non-alphabet characters")
			.build(); 
	
	private final Option ignoreGarbageLongOption = this.iPosixOption.getOptions().get(0);
	
	private final Option ignoreGarbageGnuLongOption = this.iPosixOption.getOptions().get(1);
	
	private final Option wPosixOption = new PosixOption.Builder('w')
			.builders(
					new LongOption.Builder("wrap"),
					new GnuLongOption.Builder("wrap"))
			.doc("wrap encoded lines after COLS character (default 76)."
					+ this.lineSeparator + "      Use 0 to disable line wrapping")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("COLS")
					.type(Integer.class)
					.build())
			.build(); 
	
	private final Option wrapLongOption = this.wPosixOption.getOptions().get(0);
	
	private final Option wrapGnuLongOption = this.wPosixOption.getOptions().get(1);
	
	private final Option helpGnuLongOption = new GnuLongOption.Builder("help")
			.doc("display this help and exit")
			.special(true)
			.build(); 
	
	private final Option versionGnuLongOption = new GnuLongOption.Builder("version")
			.doc("display version information and exit")
			.optionHelpTextProvider(new OptionHelpTextProvider() {

				@Override
				public String getOptionHelpText(final Option option) {
					StringBuilder sb = new StringBuilder();
					sb.append(DefaultOptionHelpTextProvider.INSTANCE.getOptionHelpText(option));
					sb.append(System.getProperty("line.separator"));
					return sb.toString();
				}
				
			})
			.special(true)
			.build(); 
	
	private final Options options = new Options(
			this.dPosixOption,
			this.iPosixOption,
			this.wPosixOption,
			this.helpGnuLongOption,
			this.versionGnuLongOption); 
	
	@Test
	public void testArgs() {
		
		ArgsParser argsParser = ArgsParser.newInstance(
				this.args, this.options, false);
		
		List<ParseResult> expected = new ArrayList<ParseResult>();
		expected.add(new OptionOccurrence(this.dPosixOption, null));
		expected.add(new OptionOccurrence(this.iPosixOption, null));
		expected.add(new OptionOccurrence(
				this.wPosixOption, this.wPosixOption.newOptionArg("1")));
		expected.add(new OptionOccurrence(this.dPosixOption, null));
		expected.add(new OptionOccurrence(this.iPosixOption, null));
		expected.add(new OptionOccurrence(
				this.wPosixOption, this.wPosixOption.newOptionArg("21")));
		expected.add(new OptionOccurrence(
				this.wPosixOption, this.wPosixOption.newOptionArg("321")));
		expected.add(new OptionOccurrence(this.dPosixOption, null));
		expected.add(new OptionOccurrence(this.iPosixOption, null));
		expected.add(new OptionOccurrence(
				this.wPosixOption, this.wPosixOption.newOptionArg("4321")));
		expected.add(new OptionOccurrence(this.decodeLongOption, null));
		expected.add(new OptionOccurrence(this.ignoreGarbageLongOption,	null));
		expected.add(new OptionOccurrence(
				this.wrapLongOption, 
				this.wrapLongOption.newOptionArg("54321")));
		expected.add(new NonparsedArg("file1.txt"));
		expected.add(new NonparsedArg("file2.txt"));
		expected.add(new OptionOccurrence(this.decodeGnuLongOption,	null));
		expected.add(new OptionOccurrence(
				this.ignoreGarbageGnuLongOption, null));
		expected.add(new OptionOccurrence(
				this.wrapGnuLongOption, 
				this.wrapGnuLongOption.newOptionArg("654321")));
		expected.add(new OptionOccurrence(
				this.wrapGnuLongOption, 
				this.wrapGnuLongOption.newOptionArg("7654321")));
		expected.add(EndOfOptionsDelimiter.INSTANCE);
		expected.add(new NonparsedArg("--help"));
		expected.add(new NonparsedArg("--version"));
		expected.add(new NonparsedArg("file3.txt"));
		
		List<ParseResult> actual = new ArrayList<ParseResult>();
		
		while (argsParser.hasNext()) {
			ParseResult parseResult = argsParser.parseNext();
			actual.add(parseResult);
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
		
		StringWriter sw1 = new StringWriter();
		sw1.write("OPTIONS:");
		sw1.write(this.lineSeparator);
		sw1.write("  -d, -decode, --decode");
		sw1.write(this.lineSeparator);
		sw1.write("      decode data");
		sw1.write(this.lineSeparator);
		sw1.write("  -i, -ignore-garbage, --ignore-garbage");
		sw1.write(this.lineSeparator);
		sw1.write("      when decoding, ignore non-alphabet characters");
		sw1.write(this.lineSeparator);
		sw1.write("  -w COLS, -wrap COLS, --wrap=COLS");
		sw1.write(this.lineSeparator);
		sw1.write("      wrap encoded lines after COLS character (default 76).");
		sw1.write(this.lineSeparator);
		sw1.write("      Use 0 to disable line wrapping");
		sw1.write(this.lineSeparator);
		sw1.write("  --help");
		sw1.write(this.lineSeparator);
		sw1.write("      display this help and exit");
		sw1.write(this.lineSeparator);
		sw1.write("  --version");
		sw1.write(this.lineSeparator);
		sw1.write("      display version information and exit");
		sw1.write(this.lineSeparator);
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
