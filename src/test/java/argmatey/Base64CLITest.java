package argmatey;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;

import org.junit.Test;

import argmatey.ArgMatey.Annotations.NonparsedArg;
import argmatey.ArgMatey.Annotations.Option;
import argmatey.ArgMatey.Annotations.OptionArgSpec;
import argmatey.ArgMatey.Annotations.OptionGroup;
import argmatey.ArgMatey.CLI;
import argmatey.ArgMatey.DefaultOptionGroupHelpTextProvider;
import argmatey.ArgMatey.GnuLongOption;
import argmatey.ArgMatey.OptionGroupHelpTextParams;
import argmatey.ArgMatey.OptionGroupHelpTextProvider;
import argmatey.ArgMatey.PosixOption;

public class Base64CLITest {
    
    public static class Base64CLI extends CLI {
        
        public Base64CLI(String[] args, boolean posixlyCorrect) {
            super(args, posixlyCorrect);
        }
        
        @OptionGroup(
            option = @Option(
                doc = "Wrap encoded lines after COLS character",
                name = "w",
                optionArgSpec = @OptionArgSpec(
                    name = "COLS"
                ),
                type = PosixOption.class
            ),
            otherOptions = {
                @Option(
                    name = "wrap", 
                    type = GnuLongOption.class
                )
            } 
        )
        public void setColumnLimit(Integer i) { 
        }
        
        @OptionGroup(
            option = @Option(
                doc = "Decode data",
                name = "d",
                type = PosixOption.class
            ),
            otherOptions = {
                @Option(
                    name = "decode", 
                    type = GnuLongOption.class
                )
            }
        )
        public void setDecodingMode(boolean b) {
        }
        
        @NonparsedArg
        public void setFile(String s) {
        }
        
        @OptionGroup(
            option = @Option(
                doc = "When decoding, ignore non-alphabet characters",
                name = "i",
                type = PosixOption.class
            ),
            otherOptions = {
                @Option(
                    name = "ignore-garbage", 
                    type = GnuLongOption.class
                )
            } 
        )
        public void setGarbageIgnored(boolean b) {
        }
        
    }
	
	
    public static class CustomOptionGroupHelpTextProvider 
	    extends OptionGroupHelpTextProvider {
	
	    public String getOptionGroupHelpText(OptionGroupHelpTextParams params) {
	        Iterator<ArgMatey.Option> iterator = 
	        		params.getDisplayableOptions().iterator();
	        StringBuilder sb = new StringBuilder();
	        String doc = null;
	        sb.append("  ");
	        while (iterator.hasNext()) {
	            ArgMatey.Option option = iterator.next();
	            sb.append(option.getUsage());
	            if (iterator.hasNext()) {
	                sb.append(", ");
	            }
	            if (doc == null) {
	                doc = option.getDoc();
	            }
	        }
	        final int docStart = 24;
	        int length = sb.length();
	        for (int i = 0; i < docStart - length; i++) {
	        	sb.append(' ');
	        }
	        sb.append(doc);
	        return sb.toString();
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
		sb1.append(String.format("  -w COLS, --wrap=COLS  Wrap encoded lines after COLS character%n"));
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
		sb2.append(String.format("      Wrap encoded lines after COLS character%n"));
		sb2.append(String.format("%n"));		
		PROGRAM_HELP = sb2.toString();
		PROGRAM_VERSION = String.format("base64 1.0%n");
	}
	
	private static void handle(
			final String[] args, 
			final PrintStream err, 
			final InputStream in,
			final PrintStream out) throws IOException {
		PrintStream formerErr = System.err;
		InputStream formerIn = System.in;
		PrintStream formerOut = System.out;
		if (err != null) { System.setErr(err); }
		if (out != null) { System.setOut(out); }
		if (in != null) { System.setIn(in); } 
		try {
			main(args);
		} finally {
			if (err != null) { System.setErr(formerErr); }
			if (out != null) { System.setOut(formerOut); }
			if (in != null) { System.setIn(formerIn); }
		}
	}
	
	private static void main(String[] args) {
        Base64CLI base64CLI = new Base64CLI(args, false);
        base64CLI.setProgramName("base64");
        base64CLI.setProgramVersion("base64 1.0");
        base64CLI.setProgramArgsUsage(" [FILE]");
        StringBuilder sb = new StringBuilder();
        sb.append("Base64 encode or decode FILE, or standard input, to standard output.");
        sb.append(String.format("%n%n"));
        sb.append("With no FILE, or when FILE is -, read standard input.");
        base64CLI.setProgramDoc(sb.toString());
        while (base64CLI.hasNext()) {
            base64CLI.handleNext();
            if (base64CLI.isProgramHelpDisplayed()
                || base64CLI.isProgramVersionDisplayed()) {
                return;
            }
        }
    }

	@Test
	public void testCustomProgramHelp() throws IOException {
		OptionGroupHelpTextProvider provider = new CustomOptionGroupHelpTextProvider();
        OptionGroupHelpTextProvider.setDefault(provider);
        try {
        	String expectedString = CUSTOM_PROGRAM_HELP;
    		ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
    		PrintStream out = new PrintStream(bytesOut);
    		handle(new String[] { "--help" }, null, null, out);
    		String actualString = new String(bytesOut.toByteArray());
    		assertEquals(expectedString, actualString);
        } finally {
        	OptionGroupHelpTextProvider.setDefault(new DefaultOptionGroupHelpTextProvider());
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
