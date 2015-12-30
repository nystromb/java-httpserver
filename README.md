# Java HTTP Server

[![Build Status](https://travis-ci.org/scarvill91/java-httpserver.svg?branch=master)](https://travis-ci.org/scarvill91/java-httpserver)

A HTTP server compliant with the subset of the HTTP specification covered by the [Cob Spec](https://github.com/8thlight/cob_spec) test suite.

## Requirements
  * Maven 3.3
  * [Cob Spec](https://github.com/8thlight/cob_spec)
  * Java 1.8
  * jUnit 4.12
  * hamcrest 1.3

## Building the project
  1. Enter ```git clone --recursive https://github.com/scarvill91/java-httpserver.git``` in the command line.
  2. Navigate to the project root directory.
  3. Enter ```mvn package``` in the command line.

## Running the server
  1. From the project root directory, enter ```java -jar target/httpserver-1.0-SNAPSHOT.jar``` in the command line.

You can optionally specify the server's port with the flag '-p' and a directory from which to serve files with the flag '-d'. e.g. ```java -jar target/httpserver-1.0-SNAPSHOT.jar -p 5000 -d cob_spec/public```. The default port is 5000, and the default directory is 'cob_spec/public'.

## Testing the server

### Running unit tests
  1. From the project root directory, enter ```mvn test``` in the command line.

### Running Cob Spec tests
  1. Navigate to the project cob_spec directory.
  2. Enter ```mvn package``` in the command line.
  3. Enter ```java -jar fitnesse.jar -p 9090``` in the command line.
  4. Navigate to http://localhost:9090/HttpTestSuite in your browser.
  5. Click the 'Suite' button.
