# ArgMatey

[![Total alerts](https://img.shields.io/lgtm/alerts/g/jh3nd3rs0n/argmatey.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/argmatey/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/jh3nd3rs0n/argmatey.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/argmatey/context:java) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/715ef579e39647b4ab3d4c9dc9c12a7b)](https://www.codacy.com/manual/jh3nd3rs0n/argmatey?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/argmatey&amp;utm_campaign=Badge_Grade)

ArgMatey is a Java annotation-based iterator-style command line arguments parser with completely customizable provided program usage and help.

**Disclaimer:** ArgMatey is a hobby project and is currently subject to breaking changes. ArgMatey is currently not production ready but it aims to be.

## Contents

-   [Features](#features)
-   [Examples](#examples)
-   [Requirements](#requirements)
-   [Installing](#installing)
-   [Building](#building)
-   [TODO](#todo)
-   [Contact](#contact)

## Features

**Extended class with annotated methods to define and receive results from parsing.** The following is an example:

```java
    
    /*
     * Extends CLI to handle command line options and arguments for 
     * this Java implementation of GNU's utility base64.
     */
    public static class Base64CLI extends CLI {
    
        private int columnLimit;
        private boolean decodingMode;
        private String file;
        private boolean garbageIgnored;
    
        public Base64CLI(String[] args) {
            super(args, false);
            this.columnLimit = 76; // default            
            this.decodingMode = false;
            this.file = null;            
            this.garbageIgnored = false;
            // for the program help and version information
            this.programArgsUsage = "[FILE]";
            this.programDoc = new StringBuilder()
                .append("Base64 encode or decode FILE, ")
                .append("or standard input, to standard output.")
                .append(String.format("%n%n"))
                .append("With no FILE, or when FILE is -, ")
                .append("read standard input.")
                .toString();
            this.programName = "base64";
            this.programVersion = "base64 1.0";
        }
        
        // getter methods
        
        public int getColumnLimit() {
            return this.columnLimit;
        }
        
        public String getFile() {
            return this.file;
        }
        
        public boolean isDecodingMode() {
            return this.decodingMode;
        }
        
        public boolean isGarbageIgnored() {
            return this.garbageIgnored;
        }
        
        // setter methods
        
        /*
         * Invoked when a non-parsed argument is encountered. In this 
         * implementation, the non-parsed argument is the FILE argument. 
         * The non-parsed argument is provided as an argument to the 
         * method parameter below. 
         */
        @Override
        protected void handleNonparsedArg(String nonparsedArg) {
            if (this.file != null) {
                /*
                 * IllegalArgumentExceptions for invalid non-parsed
                 * arguments can be thrown.
                 */
                throw new IllegalArgumentException(String.format(
                    "extra operand: `%s'", nonparsedArg));
            }
            this.file = nonparsedArg;
        }
        
        /*
         * Invoked when either of the options "-w" and "--wrap" is 
         * encountered. Option argument for either of the options "-w" 
         * and "--wrap" is provided as an argument to the method 
         * parameter below.
         *
         * Methods that are annotated with @Option that allow for an 
         * option argument must have only one method parameter. The 
         * method parameter's type must be a type or a java.util.List 
         * of a type that has either a static String conversion method 
         * or a constructor that has only one constructor parameter of 
         * type String.
         *
         * Alternatively, for any method parameter type, a class 
         * that extends StringConverter can be used to convert the 
         * option argument to that type. It can be supplied to 
         * @OptionArgSpec.stringConverter.
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
        public void setColumnLimit(Integer i) { 
            int intValue = i.intValue();
            if (intValue < 0) {
                /*
                 * IllegalArgumentExceptions for invalid option arguments
                 * can be thrown.
                 */
                throw new IllegalArgumentException(
                    "must be a non-negative integer");
            }
            this.columnLimit = intValue;
        }
        
        /*
         * Invoked when either of the options "-d" and "--decode" is 
         * encountered.
         *
         * Methods that are annotated with @Option that does not allow 
         * for an option argument can have no method parameters or only 
         * one method parameter of type boolean.
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
        public void setDecodingMode(boolean b) {
            this.decodingMode = b; // always received as true
        }
        
        /*
         * Invoked when either of the options "-i" and "--ignore-garbage" 
         * is encountered.
         *
         * Methods that are annotated with @Option that does not allow 
         * for an option argument can have no method parameters or only 
         * one method parameter of type boolean.
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
        public void setGarbageIgnored(boolean b) {
            this.garbageIgnored = b; // always received as true
        }
        
    }
    
    public static void main(String[] args) {
        Base64CLI base64CLI = new Base64CLI(args);
        // parse (and handle) the command line arguments
        while (base64CLI.hasNext()) {
            base64CLI.handleNext();
            /*
             * If the option "--help" or the option "--version" is
             * encountered and handled, exit the program regardless of 
             * the remaining command line arguments.
             */            
            if (base64CLI.isProgramHelpDisplayed()
                || base64CLI.isProgramVersionDisplayed()) {
                return;
            }
        }
        // ...        
    }
    
    /*
     * Output when using the option "--help" :
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
     * Output when using the option "--version" :
     *
     * base64 1.0
     *
     */
    
```

**Option syntax and behavior based on the [POSIX Utility Conventions](http://pubs.opengroup.org/onlinepubs/9699919799/basedefs/V1_chap12.html), GNU's function [getopt_long](http://www.gnu.org/software/libc/manual/html_node/Getopt-Long-Options.html#Getopt-Long-Options), and GNU's [Argp](http://www.gnu.org/software/libc/manual/html_node/Argp.html#Argp) interface.** The types of options you can use are familiar and readily understood by many.

**Option types:**

-   GNU long options (examples: `--help` `--version` `--output-file=file.txt`)

-   Long options (examples: `-help` `-version` `-output-file file.txt`)

-   POSIX options (examples: `-h` `-v` `-o file.txt`)
     
**Iterator-style command line argument parsing.** This style of command line argument parsing has the following advantages:

-   Interpretation of multiple occurrences of the same option
-   Interpretation of multiple occurrences of options from the same group
-   Interpretation of options and arguments based on the ordering provided
-   Adaptability of the program based on the option or argument encountered

**Complete customization of the provided program usage and help.** Every level of the provided program usage and help can be customized. A customized `OptionUsageProvider` can be used to provide the usage of one, a few, or all options of a particular type in a different format. A customized `OptionGroupHelpTextProvider` can be used to provide the help text of one, a few, or all option groups in a different format. Methods `CLI.displayProgramUsage()` and `CLI.displayProgramHelp()` can be overridden to display the entire program usage and help in a completely different format. The following is the earlier example using a customized `OptionGroupHelpTextProvider`:

```java
    
    /*
     * Provides the help text for an option group on a single line 
     * instead of multiple lines.
     */
    public static class SingleLineOptionGroupHelpTextProvider 
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
    
    public static class Base64CLI extends CLI {
    
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
         * particular option group...
         */ 
        @OptionGroupHelpTextProvider(SingleLineOptionGroupHelpTextProvider.class)
        public void setColumnLimit(Integer i) {
            // ... 
        }
        
        // ...
        
    }
    
    public static void main(String[] args) {
        // ...or you can apply the OptionGroupHelpTextProvider to all option groups.
        ArgMatey.OptionGroupHelpTextProvider provider = new SingleLineOptionGroupHelpTextProvider();
        ArgMatey.OptionGroupHelpTextProvider.setDefault(provider);
        // ...        
    }
    
    /*
     * Output when using the option "--help" :
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
    
```

**Single source code file.** As an alternative to importing ArgMatey as a Maven dependency or a jar file, ArgMatey can be imported to a project as a source code file.

## Examples

The following are some examples of projects using ArgMatey:

-   [Jargyle](https://github.com/jh3nd3rs0n/jargyle) (specific examples: [SocksServerCLI.java](https://github.com/jh3nd3rs0n/jargyle/blob/master/src/main/java/jargyle/server/SocksServerCLI.java), [UsersCLI.java](https://github.com/jh3nd3rs0n/jargyle/blob/master/src/main/java/jargyle/server/socks5/UsersCLI.java))
-   [JBase64Transformer](https://github.com/jh3nd3rs0n/jbase64transformer) (specific example: [Base64Transformer.java](https://github.com/jh3nd3rs0n/jbase64transformer/blob/master/src/main/java/jbase64transformer/Base64Transformer.java))

## Requirements

-   Apache Maven&#8482; 3.3.9 or higher (for installing and building) 
-   Java&#8482; SDK 1.8 or higher

## Installing

To install, run the following commands:

```bash

    cd argmatey
    mvn install

```

To add a dependency on ArgMatey using Maven, use the following:

```xml

    <dependency>
    	<groupId>argmatey</groupId>
    	<artifactId>argmatey</artifactId>
    	<version>1.0-SNAPSHOT</version>
    </dependency>

```

## Building

To build and package ArgMatey as a jar file, run the following command:

```bash

    mvn package

```

## TODO

See [here](https://github.com/jh3nd3rs0n/jargyle/blob/master/TODO.md)
  
## Contact

If you have any questions or comments, you can e-mail me at `j0n4th4n.h3nd3rs0n@gmail.com`
