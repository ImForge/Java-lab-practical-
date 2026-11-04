# Java Programming Lab

Solutions for all ten Java lab experiments. Each lab sits in its own folder with the source code, a screenshot of it running, the captured console output, and a README explaining the code and the result.

The programs were compiled and run with JDK 17 on Windows 11, and each lab folder has a screenshot of its output. Labs 9 and 10 use MySQL 8.0.45 through the JDBC driver.

## Folder layout

```
lab/
├── Lab-01-Java-Basics/
│   ├── BasicPrograms.java        source code
│   ├── output.txt                captured console output
│   ├── screenshots/output-1.png  terminal screenshot
│   └── README.md                 explanation
├── Lab-02 ... Lab-10             same structure
└── lib/mysql-connector-j-8.4.0.jar   JDBC driver for labs 9-10
```

## The experiments

| # | Lab | Topics | CO | Bloom |
|---|-----|--------|----|----|
| 01 | [Java Basics](./Lab-01-Java-Basics/) | nested-loop patterns, recursive factorial, primes in a range, manual string reversal | CO1 | K2 |
| 02 | [BankAccount (OOP)](./Lab-02-BankAccount-OOP/) | default/parameterized/copy constructors, deposit, withdraw with balance check, constructor overloading | CO1 | K3 |
| 03 | [Inheritance & Polymorphism](./Lab-03-Inheritance-Polymorphism/) | multilevel inheritance, overriding, `super`, dynamic dispatch, `final` methods | CO2 | K3 |
| 04 | [Interfaces & Abstract Classes](./Lab-04-Interfaces-Abstract-Classes/) | `Shape` interface, abstract `Polygon`, multiple interfaces, interface vs abstract class | CO2 | K3 |
| 05 | [Exception Handling](./Lab-05-Exception-Handling/) | custom exception, try-catch-finally, multi-catch, throw/throws, checked vs unchecked | CO3 | K3 |
| 06 | [Strings & File I/O](./Lab-06-Strings-FileIO/) | palindrome/word count/char frequency, String vs StringBuffer vs StringBuilder, buffered I/O, serialization | CO3 | K3 |
| 07 | [Collections Framework](./Lab-07-Collections-Framework/) | ArrayList with Comparator, HashMap, HashSet, PriorityQueue, iterators | CO4 | K3 |
| 08 | [Lambdas, Streams & Threads](./Lab-08-Lambdas-Streams-Multithreading/) | stream operations, Optional, producer-consumer, race condition vs `synchronized` | CO4 | K4 |
| 09 | [JDBC CRUD](./Lab-09-JDBC-MySQL-CRUD/) | Statement, PreparedStatement, batch insert, transactions with commit/rollback, ResultSet | CO5 | K4 |
| 10 | [Advanced JDBC, Records & Sealed](./Lab-10-Advanced-JDBC-Records-Sealed/) | driver types, CallableStatement, savepoints, advanced collectors, records, sealed classes | CO5 | K4 |

## Running a lab

```
cd Lab-XX-Name
javac FileName.java
java ClassName
```

Labs 1-8 need nothing but a JDK (17 or newer, since records and sealed classes arrived in Java 16/17). Labs 9 and 10 need a running MySQL server and the Connector/J driver on the classpath - the driver is in `lib/`, and each of those two READMEs has the exact setup and run commands.
