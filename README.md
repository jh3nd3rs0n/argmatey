#ArgMatey

ArgMatey is a simple yet comprehensive Java command line argument parser framework that has the following features:

- API terminology and option syntax and behavior based on the [POSIX Utility Conventions](http://pubs.opengroup.org/onlinepubs/9699919799/basedefs/V1_chap12.html), GNU's function [getopt_long()](http://www.gnu.org/software/libc/manual/html_node/Getopt-Long-Options.html#Getopt-Long-Options), and GNU's [Argp](http://www.gnu.org/software/libc/manual/html_node/Argp.html#Argp) interface
 
- Option types:
 
  - POSIX options (examples: `-h -v -o file.txt`)
    
  - Long options (examples: `-help -version -output-file file.txt`)
    
  - GNU long options (examples: `--help --version --output-file=file.txt`)
     
- Incremental command line argument parsing (similar to getopt(), getopt_long(), and Argp)
 
- Complete customization of usage and help text for the options

## Example

        ```java
        
        import argmatey.ArgParser;
        import argmatey.GnuLongOption;
        import argmatey.LongOption;
        import argmatey.Option;
        import argmatey.OptionArgSpec;
        import argmatey.Options;
        import argmatey.ParseResult;
        import argmatey.PosixOption;
        
        import java.util.ArrayList;
        import java.util.EnumSet;
        import java.util.List;
        import java.util.Set;
        
        public class Greet {
        	private static enum Language { EN, ES, FR }
        	public static void main(String[] args) {
        		Option helpOption = new PosixOption.Builder("h")
        			.builders(new GnuLongOption.Builder("help"))
        			.doc("Print this help and exit")
        			.special(true)
        			.build();
        		Option languagesOption = new PosixOption.Builder("l")
        			.builders(
        				new LongOption.Builder("lang"),
        				new GnuLongOption.Builder("languages"))
        			.doc("Comma-separated language(s) for the greeting(s).")
        			.optionArgSpec(
        				new OptionArgSpec.Builder()
        					.name("LANGUAGE(S)")
        					.separator(",")
        					.type(Language.class)
        					.build())
        			.build();       			
        		Options options = new Options(helpOption, languagesOption);
        		ArgParser argParser = new ArgParser(
        			args, options, false);
        		Set<Language> languages = EnumSet.of(Language.EN);
        		List<String> names = new ArrayList<String>();
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
                
        ```

## Requirements

- Apache Maven&#8482; 3.3.9 or higher 

- Java&#8482; SDK 1.6 or higher

## Installation

To install, run the following commands:

`$ cd argmatey`

`$ mvn install`