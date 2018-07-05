package argmatey;


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
import argmatey.Options;
import argmatey.ParseResult;
import argmatey.PosixOption;

public class Base64OptionsTest {

	private final String[] args = { 
			"-diw1", "-d", "-i", "-w21", "-w", "321", "-diw", "4321", 
			"-decode", "-ignore-garbage", "-wrap", "54321",
			"file1.txt", "file2.txt",
			"--decode", "--ignore-garbage", "--wrap", "654321", "--wrap=7654321",
			"--", "--help", "--version", "file3.txt"
	};
	
	private final String lineSeparator = System.getProperty("line.separator");
	
	private final Options options = new Options(
			new PosixOption.Builder('d')
				.beforeHelpText("OPTIONS:")
				.builders(
						new LongOption.Builder("decode"),
						new GnuLongOption.Builder("decode"))
				.doc("decode data")
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
						+ this.lineSeparator + "Use 0 to disable line wrapping")
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
				.afterHelpText(this.lineSeparator)
				.doc("display version information and exit")
				.special(true)
				.build()); 
	
	@Test
	public void testArgs() {
		
		ArgParser argParser = new ArgParser(this.args, this.options, false);
		
		String[] expectedArgs = { 
				"-d", "-i",	"-w", "1", "-d", "-i", "-w", "21", "-w", "321", "-d", "-i", "-w", "4321",
				"-decode", "-ignore-garbage", "-wrap", "54321",
				"file1.txt", "file2.txt",
				"--decode", "--ignore-garbage", "--wrap", "654321", "--wrap", "7654321",
				"--", "--help", "--version", "file3.txt"
		};
		
		List<String> actualArgsList = new ArrayList<String>();
		
		while (argParser.hasNext()) {
			ParseResult parseResult = argParser.parseNext();
			for (Object objectValue : parseResult.toObjectValues()) {
				actualArgsList.add(objectValue.toString());
			}			
		}
		
		String[] actualArgs = actualArgsList.toArray(new String[actualArgsList.size()]);
		
		assertArrayEquals(expectedArgs, actualArgs);
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
	
	@Test
	public void testWrapOptionArgs() {

		ArgParser argParser = new ArgParser(this.args, this.options, false);
		
		List<Integer> expectedWrapOptionArgsList = new ArrayList<Integer>();
		expectedWrapOptionArgsList.add(Integer.valueOf("1"));
		expectedWrapOptionArgsList.add(Integer.valueOf("21"));
		expectedWrapOptionArgsList.add(Integer.valueOf("321"));
		expectedWrapOptionArgsList.add(Integer.valueOf("4321"));
		expectedWrapOptionArgsList.add(Integer.valueOf("54321"));
		expectedWrapOptionArgsList.add(Integer.valueOf("654321"));
		expectedWrapOptionArgsList.add(Integer.valueOf("7654321"));
		
		List<Integer> actualWrapOptionArgsList = new ArrayList<Integer>();
		
		while (argParser.hasNext()) {
			ParseResult parseResult = argParser.parseNext();
			if (parseResult.isOptionOfAnyOf("-w", "-wrap", "--wrap")) {
				actualWrapOptionArgsList.add(
						parseResult.getOptionArg().toTypeValue(Integer.class));
			}
		}
		
		assertEquals(expectedWrapOptionArgsList, actualWrapOptionArgsList);
	}

}
