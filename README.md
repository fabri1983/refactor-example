# Refactoring a Java program: JobLogger

[![Build Status](https://app.travis-ci.com/fabri1983/refactor-example.svg?branch=master)](https://app.travis-ci.com/fabri1983/refactor-example?branch=master)
&nbsp;&nbsp;&nbsp;&nbsp;
[![Coverage Status](https://coveralls.io/repos/github/fabri1983/refactor-example/badge.svg)](https://coveralls.io/github/fabri1983/refactor-example?branch=master)
&nbsp;&nbsp;&nbsp;&nbsp;
[![Code Climate](https://codeclimate.com/github/fabri1983/refactor-example/badges/gpa.svg)](https://codeclimate.com/github/fabri1983/refactor-example)


This project is a technical assesment in which the developer has to show its refactor skills, design patterns knowledge 
and good practices for clean code.


- Supports JVM 8 only by the moment. I'm having issues with the use of EasyMock and its CGLIB internal api when running on JVM 9 and higher.

- Uses Maven 3.6.x for project setup and building. You can use `mvnw` if you don't have Maven installed in your host.

- Class `org.fabri1983.refactorexample.joblogger.JobLogger` is the target class to be refactored.
  - You can notice that the first line of method `JobLogger.logMessage()` will throw a NullPointerException if message object is null. 
  I think compilers should show you a warning. (I think you can configure your IDE settings for such scenarios).
  - The `JobLogger.logMessage()` method tries to connect to a database and also creates some I/O handlers no matter what's the output target.
  - It also has a very complex logic with conditional statements everywhere.
  - It defines a list of boolean parameters acting as flags letting you control the internal behavior of the method, which turns it into a white box.
  - Cohesiveness is very low since it tries to do many unrelated things: creation of I/O handlers and database must be threated at configuration/creation level.
  - Is not thread-safe: given the nature of static variables as global state holders, concurrent access results in unpredictable behavior.
  - Possible SQL Injection: the creation of a Statement using a query string build up from concatenation of several strings may lead to injection of unexpected queries.
  
- Classes on package `org.fabri1983.refactorexample.joblogger.enhanced` are the result after applying a refactor. Resulting core classes:
  - `ConsoleJobLogger`: logs to StdErr.
  - `FileJobLogger`: logs to a file.
  - `DatabaseJobLogger`: logs to a database.
  - `CompoundJobLogger`: allows creation of one logger compound by 2 or more loggers.
  - Many other classes and interfaces.

- Good practices were took into consideration, and some fixes as well:
  - Modularization.
  - Meaningful variable and method names. Also keeping code conventions for class/variable/method names.
  - Avoid static variables, since they are cumbersome for testing and may lead to irreproducible bugs on concurrent scenarios.
  - Use interfaces to define contracts and then implementing different behaviours.
  - Using enums to narrow variable values.
  - Encapsulate long list of parameters into a context or configuration object.
  - Use meaningful exception classes to better understand what is wrong under the hood.
  - Delay object creation if you don't need them.
  - Static factory method and builder design patterns.
  - Dependency Injection (for example a Connection object).
  - Close statements after use to release resources. Use *try with resources* idiom.
  - PreparedStatement allows to pre compile the target sql query to avoid SQL injection.

- JUnit tests runs both versions of the program.
  - Using Maven Surefire plugin with default group `AllLoggersCategoryTest` runs all tests. See next section.
  - Using Given-When-Then idiom.
  - Using parameterized test.
  - Using mocking frameworks.
 

## Running Tests

- Testing initial version of `JobLogger`:  
Tests are separated in two classes: `JobLoggerTest` and `JobLoggerTestWithMocks`. That way we test different behaviors.
You can see in both test how complex testing gets when dealing with static variables and no modularization at all on `JobLogger`.  
The use of mock frameworks need sometimes a lot of ceremony.
```sh
mvn clean test -P oldJobLoggerTests
```

- Testing the refactored version `IEnhancedJobLogger`:  
Tests are located in classes: `ConsoleJobLoggerTest`, `FileJobLoggerTest`, and `DatabaseJobLoggerTest`.
```sh
mvn clean test -P enhancedJobLoggerTests
```

- Testing supporting classes:  
Tests are located in classes: `StandardConsoleRedirectorTest` and `XmlDomUtilTest`.
```sh
mvn clean test -P supportingTests
```

- Execution of all unit tests:
By default profile `allTests` is activated.
```sh
mvn clean test
```


## OWASP Dependency Checker
Run next command to check if any dependency has a security risk according the Maven plugin *dependency-checker* from **OWASP**:  
```sh
mvn verify -P securitycheck
```

