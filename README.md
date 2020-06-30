# ArgMatey

[![Total alerts](https://img.shields.io/lgtm/alerts/g/jh3nd3rs0n/argmatey.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/argmatey/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/jh3nd3rs0n/argmatey.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/argmatey/context:java) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/715ef579e39647b4ab3d4c9dc9c12a7b)](https://www.codacy.com/manual/jh3nd3rs0n/argmatey?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/argmatey&amp;utm_campaign=Badge_Grade)

ArgMatey is a Java command line argument parsing library that has the following features:

**Annotations for methods to define and receive results from parsing.** The following is an example:

```java
    
    /*
     * handles command line options and arguments for this Java 
     * implementation of GNU's utility base64
     */
    public static class Base64Cli {
    
        // instance fields, constructor(s), etc...
        
        // invoked when the option "--help" is encountered
        @OptionOccurrenceSink(
            optionBuilder = @OptionBuilder(
                doc = "display this help and exit",
                name = "help",
                type = GnuLongOption.class
            )
        )
        public void displayHelp() {
            // ...
        }
        
        // invoked when the option "--version" is encountered
        @OptionOccurrenceSink(
            optionBuilder = @OptionBuilder(
                doc = "display version information and exit",
                name = "version",
                type = GnuLongOption.class
            )
        )
        public void displayVersion() {
            // ...
        }
        
        /*
         * invoked when either of the options "-d" and "--decode" is 
         * encountered
         */        
        @OptionOccurrenceSink(
            optionBuilder = @OptionBuilder(
                doc = "decode data",
                name = "d",
                type = PosixOption.class
            ),
            otherOptionBuilders = {
                @OptionBuilder(
                    name = "decode", 
                    type = GnuLongOption.class
                )
            }
        )        
        public void setDecodingMode(boolean b) {
            // ...
        }
        
        /*
         * invoked when either of the options "-i" and "--ignore-garbage" 
         * is encountered
         */
        @OptionOccurrenceSink(
            optionBuilder = @OptionBuilder(
                doc = "when decoding, ignore non-alphabet characters",
                name = "i",
                type = PosixOption.class
            ),
            otherOptionBuilders = {
                @OptionBuilder(
                    name = "ignore-garbage", 
                    type = GnuLongOption.class
                )
            }
        )        
        public void setGarbageIgnored(boolean b) {
            // ...
        }
        
        /*
         * invoked when either of the options "-w" and "--wrap" is 
         * encountered
         */
        @OptionOccurrenceSink(
            optionBuilder = @OptionBuilder(
                doc = "wrap encoded lines after COLS character",
                name = "w",
                optionArgSpecBuilder = @OptionArgSpecBuilder(
                    name = "COLS"
                ),
                type = PosixOption.class
            ),
            otherOptionBuilders = {
                /*
                 * No need to specify an OptionArgSpecBuilder here. 
                 * When not specified, an OptionArgSpecBuilder is 
                 * provided from the above OptionBuilder.  
                 */
                @OptionBuilder(
                    name = "wrap", 
                    type = GnuLongOption.class
                )
            }
        )        
        public void setColumnLimit(Integer i) { 
            /*
             * Option argument for either of the options "-w" and 
             * "--wrap" is received as an argument from the above 
             * method parameter.
             *
             * Method parameter type must be a type or a java.util.List 
             * of a type that has a static String conversion method or 
             * a constructor with one String parameter.
             *
             * Alternatively, for any method parameter type, a class 
             * that extends StringConverter can be used to convert the 
             * option argument to that type. It can be supplied to 
             * OptionArgSpecBuilder.stringConverter(). 
             *
             * IllegalArgumentExceptions for the invalid option 
             * argument can be thrown here.
             */
            // ...
        }
        
        /*
         * invoked when a non-parsed argument is encountered. In this 
         * implementation, the non-parsed argument is the FILE argument.
         */
        @NonparsedArgSink
        public void setFile(String f) {
            /*
             * The non-parsed argument is received as an argument from 
             * the above method parameter.
             *
             * Method parameter type must be of type String.
             *
             * IllegalArgumentExceptions for the invalid non-parsed 
             * argument can be thrown here.
             */
            // ...
        }
        
    }
    
    public static void main(String[] args) {
        Base64Cli base64Cli = new Base64Cli();
        ParseResultSinkObject parseResultSinkObject =
            ParseResultSinkObject.newInstance(base64Cli);
        Options options = parseResultSinkObject.getOptions();
        ArgsParser argsParser = ArgsParser.newInstance(args, options, false);
        while (argsParser.hasNext()) {
            ParseResultHolder parseResultHolder = argsParser.parseNext();
            parseResultSinkObject.send(parseResultHolder);
        }
        // do post parsing stuff        
    }
    
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

**Complete customization of usage and help text for the options.** The customization of the usage and help text for a option can be applied to a particular option, to some options, or to all options. The following is an example based off the earlier example:

```java
    
    /*
     * provides the help text for an option and its other options on a 
     * single line instead of multiple lines
     */
    public static class CustomOptionHelpTextProvider extends OptionHelpTextProvider {
    
        public String getOptionHelpText(OptionHelpTextParams params) {
            StringBuilder sb = new StringBuilder();
            Iterator<OptionHelpTextParams> iterator = params.getAllOptionHelpTextParams().iterator();
            while (iterator.hasNext()) {
                OptionHelpTextParams p = iterator.next();
                sb.append(p.getUsage());
                if (iterator.hasNext()) {
                    sb.append(", ");
                }
            }
            return String.format("  %s\t\t\t\t%s", sb.toString(), params.getDoc());
        }
        
    }
    
    public static class Base64Cli {
    
        // ...
        
        @OptionOccurrenceSink(
            optionBuilder = @OptionBuilder(
                doc = "display version information and exit",
                name = "version",
                /*
                 * You can apply the OptionHelpTextProvider to a 
                 * particular option...
                 */ 
                optionHelpTextProvider = CustomOptionHelpTextProvider.class,
                type = GnuLongOption.class
            )
        )        
        public void displayVersion() {
            // ...
        }
        
        // ...
        
    }
    
    public static void main(String[] args) {
        // ...or you can apply the OptionHelpTextProvider to all options.
        OptionHelpTextProvider optionHelpTextProvider = new CustomOptionHelpTextProvider();
        OptionHelpTextProvider.setDefault(optionHelpTextProvider);
        // ...        
    }
    
```

**Single source code file.** As an alternative to importing ArgMatey as a Maven dependency or a jar file, ArgMatey can be imported to a project as a source code file.

The following are some examples of projects using ArgMatey:

-   [Jargyle](https://github.com/jh3nd3rs0n/jargyle) (specific examples: [SocksServerCli.java](https://github.com/jh3nd3rs0n/jargyle/blob/master/src/main/java/jargyle/server/SocksServerCli.java), [UsersCli.java](https://github.com/jh3nd3rs0n/jargyle/blob/master/src/main/java/jargyle/server/socks5/UsersCli.java))
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
