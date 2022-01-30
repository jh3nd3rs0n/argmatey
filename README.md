# ArgMatey

[![Total alerts](https://img.shields.io/lgtm/alerts/g/jh3nd3rs0n/argmatey.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/argmatey/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/jh3nd3rs0n/argmatey.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/argmatey/context:java) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/246e8008635747eb94e11641504d553d)](https://www.codacy.com/gh/jh3nd3rs0n/argmatey/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/argmatey&amp;utm_campaign=Badge_Grade)

ArgMatey is an extensible Java command line interface.

**DISCLAIMER**: ArgMatey is not production-ready but it aims to be. It is also subject to breaking changes.

## Contents

-   [About](#about)
-   [Examples](#examples)
-   [Requirements](#requirements)
-   [Generating Javadocs](#generating-javadocs)
-   [Automated Testing](#automated-testing)
-   [Installing](#installing)
-   [Building](#building)
-   [Contact](#contact)

## About

ArgMatey is an extensible Java command line interface that has the following features:

**Command line option types whose syntax and behavior are based on the [POSIX Utility Conventions](http://pubs.opengroup.org/onlinepubs/9699919799/basedefs/V1_chap12.html), GNU's function [getopt_long](http://www.gnu.org/software/libc/manual/html_node/Getopt-Long-Options.html#Getopt-Long-Options), and GNU's [Argp](http://www.gnu.org/software/libc/manual/html_node/Argp.html#Argp) interface:**

-   GNU long options (examples: `--help` `--version` `--output-file=file.txt`)
-   Long options (examples: `-help` `-version` `-output-file file.txt`)
-   POSIX options (examples: `-h` `-v` `-o file.txt`)

**Command line argument parsing and handling with triggerable event methods, query methods, iterator methods, and descriptive methods.** To parse and handle command line arguments, a class must extend `CLI` and have its constructor invoke the superclass's constructor `CLI(String[] args, boolean posixCorrect)` and the instance of that  extending class must have its inherited public method `CLI.handleArgs()` invoked. The extending class can have, override, or use the following methods:

Triggerable event methods that can be added:

-   `@Option` annotated methods: invoked when any of the command line options defined by the method's `@Option` annotations is encountered

Triggerable event methods that can be overridden:

-   `CLI.beforeHandleArgs()`: invoked before parsing and handling the command line arguments
-   `CLI.afterHandleArgs()`: invoked after parsing and handling the command line arguments
-   `CLI.beforeHandleNext()`: invoked before parsing and handling the next part of the current command line argument or the next command line argument
-   `CLI.afterHandleNext()`: invoked after parsing and handling the next part of the current command line argument or the next command line argument
-   `CLI.displayProgramHelp()`: invoked when one of the command line options for the program help (`--help`) is encountered and displays the program help
-   `CLI.displayProgramVersion()`: invoked when one of the command line options for the program version (`--version`) is encountered and displays the program version
-   `CLI.handleNonparsedArg(String)`: invoked when a non-parsed command line argument is encountered
-   `CLI.handleThrowable(Throwable)`: invoked when a `Throwable` is thrown when parsing and handling the next part of the command line argument or the next command line argument

Query methods:

-   `CLI.getArg()`: returns the current command line argument
-   `CLI.getArg(int)`: returns the command line argument of the provided index
-   `CLI.getArgCharIndex()`: returns the current character index of the current command line argument
-   `CLI.getArgCount()`: returns the count of the command line arguments
-   `CLI.getArgIndex()`: return the index of the current command line argument
-   `CLI.getArgs()`: returns an array of the command line arguments
-   `CLI.getParseResultHolder()`: returns the current `ParseResultHolder`

Iterator methods:

-   `CLI.hasNext()`: returns a boolean value to indicate if this `CLI` has the next part of the current command line argument or the next command line argument
-   `CLI.next()`: returns the next part of the current command line argument or the next command line argument

Descriptive methods:

-   `CLI.getOptionGroups()`: returns an instance of `OptionGroups` defined by the `@Option` annotated methods
-   `CLI.getProgramDoc()`/`CLI.setProgramDoc(String)`: returns/sets the documentation (description) of the program
-   `CLI.getProgramName()`/`CLI.setProgramName(String)`: returns/sets the name of the program
-   `CLI.getProgramOperandsUsage()`/`CLI.setProgramOperandsUsage(String)`: returns/sets the usage of the operands of the program
-   `CLI.getProgramVersion()`/`CLI.setProgramVersion(String)`: returns/sets the version of the program

This style of command line argument parsing and handling has the following advantages:

-   Custom interpretation of multiple occurrences of the same command line option
-   Custom interpretation of multiple occurrences of command line options from the same group
-   Custom interpretation of command line options and arguments based on the ordering provided
-   Custom adaptability of the program based on the command line option or argument encountered

An example of using ArgMatey's command line argument parsing and handling can be found [here](#example-of-using-argmatey).

**Complete customization of the program usage and help.** Every level of the program usage and help can be customized. The usage of one, a few, or all command line options of a particular type can be customized in a different format. The help text of one, a few, or all command line option groups can be customized in a different format. The entire program usage and help can be customized in a completely different format.

An example of customizing one aspect of the program help can be found [here](#example-of-customizing-the-help-text-of-command-line-option-groups).
 
**Single source code file.** As an alternative to importing ArgMatey as a Maven dependency or a JAR file, ArgMatey can be imported to a project as a source code file.

## Examples

### Example of Using ArgMatey

The following is an example of using ArgMatey:

```java
    
    import com.github.jh3nd3rs0n.argmatey.ArgMatey;
    import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.Option;
    import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.OptionArgSpec;
    import com.github.jh3nd3rs0n.argmatey.ArgMatey.CLI;
    import com.github.jh3nd3rs0n.argmatey.ArgMatey.OptionType;
    import com.github.jh3nd3rs0n.argmatey.ArgMatey.TerminationRequestedException;
    
    /*
     * Extends CLI to parse and handle command line options and 
     * arguments for this Java implementation of GNU's utility 
     * base64.
     */
    public class Base64CLI extends CLI {
    
        private int columnLimit;
        private boolean decodingMode;
        private String file;
        private boolean garbageIgnored;
    
        public Base64CLI(String[] args) {
            super(args, false);
            // for the program help and version information
            this.setProgramDoc(new StringBuilder()
                .append("Base64 encode or decode FILE, ")
                .append("or standard input, to standard output.")
                .append(String.format("%n%n"))
                .append("With no FILE, or when FILE is -, ")
                .append("read standard input.")
                .toString());
            this.setProgramName("base64");
            this.setProgramOperandsUsage("[FILE]");
            this.setProgramVersion("base64 1.0");
        }
        
        /*
         * Invoked after parsing and handling the command line 
         * arguments.
         */
        @Override
        protected void afterHandleArgs() 
            throws TerminationRequestedException {
            // post parsing can happen here
            System.out.printf("columnLimit: %s%n", this.columnLimit);
            System.out.printf("decodingMode: %s%n", this.decodingMode);
            System.out.printf("file: %s%n", this.file);
            System.out.printf("garbageIgnored: %s%n", this.garbageIgnored);
            throw new TerminationRequestedException(0);
        }
        
        /*
         * Invoked after parsing and handling the next part of the 
         * current command line argument or the next command line 
         * argument.
         */
        @Override
        protected void afterHandleNext() 
            throws TerminationRequestedException {
        }
        
        /*
         * Invoked before parsing and handling the command line 
         * arguments.
         */
        @Override
        protected void beforeHandleArgs() 
            throws TerminationRequestedException {
            // initialization
            this.columnLimit = 76; // default            
            this.decodingMode = false;
            this.file = null;            
            this.garbageIgnored = false;
        }
        
        /*
         * Invoked before parsing and handling the next part of the 
         * current command line argument or the next command line 
         * argument.
         */
        @Override
        protected void beforeHandleNext() 
            throws TerminationRequestedException {
        }
                
        /*
         * Invoked when a non-parsed command line argument is 
         * encountered. In this implementation, the non-parsed 
         * command line argument is the command line argument for 
         * FILE. The non-parsed command line argument is provided 
         * as an argument to the method parameter below. 
         */
        @Override
        protected void handleNonparsedArg(String nonparsedArg) 
            throws TerminationRequestedException {
            if (this.file != null) {
                /*
                 * IllegalArgumentExceptions for invalid non-parsed
                 * command line arguments can be thrown.
                 */
                throw new IllegalArgumentException(String.format(
                    "extra operand: `%s'", nonparsedArg));
            }
            this.file = nonparsedArg;
        }
        
        /*
         * Invoked when a Throwable is thrown when parsing and 
         * handling the next part of the command line argument or 
         * the next command line argument.
         */
        @Override
        protected void handleThrowable(Throwable t) 
            throws TerminationRequestedException {
            System.err.printf("%s: %s%n", this.getProgramName(), throwable);
            throw new TerminationRequestedException(-1);
        }
                
        /*
         * Invoked when either of the command line options "-w" and 
         * "--wrap" is encountered. Command line option argument 
         * for either of the command line options "-w" and "--wrap" 
         * is provided as an argument to the method parameter below.
         *
         * Methods that are annotated with @Option that allow for a 
         * command line option argument must have only one method
         * parameter. The method parameter's type must be a type or 
         * a java.util.List of a type that has either a static 
         * String conversion method or a constructor that has only 
         * one constructor parameter of type String.
         *
         * Alternatively, for any method parameter type, a class 
         * that extends StringConverter can be used to convert the 
         * command line option argument to that type. It can be 
         * supplied to @OptionArgSpec.stringConverter.
         */
        @Option(
            doc = "Wrap encoded lines after COLS character (default is 76)",
            name = "w",
            optionArgSpec = @OptionArgSpec(name = "COLS"),
            type = OptionType.POSIX
        )
        @Option(
            name = "wrap",
            // optionArgSpec is provided from the above @Option.                    
            type = OptionType.GNU_LONG
        )
        private void setColumnLimit(Integer i) { 
            int intValue = i.intValue();
            if (intValue < 0) {
                /*
                 * IllegalArgumentExceptions for invalid command 
                 * line option arguments can be thrown.
                 */
                throw new IllegalArgumentException(
                    "must be a non-negative integer");
            }
            this.columnLimit = intValue;
        }
                
        /*
         * Invoked when either of the command line options "-d" and 
         * "--decode" is encountered.
         *
         * Methods that are annotated with @Option that does not 
         * allow for a command line option argument can have no 
         * method parameters or only one method parameter of type 
         * boolean.
         */        
        @Option(
            doc = "Decode data",
            name = "d",
            type = OptionType.POSIX
        )
        @Option(
            name = "decode", 
            type = OptionType.GNU_LONG
        )
        private void setDecodingMode(boolean b) {
            this.decodingMode = b; // always received as true
        }
        
        /*
         * Invoked when either of the command line options "-i" and 
         * "--ignore-garbage" is encountered.
         *
         * Methods that are annotated with @Option that does not 
         * allow for a command line option argument can have no 
         * method parameters or only one method parameter of type 
         * boolean.
         */
        @Option(
            doc = "When decoding, ignore non-alphabet characters",
            name = "i",
            type = OptionType.POSIX
        )
        @Option(
            name = "ignore-garbage", 
            type = OptionType.GNU_LONG
        )
        private void setGarbageIgnored(boolean b) {
            this.garbageIgnored = b; // always received as true
        }
            
        public static void main(String[] args) {
            // how to use a CLI such as Base64CLI
            CLI cli = new Base64CLI(args);
            try {
                cli.handleArgs();
            } catch (TerminationRequestedException e) {
                System.exit(e.getExitStatusCode());
            }
        }
        
        /*
         * Output when using the command line option "--help" :
         *
         * Usage: base64 [OPTION]... [FILE]
         * Base64 encode or decode FILE, or standard input, to standard output.
         *
         * With no FILE, or when FILE is -, read standard input.
         *
         * OPTIONS:
         *   -d, --decode
         *       Decode data
         *   --help
         *       Display this help and exit
         *   -i, --ignore-garbage
         *       When decoding, ignore non-alphabet characters
         *   --version
         *       Display version information and exit
         *   -w COLS, --wrap=COLS
         *       Wrap encoded lines after COLS character (default is 76)
         *
         */
         
        /*
         * Output when using the command line option "--version" :
         *
         * base64 1.0
         *
         */
         
        /*
         * Output when using the following command line arguments:
         *
         * -w 100 input.txt
         *
         * columnLimit: 100
         * decodingMode: false
         * file: input.txt
         * garbageIgnored: false
         *
         */
         
        /*
         * Output when using the following command line arguments:
         *
         * -di text.txt
         *
         * columnLimit: 76
         * decodingMode: true
         * file: text.txt
         * garbageIgnored: true
         *
         */
         
        /*
         * Output when using the following command line arguments:
         *
         * --wrap=10 -
         *
         * columnLimit: 10
         * decodingMode: false
         * file: -
         * garbageIgnored: false
         *
         */
         
        /*
         * Output when using the following command line arguments:
         *
         * --decode --ignore-garbage text.txt
         *
         * columnLimit: 76
         * decodingMode: true
         * file: text.txt
         * garbageIgnored: true
         *
         */
         
        /*
         * Output when using the bogus command line option "--bogus":
         *
         * base64: com.github.jh3nd3rs0n.argmatey.ArgMatey$UnknownOptionException: unknown option `--bogus'
         *
         */
         
        /*
         * Output when using the following command line arguments:
         *
         * --wrap=-42 input.txt
         *
         * base64: com.github.jh3nd3rs0n.argmatey.ArgMatey$IllegalOptionArgException: illegal option argument `-42' for option `--wrap': java.lang.IllegalArgumentException: must be a non-negative integer
         *
         */
         
        /*
         * Output when using the following command line arguments:
         *
         * text.txt anothertext.txt
         *
         * base64: com.github.jh3nd3rs0n.argmatey.ArgMatey$IllegalArgException: illegal argument `anothertext.txt': java.lang.IllegalArgumentException: extra operand: `anothertext.txt'
         *
         */
    }
    
```

### Example of Customizing the Help Text of Command Line Option Groups

The following is the earlier example using a customized `OptionGroupHelpTextProvider`:

```java

    // ...
    import com.github.jh3nd3rs0n.argmatey.ArgMatey.Annotations.OptionGroupHelpTextProvider;
    import com.github.jh3nd3rs0n.argmatey.ArgMatey.OptionGroupHelpTextParams;
        
    public class Base64CLI extends CLI {
        
        /*
         * Provides the help text for a command line option group 
         * on a single line instead of multiple lines.
         */
        private static class SingleLineOptionGroupHelpTextProvider 
            extends ArgMatey.OptionGroupHelpTextProvider {
        
            @Override
            public String getOptionGroupHelpText(OptionGroupHelpTextParams params) {
                String optionGroupHelpText = null;
                StringBuilder sb = null;
                String doc = null;
                for (ArgMatey.Option option : params.getDisplayableOptions()) {
                    if (sb == null) {
                        sb = new StringBuilder();
                        sb.append("  ");
                    } else {
                        sb.append(", ");
                    }
                    sb.append(option.getUsage());
                    if (doc == null) {
                        doc = option.getDoc();
                    }
                }
                if (sb != null) {
                    if (doc != null) {
                        final int docStart = 24;
                        int length = sb.length();
                        for (int i = 0; i < docStart - length; i++) {
                            sb.append(' ');
                        }
                        sb.append(doc);
                    }
                    optionGroupHelpText = sb.toString();
                }
                return optionGroupHelpText;
            }
        
        }
        
        // ...
        
        @Option(
            doc = "Wrap encoded lines after COLS character (default is 76)",
            name = "w",
            optionArgSpec = @OptionArgSpec(name = "COLS"),
            type = OptionType.POSIX
        )
        @Option(
            name = "wrap",
            type = OptionType.GNU_LONG
        )
        /*
         * You can apply the OptionGroupHelpTextProvider to a 
         * particular command line option group...
         */ 
        @OptionGroupHelpTextProvider(SingleLineOptionGroupHelpTextProvider.class)
        private void setColumnLimit(Integer i) {
            // ... 
        }
        
        // ...
        
        public static void main(String[] args) {
            /*
             * ... or you can apply the OptionGroupHelpTextProvider 
             * to all command line option groups.
             */
            ArgMatey.OptionGroupHelpTextProvider provider = new SingleLineOptionGroupHelpTextProvider();
            ArgMatey.OptionGroupHelpTextProvider.setDefault(provider);
            // ...        
        }
                
        /*
         * Output when using the command line option "--help" :
         *
         * Usage: base64 [OPTION]... [FILE]
         * Base64 encode or decode FILE, or standard input, to standard output.
         *
         * With no FILE, or when FILE is -, read standard input.
         *
         * OPTIONS:
         *   -d, --decode          Decode data
         *   --help                Display this help and exit
         *   -i, --ignore-garbage  When decoding, ignore non-alphabet characters
         *   --version             Display version information and exit
         *   -w COLS, --wrap=COLS  Wrap encoded lines after COLS character (default is 76)
         *
         */
         
    }
    
```

### Other Examples

The following are some examples of projects using ArgMatey:

-   [Jargyle](https://github.com/jh3nd3rs0n/jargyle) (specific examples: [SocksServerCLI.java](https://github.com/jh3nd3rs0n/jargyle/blob/master/src/main/java/com/github/jh3nd3rs0n/jargyle/server/SocksServerCLI.java), [UsersCLI.java](https://github.com/jh3nd3rs0n/jargyle/blob/master/src/main/java/com/github/jh3nd3rs0n/jargyle/server/socks5/userpassauth/UsersCLI.java))
-   [JBase64Transformer](https://github.com/jh3nd3rs0n/jbase64transformer) (specific example: [Base64TransformerCLI.java](https://github.com/jh3nd3rs0n/jbase64transformer/blob/master/src/main/java/com/github/jh3nd3rs0n/jbase64transformer/Base64TransformerCLI.java))

## Requirements

-   Apache Maven&#8482; 3.3.9 or higher (for generating javadocs, automated testing, installing, and building) 
-   Java&#8482; SDK 1.8 or higher

## Generating Javadocs

To generate javadocs, run the following commands:

```bash

    cd argmatey
    mvn javadoc:javadoc

```

After running the aforementioned commands, the javadocs can be found in the following path:

```text
    
    target/site/apidocs
    
```

## Automated Testing

To run automated testing, run the following command:

```bash

    mvn test

```

## Installing

To install, run the following command:

```bash

    mvn install

```

To add a dependency on ArgMatey using Maven, use the following:

```xml

    <dependency>
    	<groupId>com.github.jh3nd3rs0n</groupId>
    	<artifactId>argmatey</artifactId>
    	<version>1.0.0-SNAPSHOT</version>
    </dependency>

```

## Building

To build and package ArgMatey as a JAR file, run the following command:

```bash

    mvn package

```

After running the aforementioned command, the JAR file can be found in the following path:

```text
    
    target/argmatey-${VERSION}.jar
    
```

`${VERSION}` is replaced by the actual version shown within the name of the JAR file.
  
## Contact

If you have any questions or comments, you can e-mail me at `j0n4th4n.h3nd3rs0n@gmail.com`
