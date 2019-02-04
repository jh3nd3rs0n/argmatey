package argmatey;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
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
			.doc("decode data")
			.optionHelpTextProvider(new OptionHelpTextProvider() {

				@Override
				public String getOptionHelpText(
						final OptionHelpTextParams params) {
					StringBuilder sb = new StringBuilder();
					sb.append("OPTIONS:");
					sb.append(System.getProperty("line.separator"));
					sb.append(DefaultOptionHelpTextProvider.INSTANCE.getOptionHelpText(params));
					return sb.toString();
				}
				
			})
			.otherBuilders(
					new LongOption.Builder("decode"),
					new GnuLongOption.Builder("decode"))
			.build();
	
	private final Option decodeLongOption = 
			this.dPosixOption.getOtherOptions().get(0);
	
	private final Option decodeGnuLongOption = 
			this.dPosixOption.getOtherOptions().get(1);
	
	private final Option iPosixOption = new PosixOption.Builder('i')
			.doc("when decoding, ignore non-alphabet characters")
			.otherBuilders(
					new LongOption.Builder("ignore-garbage"),
					new GnuLongOption.Builder("ignore-garbage"))
			.build(); 
	
	private final Option ignoreGarbageLongOption = 
			this.iPosixOption.getOtherOptions().get(0);
	
	private final Option ignoreGarbageGnuLongOption = 
			this.iPosixOption.getOtherOptions().get(1);
	
	private final Option wPosixOption = new PosixOption.Builder('w')
			.doc("wrap encoded lines after COLS character (default 76)."
					+ this.lineSeparator + "      Use 0 to disable line wrapping")
			.optionArgSpec(new OptionArgSpec.Builder()
					.name("COLS")
					.type(Integer.class)
					.build())
			.otherBuilders(
					new LongOption.Builder("wrap"),
					new GnuLongOption.Builder("wrap"))
			.build(); 
	
	private final Option wrapLongOption = 
			this.wPosixOption.getOtherOptions().get(0);
	
	private final Option wrapGnuLongOption = 
			this.wPosixOption.getOtherOptions().get(1);
	
	private final Option helpGnuLongOption = new GnuLongOption.Builder("help")
			.doc("display this help and exit")
			.special(true)
			.build(); 
	
	private final Option versionGnuLongOption = new GnuLongOption.Builder("version")
			.doc("display version information and exit")
			.optionHelpTextProvider(new OptionHelpTextProvider() {

				@Override
				public String getOptionHelpText(
						final OptionHelpTextParams params) {
					StringBuilder sb = new StringBuilder();
					sb.append(DefaultOptionHelpTextProvider.INSTANCE.getOptionHelpText(params));
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
		
		List<Object> expected = new ArrayList<Object>();
		expected.add(this.dPosixOption);
		expected.add(this.iPosixOption);
		expected.addAll(Arrays.asList(
				this.wPosixOption, this.wPosixOption.newOptionArg("1")));
		expected.add(this.dPosixOption);
		expected.add(this.iPosixOption);
		expected.addAll(Arrays.asList(
				this.wPosixOption, this.wPosixOption.newOptionArg("21")));
		expected.addAll(Arrays.asList(
				this.wPosixOption, this.wPosixOption.newOptionArg("321")));
		expected.add(this.dPosixOption);
		expected.add(this.iPosixOption);
		expected.addAll(Arrays.asList(
				this.wPosixOption, this.wPosixOption.newOptionArg("4321")));
		expected.add(this.decodeLongOption);
		expected.add(this.ignoreGarbageLongOption);
		expected.addAll(Arrays.asList(
				this.wrapLongOption, 
				this.wrapLongOption.newOptionArg("54321")));
		expected.add("file1.txt");
		expected.add("file2.txt");
		expected.add(this.decodeGnuLongOption);
		expected.add(this.ignoreGarbageGnuLongOption);
		expected.addAll(Arrays.asList(
				this.wrapGnuLongOption, 
				this.wrapGnuLongOption.newOptionArg("654321")));
		expected.addAll(Arrays.asList(
				this.wrapGnuLongOption, 
				this.wrapGnuLongOption.newOptionArg("7654321")));
		expected.add(EndOfOptionsDelimiter.INSTANCE);
		expected.add("--help");
		expected.add("--version");
		expected.add("file3.txt");
		
		List<Object> actual = new ArrayList<Object>();
		
		while (argsParser.hasNext()) {
			ParseResult parseResult = argsParser.parseNext();
			actual.addAll(parseResult.getObjectValues());
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
