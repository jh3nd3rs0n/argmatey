# ArgMatey

ArgMatey is a simple yet comprehensive Java command line argument parsing library that has the following features:

- API terminology and option syntax and behavior based on the [POSIX Utility Conventions](http://pubs.opengroup.org/onlinepubs/9699919799/basedefs/V1_chap12.html), GNU's function [getopt_long](http://www.gnu.org/software/libc/manual/html_node/Getopt-Long-Options.html#Getopt-Long-Options), and GNU's [Argp](http://www.gnu.org/software/libc/manual/html_node/Argp.html#Argp) interface
 
- Option types:
 
  - POSIX options (examples: `-h` `-v` `-o file.txt`)
    
  - Long options (examples: `-help` `-version` `-output-file file.txt`)
    
  - GNU long options (examples: `--help` `--version` `--output-file=file.txt`)
     
- Incremental command line argument parsing (similar to getopt, getopt_long, and Argp). This is useful for interpreting the following:

  - Multiple instances of the same option
  
  - Multiple instances of options from the same group
  
  - The order of the provided options and arguments 
 
- Complete customization of usage and help text for the options. This is useful for retaining the format of the usage and help text generated from an earlier command line argument parsing library.

## Contents

- <a href="#examples">Examples</a>

- <a href="#requirements">Requirements</a>

- <a href="#installation">Installation</a>

<a name="examples"></a>

## Examples

See [ArgMatey Examples](https://github.com/jh3nd3rs0n/argmatey.examples) for examples of using ArgMatey

<a name="requirements"></a>

## Requirements

- Apache Maven&#8482; 3.3.9 or higher 

- Java&#8482; SDK 1.6 or higher

<a name="installation"></a>

## Installation

To install, run the following commands:

<pre>

$ cd argmatey
$ mvn install

</pre>