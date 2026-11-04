# Lab 09 - JDBC CRUD with MySQL

A Java program that connects to MySQL, creates a `student` table, and runs full CRUD four ways: a plain `Statement`, a `PreparedStatement`, a batch insert, and a transaction with commit and rollback. Rows are displayed with a `ResultSet` after each stage.

Course outcome CO5 (database-connected Java applications with JDBC), Bloom level K4.

It connects to MySQL 8.0.45 (database `college`, user `labuser`) through Connector/J 8.4.0.

## Files

- `StudentJDBC.java` - the full program, all four techniques
- `output.txt` - the program output
- `screenshots/output-1.png` - screenshot of the output

## Setup and running

In MySQL, once:

```sql
CREATE DATABASE college;
CREATE USER 'labuser'@'localhost' IDENTIFIED BY 'lab123';
GRANT ALL PRIVILEGES ON college.* TO 'labuser'@'localhost';
FLUSH PRIVILEGES;
```

Download the MySQL Connector/J driver (`mysql-connector-j-8.4.0.jar`) from [dev.mysql.com](https://dev.mysql.com/downloads/connector/j/), then compile and run with it on the classpath:

```
javac StudentJDBC.java
java -cp ".;mysql-connector-j-8.4.0.jar" StudentJDBC
```

On Linux or macOS use `:` instead of `;` in the classpath. Adjust the path to the jar to wherever you saved it. The connection URL has `?createDatabaseIfNotExist=true` so the schema is created automatically if it's missing. Edit the `USER` and `PASS` constants at the top of the file to match your own MySQL login.

## How the code works

**Statement.** Used for the fixed SQL - creating the table and one plain insert. Fine for one-off queries, but values get glued straight into the SQL string, which is where SQL injection comes from.

**PreparedStatement.** The SQL is written once with `?` placeholders and the values are supplied separately as typed parameters. It's precompiled, so it's faster on repeats, and the values are treated as data rather than SQL, so injection isn't possible.

**Batch.** Ten inserts are queued with `addBatch()` and sent together with `executeBatch()`, which returns an array of per-row update counts - one network round trip instead of ten.

**Transaction.** With `setAutoCommit(false)`, the code deducts 10 marks from one student and adds 10 to another, then commits - both updates happen together. A second transfer deliberately throws partway through, and `rollback()` undoes the uncommitted deduction. That's the all-or-nothing property of a transaction.

## Screenshot

![Lab 09 running against MySQL](screenshots/output-1.png)

## Output

```text
Connected to MySQL: 8.0.45

 1. Statement - CREATE TABLE + simple INSERT/SELECT
  Table 'student' created.

 2. PreparedStatement - parameterized INSERT and UPDATE
  2 rows inserted via placeholders (?) - SQL-injection safe.
  PreparedStatement UPDATE -> 1 row (Kabir 45.0 -> 50.5).

 3. BATCH - 10 records inserted in ONE round trip
  executeBatch() returned 10 results -> total rows inserted: 10

 4. TRANSACTION - transfer 10 marks, commit; then rollback demo
  Transfer of 10 marks (id 2 -> id 3) COMMITTED.
  ERROR: Simulated failure before crediting the receiver!
  ROLLBACK executed - id 1's marks are unchanged.

  --- Final state of ids 1-3 (committed transfer + rollback proof) ---
  1   Aarav Sharma   88.5     <- rollback worked (the -20 was undone)
  2   Diya Patel     82.0     <- gave 10 marks  (92.0 -> 82.0)
  3   Kabir Singh    60.5     <- received 10    (50.5 -> 60.5)
```

Diya drops from 92 to 82 and Kabir rises from 50.5 to 60.5, so the committed transfer moved exactly 10 marks between two real rows. Aarav stays at 88.5 even though 20 was deducted mid-transaction, which shows the rollback undid the uncommitted change. The batch really added all ten rows.
