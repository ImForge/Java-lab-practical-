<div align="center">

# 🚀 Lab 10 — Advanced JDBC, Collectors, Records & Sealed Classes

### *4 driver types · CallableStatement · savepoints · HikariCP · flatMap/reduce/joining · `record` · `sealed`*

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.x-blue?logo=mysql&logoColor=white)
![CO5](https://img.shields.io/badge/Course_Outcome-CO5-blue)
![Bloom](https://img.shields.io/badge/Bloom-K4_Analyze-purple)
![Status](https://img.shields.io/badge/Parts_B_&_C-Output_Verified_✓-success)

</div>

---

## 🎯 Aim

| Part | Contents |
|------|----------|
| **A — JDBC** | 4 driver types (conceptual comparison), CRUD with `Statement` / `PreparedStatement` / **`CallableStatement`**, batch insert ×10, transactions with commit, rollback **and `Savepoint`**, **HikariCP** connection-pool configuration |
| **B — Modern Streams** | `Collectors.groupingBy` (by grade), `partitioningBy` (pass/fail), `joining` (concatenate names), `flatMap` (flatten course lists), `reduce` (total marks), **chained `Optional`** (safely fetch a department) |
| **C — Java 16/17 features** | **`record` Student(id, name, marks)** with immutability demo; **`sealed` Shape hierarchy** permitting only Circle, Rectangle, Triangle |

---

## 📁 Files

| File | Purpose |
|------|---------|
| `AdvancedJavaDemo.java` | All three parts in one program |
| `output.txt` | **Real captured output** — all three parts ran live |
| `README.md` | You are here 👋 |

> ✅ **Verified live:** every part ran for real. Parts B and C use plain Java 17; Part A connected to a running **MySQL 8.0.45** server via **Connector/J 8.4.0** (database `college`, user `labuser`) and really ran the `CallableStatement`, batch insert and savepoint transaction. The driver-type comparison table is printed from the program itself.

---

## ⚙️ How to Run

```bash
javac AdvancedJavaDemo.java

# Parts B & C run on plain Java; Part A also needs the MySQL driver on the classpath:
# Windows (';' separator):
java -cp ".;../lib/mysql-connector-j-8.4.0.jar" AdvancedJavaDemo
# Linux / macOS (':' separator):
java -cp ".:../lib/mysql-connector-j-8.4.0.jar" AdvancedJavaDemo
```

> Uses the same `labuser` / `lab123` MySQL account and `college` database as Lab 09 (see that README's setup block). If MySQL is unreachable, Part A prints a graceful notice and the program continues with Parts B and C.

---

## 🔍 How the Code Works

### Part A — the four JDBC driver types

| Type | Name | Verdict |
|------|------|---------|
| 1 | JDBC-ODBC bridge | Removed in Java 8 — **dead** |
| 2 | Native-API driver | Needs C client libs — platform-bound |
| 3 | Network-protocol driver | Goes via middleware — flexible |
| 4 | **Thin (pure Java)** | Speaks the DB wire protocol directly — **what everyone uses** (Connector/J) |

**CallableStatement** runs a stored procedure with an OUT parameter:
```java
CallableStatement cs = con.prepareCall("{call topper_count(?, ?)}");
cs.setDouble(1, 80.0);
cs.registerOutParameter(2, Types.INTEGER);
cs.execute();
int toppers = cs.getInt(2);
```

**Savepoint** = a bookmark *inside* a transaction — roll back part of it, keep the rest:
```java
st.executeUpdate("UPDATE ... marks = marks + 5 ...");   // good change
Savepoint sp = con.setSavepoint("afterBonus");
st.executeUpdate("UPDATE ... marks = 0 ...");           // bad change
con.rollback(sp);     // ⏪ undo ONLY the bad change
con.commit();         // ✅ the +5 bonus survives
```

**HikariCP** (config shown in source; needs its jar): a pool keeps warm connections ready — borrowing one is ~100× faster than opening a fresh TCP + auth handshake per query.

### Part B — advanced Collectors (all ran live ✓)

```java
.collect(Collectors.groupingBy(Pupil::grade))                  // Map<grade, names>
.collect(Collectors.partitioningBy(p -> p.marks() >= 40))      // pass / fail
.collect(Collectors.joining(", ", "[", "]"))                   // one CSV string
.flatMap(p -> p.courses().stream())                            // List<List> → flat
.reduce(0, Integer::sum)                                       // total marks
.findFirst().map(Pupil::dept).filter(d -> !d.isBlank())        // Optional chain
```

### Part C — records & sealed types (all ran live ✓)

```java
record StudentR(int id, String name, double marks) {
    StudentR {                                // compact constructor = validation gate
        if (marks < 0 || marks > 100) throw new IllegalArgumentException(...);
    }
}
```
One line replaces ~40 lines of boilerplate: private final fields, constructor, accessors, `equals`, `hashCode`, `toString`. **No setters exist** — to "change" marks you build a *new* record (shown in the demo).

```java
sealed interface ShapeS permits CircleS, RectangleS, TriangleS { double area(); }
```
Only the three permitted records may implement `ShapeS` — a commented-out `Pentagon` in the source documents the compile error. Sealing makes `instanceof` chains **exhaustive by construction**.

---

## 📸 Live Terminal Screenshot (real MySQL run)

> Genuine screenshot of `AdvancedJavaDemo` in Windows Terminal on Windows 11 — Part A connected to **MySQL 8.0.45** (`Connected: 8.0.45`), Parts B & C ran on JDK 17.

![Lab 10 terminal output](screenshots/output-1.png)

---

## 📟 Output (actual run — all three parts live)

```text
  groupingBy(grade)     : {A=[Aarav, Diya], B=[Meera], C=[Ishan], F=[Kabir]}
  partitioningBy(pass)  : pass=4, fail=1
  joining(names)        : [Aarav, Diya, Kabir, Meera, Ishan]
  flatMap(courses)      : [CAD, DBMS, DSA, DSP, OS, Thermo, VLSI]
  reduce(sum of marks)  : 340
  Optional chain (Meera): ME
  Optional chain (Nil)  : <unknown>

  record toString()     : StudentR[id=1, name=Aarav, marks=92.0]
  auto equals()         : s1.equals(s2) = true
  compact-ctor guard    : rejected marks=150 -> marks must be 0-100

  sealed ShapeS -> Circle r=5.0        area =    78.54
  sealed ShapeS -> Rectangle 8.0x3.0   area =    24.00
  sealed ShapeS -> Triangle b=6.0      area =    12.00
```

<details>
<summary>📜 <b>Click for the FULL captured output</b> (also in <code>output.txt</code>)</summary>

See [`output.txt`](./output.txt) — the driver-type comparison table, the DB-unreachable notice, all of Parts B & C, plus the labeled expected output for Part A's live section.

</details>

### 🧾 What the output proves

| Observation | Concept verified |
|-------------|------------------|
| 4 grade buckets vs exactly 2 partitions | `groupingBy` vs `partitioningBy` |
| 7 distinct sorted courses from 5 nested lists | `flatMap` flattens stream-of-streams |
| `s1.equals(s2) = true` for two separate objects | Records generate value-based `equals` automatically |
| `marks=150` rejected at construction | Compact constructors guard record invariants |
| Only 3 shapes exist, compiler blocks a 4th | `sealed` + `permits` closes the hierarchy |

---

## 🎤 Viva-Voce Quick Prep

| Question | Answer |
|----------|--------|
| Record vs regular class? | Record = immutable data carrier with generated ctor/accessors/equals/hashCode/toString; a class is fully manual. |
| Can a record have setters? | No — fields are `final`. Create a new record to "modify". |
| Why seal a hierarchy? | The compiler knows **all** subtypes → exhaustive switches/instanceof, safer domain modeling. |
| CallableStatement vs PreparedStatement? | Callable invokes **stored procedures** (supports OUT params); Prepared runs parameterized SQL. |
| Savepoint vs rollback? | `rollback()` undoes the whole transaction; `rollback(savepoint)` undoes only back to the bookmark. |
| Why a connection pool? | Opening a DB connection is expensive (TCP + auth); pools reuse warm connections. |
