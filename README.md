#ArgMatey

ArgMatey is a simple yet comprehensive Java command line argument parser framework that has the following features:

- API terminology and option syntax and behavior based on the [POSIX Utility Conventions](http://pubs.opengroup.org/onlinepubs/9699919799/basedefs/V1_chap12.html), GNU's function [getopt_long()](http://www.gnu.org/software/libc/manual/html_node/Getopt-Long-Options.html#Getopt-Long-Options), and GNU's [Argp](http://www.gnu.org/software/libc/manual/html_node/Argp.html#Argp) interface
 
- Option types:
 
  - POSIX options (examples: `-h -v -o file.txt`)
    
  - Long options (examples: `-help -version -output-file file.txt`)
    
  - GNU long options (examples: `--help --version --output-file=file.txt`)
     
- Incremental command line argument parsing (similar to getopt(), getopt_long(), and Argp)
 
- Complete customization of usage and help text for the options

## Requirements

- Apache Maven&#8482; 3.3.9 or higher 

- Java&#8482; SDK 1.6 or higher

## Installation

To install, run the following commands:

`$ cd argmatey`

`$ mvn install`