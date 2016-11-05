

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import argmatey.ArgParser;
import argmatey.GnuLongOption;
import argmatey.LongOption;
import argmatey.OptionArgSpec;
import argmatey.OptionOccurrence;
import argmatey.Options;
import argmatey.ParseResult;
import argmatey.PosixOption;

public class Base64OptionsTest {

	@Test
	public void test() {
		
		String lineSeparator = System.getProperty("line.separator");
		
		Options options = new Options(
				new PosixOption.Builder('d')
					.builders(
							new LongOption.Builder("decode"),
							new GnuLongOption.Builder("decode"))
					.doc("decode data")
					.preHelpText("OPTIONS:")
					.build(),
				new PosixOption.Builder('i')
					.builders(
							new LongOption.Builder("ignore-garbage"),
							new GnuLongOption.Builder("ignore-garbage"))
					.doc("when decoding, ignore non-alphabet characters")
					.build(),
				new PosixOption.Builder('w')
					.builders(
							new LongOption.Builder("wrap"),
							new GnuLongOption.Builder("wrap"))
					.doc("wrap encoded lines after COLS character (default 76)." 
							+ lineSeparator 
							+ "Use 0 to disable line wrapping")
					.optionArgSpec(new OptionArgSpec.Builder()
							.name("COLS")
							.type(Integer.class)
							.build())
					.build(),
				new GnuLongOption.Builder("help")
					.doc("display this help and exit")
					.special(true)
					.build(),
				new GnuLongOption.Builder("version")
					.doc("display version information and exit")
					.postHelpText(lineSeparator)
					.special(true)
					.build());
		
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
		PrintWriter pw1 = new PrintWriter(sw2);
		options.printHelpText(pw1);
		pw1.flush();
		sw2.flush();
		
		assertEquals(sw1.toString(), sw2.toString());
		
		StringWriter sw3 = new StringWriter();
		sw3.write("[-d] [-i] [-w COLS]");
		sw3.flush();
		
		StringWriter sw4 = new StringWriter();
		PrintWriter pw2 = new PrintWriter(sw4);
		options.printUsage(pw2);
		pw2.flush();
		sw4.flush();
		
		assertEquals(sw3.toString(), sw4.toString());
		
		String[] args = { 
				"-diw1", "-d", "-i", "-w21", "-w", "321", "-diw", "4321", 
				"-decode", "-ignore-garbage", "-wrap", "54321",
				"file1.txt", "file2.txt",
				"--decode", "--ignore-garbage", "--wrap", "654321", "--wrap=7654321",
				"--", "--help", "--version", "file3.txt"
		};
		
		ArgParser argParser = new ArgParser(args, options, false);
		
		String[] expectedArgs = { 
				"OptionOccurrence [option=-d, optionArg=null]", 
				"OptionOccurrence [option=-i, optionArg=null]", 
				"OptionOccurrence [option=-w, optionArg=1]", 
				"OptionOccurrence [option=-d, optionArg=null]", 
				"OptionOccurrence [option=-i, optionArg=null]", 
				"OptionOccurrence [option=-w, optionArg=21]", 
				"OptionOccurrence [option=-w, optionArg=321]", 
				"OptionOccurrence [option=-d, optionArg=null]", 
				"OptionOccurrence [option=-i, optionArg=null]", 
				"OptionOccurrence [option=-w, optionArg=4321]", 
				"OptionOccurrence [option=-decode, optionArg=null]", 
				"OptionOccurrence [option=-ignore-garbage, optionArg=null]", 
				"OptionOccurrence [option=-wrap, optionArg=54321]",
				"file1.txt", "file2.txt",
				"OptionOccurrence [option=--decode, optionArg=null]", 
				"OptionOccurrence [option=--ignore-garbage, optionArg=null]", 
				"OptionOccurrence [option=--wrap, optionArg=654321]", 
				"OptionOccurrence [option=--wrap, optionArg=7654321]",
				"--", "--help", "--version", "file3.txt"
		};
		
		List<Integer> expectedWrapOptionArgsList = new ArrayList<Integer>();
		expectedWrapOptionArgsList.add(Integer.valueOf("1"));
		expectedWrapOptionArgsList.add(Integer.valueOf("21"));
		expectedWrapOptionArgsList.add(Integer.valueOf("321"));
		expectedWrapOptionArgsList.add(Integer.valueOf("4321"));
		expectedWrapOptionArgsList.add(Integer.valueOf("54321"));
		expectedWrapOptionArgsList.add(Integer.valueOf("654321"));
		expectedWrapOptionArgsList.add(Integer.valueOf("7654321"));
		
		List<String> actualArgsList = new ArrayList<String>();
		List<Integer> actualWrapOptionArgsList = new ArrayList<Integer>();
		
		while (argParser.hasNext()) {
			ParseResult parseResult = argParser.parseNext();
			actualArgsList.add(parseResult.asObjectValue().toString());
			if (!parseResult.isOptionOccurrence()) {
				continue;
			}
			OptionOccurrence optionOccurrence = parseResult.asOptionOccurrence();
			if (optionOccurrence.hasOptionOfAnyOf("-w", "-wrap", "--wrap")) {
				actualWrapOptionArgsList.add(
						optionOccurrence.getOptionArg().asTypeValue(Integer.class));
			}
		}
		
		String[] actualArgs = actualArgsList.toArray(new String[actualArgsList.size()]);
		
		assertArrayEquals(expectedArgs, actualArgs);
		
		assertEquals(expectedWrapOptionArgsList, actualWrapOptionArgsList);
		
		assertTrue(argParser.getArgIndex() == args.length - 1);
		assertTrue(argParser.getArgCharIndex() == -1);
	}

}
