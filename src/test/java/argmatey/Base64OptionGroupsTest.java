package argmatey;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import argmatey.ArgMatey.ArgsParser;
import argmatey.ArgMatey.DefaultOptionGroupHelpTextProvider;
import argmatey.ArgMatey.EndOfOptionsDelimiter;
import argmatey.ArgMatey.GnuLongOption;
import argmatey.ArgMatey.LongOption;
import argmatey.ArgMatey.Option;
import argmatey.ArgMatey.OptionArgSpec;
import argmatey.ArgMatey.OptionGroup;
import argmatey.ArgMatey.OptionGroupHelpTextParams;
import argmatey.ArgMatey.OptionGroupHelpTextProvider;
import argmatey.ArgMatey.OptionGroups;
import argmatey.ArgMatey.OptionOccurrence;
import argmatey.ArgMatey.ParseResultHolder;
import argmatey.ArgMatey.PosixOption;

public class Base64OptionGroupsTest {
	
	public static final OptionGroup DECODE_OPTION_GROUP = new OptionGroup.Builder(
			new PosixOption.Builder('d')
				.doc("decode data"))
			.optionGroupHelpTextProvider(new OptionGroupHelpTextProvider() {
				
				@Override
				public String getOptionGroupHelpText(
						final OptionGroupHelpTextParams params) {
					StringBuilder sb = new StringBuilder();
					sb.append("OPTIONS:");
					sb.append(System.getProperty("line.separator"));
					OptionGroupHelpTextProvider provider =
							new DefaultOptionGroupHelpTextProvider();
					sb.append(provider.getOptionGroupHelpText(params));
					return sb.toString();
				}
				
			})
			.otherOptionBuilders(
					new LongOption.Builder("decode"),
					new GnuLongOption.Builder("decode"))
			.build();
	
	public static final OptionGroup IGNORE_GARBAGE_OPTION_GROUP = new OptionGroup.Builder(
			new PosixOption.Builder('i')
				.doc("when decoding, ignore non-alphabet characters"))
			.otherOptionBuilders(
					new LongOption.Builder("ignore-garbage"),
					new GnuLongOption.Builder("ignore-garbage"))
			.build();

	public static final OptionGroup WRAP_OPTION_GROUP = new OptionGroup.Builder(
			new PosixOption.Builder('w')
				.doc(String.format(
						"wrap encoded lines after COLS character (default 76)."
						+ "%n      Use 0 to disable line wrapping"))
				.optionArgSpec(new OptionArgSpec.Builder()
						.name("COLS")
						.type(Integer.class)
						.build()))
			.otherOptionBuilders(
					new LongOption.Builder("wrap"),
					new GnuLongOption.Builder("wrap"))
			.build();

	public static final OptionGroup HELP_OPTION_GROUP = new OptionGroup.Builder(
			new GnuLongOption.Builder("help")
				.doc("display this help and exit"))
			.build();

	public static final OptionGroup VERSION_OPTION_GROUP = new OptionGroup.Builder(
			new GnuLongOption.Builder("version")
				.doc("display version information and exit"))
			.optionGroupHelpTextProvider(new OptionGroupHelpTextProvider() {

				@Override
				public String getOptionGroupHelpText(
						final OptionGroupHelpTextParams params) {
					StringBuilder sb = new StringBuilder();
					OptionGroupHelpTextProvider provider =
							new DefaultOptionGroupHelpTextProvider();
					sb.append(provider.getOptionGroupHelpText(params));
					sb.append(System.getProperty("line.separator"));
					return sb.toString();
				}
					
			})
			.build();

	private final String[] args = { 
			"-diw1", "-d", "-i", "-w21", "-w", "321", "-diw", "4321", 
			"-decode", "-ignore-garbage", "-wrap", "54321",
			"file1.txt", "file2.txt",
			"--decode", "--ignore-garbage", "--wrap", "654321", "--wrap=7654321",
			"--", "--help", "--version", "file3.txt"
	};

	private final OptionGroups optionGroups = OptionGroups.newInstance(
			Base64OptionGroupsTest.DECODE_OPTION_GROUP, 
			Base64OptionGroupsTest.IGNORE_GARBAGE_OPTION_GROUP, 
			Base64OptionGroupsTest.WRAP_OPTION_GROUP, 
			Base64OptionGroupsTest.HELP_OPTION_GROUP, 
			Base64OptionGroupsTest.VERSION_OPTION_GROUP);
	
	@Test
	public void testArgs() {
		
		OptionGroup decodeOptionGroup = Base64OptionGroupsTest.DECODE_OPTION_GROUP;
		OptionGroup ignoreGarbageOptionGroup = 
				Base64OptionGroupsTest.IGNORE_GARBAGE_OPTION_GROUP;
		OptionGroup wrapOptionGroup = Base64OptionGroupsTest.WRAP_OPTION_GROUP;
		Option decodePosixOption = decodeOptionGroup.get(0);
		Option decodeLongOption = decodeOptionGroup.get(1);
		Option decodeGnuLongOption = decodeOptionGroup.get(2);
		Option ignoreGarbagePosixOption = ignoreGarbageOptionGroup.get(0);
		Option ignoreGarbageLongOption = ignoreGarbageOptionGroup.get(1);
		Option ignoreGarbageGnuLongOption =	ignoreGarbageOptionGroup.get(2);
		Option wrapPosixOption = wrapOptionGroup.get(0);
		Option wrapLongOption = wrapOptionGroup.get(1);
		Option wrapGnuLongOption = wrapOptionGroup.get(2);
		
		List<Object> expected = new ArrayList<Object>();
		expected.add(new OptionOccurrence(decodePosixOption, null));
		expected.add(new OptionOccurrence(ignoreGarbagePosixOption, null));
		expected.add(new OptionOccurrence(
				wrapPosixOption, wrapPosixOption.newOptionArg("1")));
		expected.add(new OptionOccurrence(decodePosixOption, null));
		expected.add(new OptionOccurrence(ignoreGarbagePosixOption, null));
		expected.add(new OptionOccurrence(
				wrapPosixOption, wrapPosixOption.newOptionArg("21")));
		expected.add(new OptionOccurrence(
				wrapPosixOption, wrapPosixOption.newOptionArg("321")));
		expected.add(new OptionOccurrence(decodePosixOption, null));
		expected.add(new OptionOccurrence(ignoreGarbagePosixOption, null));
		expected.add(new OptionOccurrence(
				wrapPosixOption, wrapPosixOption.newOptionArg("4321")));
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
				this.args, this.optionGroups, false);
		
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
				this.args, this.optionGroups, false);
		
		while (argsParser.hasNext()) {
			argsParser.parseNext();
		}
		
		assertTrue(argsParser.getArgCharIndex() == -1);
	}
	
	@Test
	public void testFinalArgIndex() {
		
		ArgsParser argsParser = ArgsParser.newInstance(
				this.args, this.optionGroups, false);
		
		while (argsParser.hasNext()) {
			argsParser.parseNext();
		}
		
		assertTrue(argsParser.getArgIndex() == this.args.length - 1);
	}
	
	@Test
	public void testOptionGroupsHelpText() {

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
		sw1.write(lineSeparator);
		sw1.flush();
		
		StringWriter sw2 = new StringWriter();
		PrintWriter pw = new PrintWriter(sw2);
		this.optionGroups.printHelpText(pw);
		pw.flush();
		sw2.flush();
		
		assertEquals(sw1.toString(), sw2.toString());
	}

}
