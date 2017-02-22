#ArgMatey

ArgMatey is a simple yet comprehensive Java command line argument parser framework that has the following features:

- API terminology and option syntax and behavior based on the [POSIX Utility Conventions](http://pubs.opengroup.org/onlinepubs/9699919799/basedefs/V1_chap12.html), GNU's function [getopt_long()](http://www.gnu.org/software/libc/manual/html_node/Getopt-Long-Options.html#Getopt-Long-Options), and GNU's [Argp](http://www.gnu.org/software/libc/manual/html_node/Argp.html#Argp) interface
 
- Option types:
 
  - POSIX options (examples: `-h -v -o file.txt`)
    
  - Long options (examples: `-help -version -output-file file.txt`)
    
  - GNU long options (examples: `--help --version --output-file=file.txt`)
     
- Incremental command line argument parsing (similar to getopt(), getopt_long(), and Argp). This is useful for interpreting the following:

  - Multiple instances of the same option
  
  - Multiple instances of options from the same group
  
  - The order of the provided options and arguments 
 
- Complete customization of usage and help text for the options. This is useful for retaining the format of the usage and help text generated from an earlier command line argument parsing library.

## Example

<pre>

import argmatey.ArgParser;
import argmatey.GnuLongOption;
import argmatey.LongOption;
import argmatey.Option;
import argmatey.OptionArgSpec;
import argmatey.OptionHelpTextProvider;
import argmatey.Options;
import argmatey.OptionUsageProvider;
import argmatey.ParseResult;
import argmatey.PosixOption;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class Greet {
	private static enum Language { EN, ES, FR }
	public static void main(String[] args) {
		Option helpOption = new PosixOption.Builder("h") // POSIX option
			.builders(new GnuLongOption.Builder("help")) // GNU long option
			.doc("Print this help and exit")
			.special(true) // Does not appear in the usage of the options
			.build();
		Option languagesOption = new PosixOption.Builder("l")
			.builders(
				new LongOption.Builder("lang"), // Long option
				new GnuLongOption.Builder("languages")
					// Customization of an option's usage
					.optionUsageProvider(new OptionUsageProvider() {
						@Override
						public String getOptionUsage(Option option) {
							String u = String.format(
								"%1$s=%2$s1[%3$s%2$s2]...",
								option,
								option.getOptionArgSpec().getName(),
								option.getOptionArgSpec().getSeparator());
							return u;
						} 
					}))
			.doc("Language(s) for the greeting(s).")
			.optionArgSpec(
				new OptionArgSpec.Builder()
					.name("LANGUAGE")
					.separator(",")
					.type(Language.class)
					.build())
			// Customization of an option's help text
			.optionHelpTextProvider(new OptionHelpTextProvider() {
				@Override
				public String getOptionHelpText(Option option) {
					StringBuilder sb = new StringBuilder();
					sb.append("  ");
					Iterator&lt;Option&gt; it = option.getAllOptions().iterator();
					while (it.hasNext()) {
						Option opt = it.next();
						if (it.hasNext()) {
							// Print just the option itself 
							sb.append(option.toString());
							sb.append(", ");
						} else {
							/* 
							Print the usage of the last option of the group 
							(--languages)
							*/
							sb.append(option.getUsage());
						}
					}
					sb.append(System.getProperty("line.separator"));
					sb.append("      ");
					sb.append(option.getDoc());
					return sb.toString();
				}
			})
			.build();
		Options options = new Options(helpOption, languagesOption);
		ArgParser argParser = new ArgParser(args, options, false);
		Set&lt;Language&gt; languages = EnumSet.of(Language.EN);
		List&lt;String&gt; names = new ArrayList&lt;String&gt;();
		// Incremental parsing
		while (argParser.hasNext()) {
			ParseResult parseResult = argParser.parseNext();
			if (parseResult.isOptionOfAnyOf("-h", "--help")) {
				System.out.print("Usage: Greet ");
				argParser.getOptions().printUsage();
				System.out.println(" NAME(S)");
				System.out.println();
				System.out.println("OPTIONS:");
				argParser.getOptions().printHelpText();
				System.out.println();
				System.out.println();
				return;
			}
			if (parseResult.isOptionOfAnyOf("-l", "-lang", "--languages")) {
				languages = EnumSet.copyOf(
					parseResult.getOptionArg().asTypeValues(Language.class));
			}
			if (parseResult.isNonparsedArg()) {
				names.add(parseResult.asNonparsedArg());
			}
		}
		for (Language language : languages) {
			for (String name : names) {
				switch (language) {
				case EN:
					System.out.printf("Hello %s%n", name);
					break;
				case ES:
					System.out.printf("Hola %s%n", name);
					break;
				case FR:
					System.out.printf("Salut %s%n", name);
					break;
				default:
					break;
				}
			}
		}
	}
}

</pre>

## Requirements

- Apache Maven&#8482; 3.3.9 or higher 

- Java&#8482; SDK 1.6 or higher

## Installation

To install, run the following commands:

<pre>

$ cd argmatey
$ mvn install

</pre>