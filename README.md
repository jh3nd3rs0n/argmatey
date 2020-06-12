# ArgMatey

[![Total alerts](https://img.shields.io/lgtm/alerts/g/jh3nd3rs0n/argmatey.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/argmatey/alerts/) [![Language grade: Java](https://img.shields.io/lgtm/grade/java/g/jh3nd3rs0n/argmatey.svg?logo=lgtm&logoWidth=18)](https://lgtm.com/projects/g/jh3nd3rs0n/argmatey/context:java) [![Codacy Badge](https://app.codacy.com/project/badge/Grade/715ef579e39647b4ab3d4c9dc9c12a7b)](https://www.codacy.com/manual/jh3nd3rs0n/argmatey?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=jh3nd3rs0n/argmatey&amp;utm_campaign=Badge_Grade)

ArgMatey is a comprehensive Java command line argument parsing library that has the following features:

**Option syntax and behavior based on the [POSIX Utility Conventions](http://pubs.opengroup.org/onlinepubs/9699919799/basedefs/V1_chap12.html), GNU's function [getopt_long](http://www.gnu.org/software/libc/manual/html_node/Getopt-Long-Options.html#Getopt-Long-Options), and GNU's [Argp](http://www.gnu.org/software/libc/manual/html_node/Argp.html#Argp) interface.** The types of options you can define and use are familiar and readily understood by many.
 
**Option types:**
 
-   POSIX options (examples: `-h` `-v` `-o file.txt`)

-   Long options (examples: `-help` `-version` `-output-file file.txt`)

-   GNU long options (examples: `--help` `--version` `--output-file=file.txt`)
     
**Iterative command line argument parsing (similar to getopt, getopt_long, and Argp).** This style of command line argument parsing has the following advantages:

-   Interpretation of multiple instances of the same option
-   Interpretation of multiple instances of options from the same group
-   Interpretation of options and arguments based on the ordering provided
-   Intentional interruption at any point in the parsing of the command line arguments  

**Complete customization of usage and help text for the options.** Any element of the usage and help text for the options can be customized.

**Single source code file.** As an alternative to importing ArgMatey as a Maven dependency or a jar file, ArgMatey can be imported to a project as a source code file.

The following are some examples of projects using ArgMatey:

-   [Jargyle](https://github.com/jh3nd3rs0n/jargyle)
-   [JBase64Transformer](https://github.com/jh3nd3rs0n/jbase64transformer)

## Contents

-   [Requirements](#requirements)
-   [Installing](#installing)
-   [Building](#building)

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
