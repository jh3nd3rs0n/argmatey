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
					sb.append(option.getSelfProvidedHelpText());
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
	
	private final OptionArgSpec colsOptionArgSpec = new OptionArgSpec.Builder()
			.name("COLS")
			.type(Integer.class)
			.build(); 
	
	private final Option wPosixOption = new PosixOption.Builder('w')
			.builders(
					new LongOption.Builder("wrap"),
					new GnuLongOption.Builder("wrap"))
			.doc("wrap encoded lines after COLS character (default 76)."
					+ this.lineSeparator + "      Use 0 to disable line wrapping")
			.optionArgSpec(this.colsOptionArgSpec)
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
					sb.append(option.getSelfProvidedHelpText());
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
		
		ArgParser argParser = new ArgParser(this.args, this.options, false);
		
		List<Object> expectedObjectValues = new ArrayList<Object>();
		expectedObjectValues.add(this.dPosixOption);
		expectedObjectValues.add(this.iPosixOption);
		expectedObjectValues.add(this.wPosixOption);
		expectedObjectValues.add(this.colsOptionArgSpec.newOptionArg("1"));
		expectedObjectValues.add(this.dPosixOption);
		expectedObjectValues.add(this.iPosixOption);
		expectedObjectValues.add(this.wPosixOption);
		expectedObjectValues.add(this.colsOptionArgSpec.newOptionArg("21"));
		expectedObjectValues.add(this.wPosixOption);
		expectedObjectValues.add(this.colsOptionArgSpec.newOptionArg("321"));
		expectedObjectValues.add(this.dPosixOption);
		expectedObjectValues.add(this.iPosixOption);
		expectedObjectValues.add(this.wPosixOption);
		expectedObjectValues.add(this.colsOptionArgSpec.newOptionArg("4321"));
		expectedObjectValues.add(this.decodeLongOption);
		expectedObjectValues.add(this.ignoreGarbageLongOption);
		expectedObjectValues.add(this.wrapLongOption);
		expectedObjectValues.add(this.colsOptionArgSpec.newOptionArg("54321"));
		expectedObjectValues.add("file1.txt");
		expectedObjectValues.add("file2.txt");
		expectedObjectValues.add(this.decodeGnuLongOption);
		expectedObjectValues.add(this.ignoreGarbageGnuLongOption);
		expectedObjectValues.add(this.wrapGnuLongOption);
		expectedObjectValues.add(this.colsOptionArgSpec.newOptionArg("654321"));
		expectedObjectValues.add(this.wrapGnuLongOption);
		expectedObjectValues.add(this.colsOptionArgSpec.newOptionArg("7654321"));
		expectedObjectValues.add(EndOfOptionsDelimiter.INSTANCE);
		expectedObjectValues.add("--help");
		expectedObjectValues.add("--version");
		expectedObjectValues.add("file3.txt");
		
		List<Object> actualObjectValues = new ArrayList<Object>();
		
		while (argParser.hasNext()) {
			ParseResult parseResult = argParser.parseNext();
			for (Object objectValue : parseResult.toObjectValues()) {
				actualObjectValues.add(objectValue);
			}			
		}
		
		assertEquals(expectedObjectValues, actualObjectValues);
	}
	
	@Test
	public void testFinalArgCharIndex() {
		
		ArgParser argParser = new ArgParser(this.args, this.options, false);
		
		while (argParser.hasNext()) {
			argParser.parseNext();
		}
		
		assertTrue(argParser.getArgCharIndex() == -1);
	}
	
	@Test
	public void testFinalArgIndex() {
		
		ArgParser argParser = new ArgParser(this.args, this.options, false);
		
		while (argParser.hasNext()) {
			argParser.parseNext();
		}
		
		assertTrue(argParser.getArgIndex() == this.args.length - 1);
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
