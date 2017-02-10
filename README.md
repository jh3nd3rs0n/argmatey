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
        import java.util.List;
        
        public class Greeting {
        	private static enum Language { EN, ES, FR }
        	public static void main(String[] args) {
        		Option helpOption = new PosixOption.Builder("h")
        			.builders(new GnuLongOption.Builder("help"))
        			.doc("Print this help and exit")
        			.special(true)
        			.build();
        		Option languageOption = new PosixOption.Builder("l")
        			.builders(
        				new LongOption.Builder("lang"),
        				new GnuLongOption.Builder("language"))
        			.doc("Language for the greeting")
        			.optionArgSpec(
        				new OptionArgSpec.Builder()
        					.name("LANGUAGE")
        					.type(Language.class)
        					.build())
        			.build();       			
        		Options options = new Options(helpOption, languageOption);
        		ArgParser argParser = new ArgParser(
        			args, options, false);
        		Language language = Language.EN;
        		List<String> names = new ArrayList<String>();
        		while (argParser.hasNext()) {
        			ParseResult parseResult = argParser.parseNext();
        			if (parseResult.isOptionOfAnyOf("-h", "--help")) {
        				System.out.print("Usage: Greeting ");
        				argParser.getOptions().printUsage();
        				System.out.println(" NAME(S)");
        				System.out.println();
        				System.out.println("OPTIONS:");
        				argParser.getOptions().printHelpText();
        				System.out.println();
        				System.out.println();
        				return;
        			}
        			if (parseResult.isOptionOfAnyOf("-l", "-lang", "--language")) {
        				language = parseResult.getOptionArg().getTypeValue(Language.class);
        			}
        			if (parseResult.isNonparsedArg()) {
        				names.add(parseResult.getNonparsedArg());
        			}
        		}
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
                
        ```

## Requirements

- Apache Maven&#8482; 3.3.9 or higher 

- Java&#8482; SDK 1.6 or higher

## Installation

To install, run the following commands:

        ```
        $ cd argmatey
        $ mvn install
        ```
