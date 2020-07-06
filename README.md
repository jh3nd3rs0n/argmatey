# ArgMatey

[![Total alerts](https://img.shields.io/lgtm/alerts/g/jh3nd3rs0n/argmatey.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/argmatey/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/jh3nd3rs0n/argmatey.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/argmatey/context:java) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/715ef579e39647b4ab3d4c9dc9c12a7b)](https://www.codacy.com/manual/jh3nd3rs0n/argmatey?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/argmatey&amp;utm_campaign=Badge_Grade)

ArgMatey is a Java command line argument parsing library that has the following features:

**Class with annotated methods to define and receive results from parsing.** The following is an example:

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
         * Invoked when either of the options "-w" and "--wrap" is 
         * encountered. Option argument for either of the options "-w" 
         * and "--wrap" is provided as an argument to the method 
         * parameter below.
         *
         * Methods that are annotated with @OptionGroup that have an 
         * @OptionArgSpec must have only one method parameter. The 
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
        @OptionGroup(
            option = @Option(
                doc = "Wrap encoded lines after COLS character (default is 76)",
                name = "w",
                optionArgSpec = @OptionArgSpec(
                    name = "COLS"
                ),
                type = PosixOption.class
            ),
            otherOptions = {
                @Option(
                    name = "wrap",
                    // optionArgSpec is provided from the above @Option.                    
                    type = GnuLongOption.class
                )
            } 
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
         * Methods that are annotated with @OptionGroup without an 
         * @OptionArgSpec can have no method parameters or only one 
         * method parameter of type boolean.
         */        
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
            this.decodingMode = b; // always received as true
        }
        
        /*
         * Invoked when a non-parsed argument is encountered. In this 
         * implementation, the non-parsed argument is the FILE argument. 
         * The non-parsed argument is provided as an argument to the 
         * method parameter below. 
         *
         * Only one method in a class can be annotated with 
         * @NonparsedArg. Methods that are annotated with @NonparsedArg 
         * must have only one method parameter of type String.
         */
        @NonparsedArg
        public void setFile(String s) {
            if (this.file != null) {
                /*
                 * IllegalArgumentExceptions for invalid non-parsed
                 * arguments can be thrown.
                 */
                throw new IllegalArgumentException(String.format(
                    "extra operand: `%s'", s));
            }
            this.file = s;
        }
        
        /*
         * Invoked when either of the options "-i" and "--ignore-garbage" 
         * is encountered.
         *
         * Methods that are annotated with @OptionGroup without an 
         * @OptionArgSpec can have no method parameters or only one 
         * method parameter of type boolean.
         */
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
            this.garbageIgnored = b; // always received as true
        }
        
    }
    
    public static void main(String[] args) {
        Base64CLI base64CLI = new Base64CLI(args);
        // for the program help and version information
        base64CLI.setProgramName("base64");
        base64CLI.setProgramVersion("base64 1.0");
        base64CLI.setProgramArgsUsage(" [FILE]");
        StringBuilder sb = new StringBuilder();
        sb.append("Base64 encode or decode FILE, or standard input, to standard output.");
        sb.append(String.format("%n%n"));
        sb.append("With no FILE, or when FILE is -, read standard input.");
        base64CLI.setProgramDoc(sb.toString());
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
     
**Command line argument parsing results on a per command line argument basis (similar to getopt, getopt_long, and Argp).** This style of command line argument parsing has the following advantages:

-   Interpretation of multiple occurrences of the same option
-   Interpretation of multiple occurrences of options from the same group
-   Interpretation of options and arguments based on the ordering provided

**Complete customization of the usage and help text for the options.** The following is an example based off of the earlier example:

```java
    
    /*
     * Provides the help text for an option group on a single line 
     * instead of multiple lines.
     */
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
    
    public static class Base64CLI extends CLI {
    
        // ...
        
        @OptionGroup(
            option = @Option(
                doc = "Wrap encoded lines after COLS character (default is 76)",
                name = "w",
                optionArgSpec = @OptionArgSpec(
                    name = "COLS"
                ),
                type = PosixOption.class
            ),
            /*
             * You can apply the OptionGroupHelpTextProvider to a 
             * particular option group...
             */ 
            optionGroupHelpTextProvider = CustomOptionGroupHelpTextProvider.class,
            otherOptions = {
                @Option(
                    name = "wrap", 
                    type = GnuLongOption.class
                )
            } 
        )
        public void setColumnLimit(Integer i) {
            // ... 
        }
        
        // ...
        
    }
    
    public static void main(String[] args) {
        // ...or you can apply the OptionGroupHelpTextProvider to all option groups.
        OptionGroupHelpTextProvider provider = new CustomOptionGroupHelpTextProvider();
        OptionGroupHelpTextProvider.setDefault(provider);
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

The following are some examples of projects using ArgMatey:

-   [Jargyle](https://github.com/jh3nd3rs0n/jargyle) (specific examples: [SocksServerCLI.java](https://github.com/jh3nd3rs0n/jargyle/blob/master/src/main/java/jargyle/server/SocksServerCLI.java), [UsersCLI.java](https://github.com/jh3nd3rs0n/jargyle/blob/master/src/main/java/jargyle/server/socks5/UsersCLI.java))
-   [JBase64Transformer](https://github.com/jh3nd3rs0n/jbase64transformer) (specific example: [Base64Transformer.java](https://github.com/jh3nd3rs0n/jbase64transformer/blob/master/src/main/java/jbase64transformer/Base64Transformer.java))

## Contents

-   [Requirements](#requirements)
-   [Installing](#installing)
-   [Building](#building)
-   [TODO](#todo)
-   [Contact](#contact)

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

-   [ ] Javadoc documentation on all types
-   [ ] Unit testing on other types
  
## Contact

If you have any questions or comments, you can e-mail me at `j0n4th4n.h3nd3rs0n@gmail.com`
