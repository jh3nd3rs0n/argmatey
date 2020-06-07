# ArgMatey

ArgMatey is a comprehensive Java command line argument parsing library that has the following features:

**API terminology and option syntax and behavior based on the [POSIX Utility Conventions](http://pubs.opengroup.org/onlinepubs/9699919799/basedefs/V1_chap12.html), GNU's function [getopt_long](http://www.gnu.org/software/libc/manual/html_node/Getopt-Long-Options.html#Getopt-Long-Options), and GNU's [Argp](http://www.gnu.org/software/libc/manual/html_node/Argp.html#Argp) interface.**
 
**Option types**:
 
- POSIX options (examples: `-h` `-v` `-o file.txt`)
- Long options (examples: `-help` `-version` `-output-file file.txt`)
- GNU long options (examples: `--help` `--version` `--output-file=file.txt`)
     
**Iterative command line argument parsing (similar to getopt, getopt_long, and Argp).** This is useful for interpreting the following:

- Multiple instances of the same option
- Multiple instances of options from the same group
- The order of the provided options and arguments 
 
**Complete customization of usage and help text for the options.** This is useful for retaining the format of the usage and help text generated from an earlier command line argument parsing library.

**Single source code file.** This is useful for importing ArgMatey to a project as a source code file instead of importing ArgMatey as a Maven dependency or a jar file.

The following are some examples of projects using ArgMatey:

- [Jargyle](https://github.com/jh3nd3rs0n/jargyle)
- [JBase64Transformer](https://github.com/jh3nd3rs0n/jbase64transformer)

## Contents

- [Requirements](#requirements)
- [Installing](#installing)
- [Building](#building)

## Requirements

- Apache Maven&#8482; 3.3.9 or higher (for installing and building) 
- Java&#8482; SDK 1.6 or higher

## Installing

To install, run the following commands:

```

    $ cd argmatey
    $ mvn install

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

```

    $ mvn package

```
