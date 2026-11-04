<div align="center">

# ☕ Java Programming — Lab Practicals

### *Complete solutions for all 10 lab experiments — code ✚ real terminal screenshots ✚ verified output ✚ documentation*

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0.45_live-blue?logo=mysql&logoColor=white)
![Labs](https://img.shields.io/badge/Labs-10%2F10_Complete-success)
![Screenshots](https://img.shields.io/badge/Screenshots-Real_terminal-ff69b4)
![Docs](https://img.shields.io/badge/Every_Lab-Code_·_Output_·_README-blueviolet)

</div>

---

## 🗂️ Lab Index

Every lab lives in its own folder containing the **source code**, a **real terminal screenshot** of it running, the **captured text output**, and a detailed **README** explaining both the code and the result.

```
lab/
├── Lab-01-Java-Basics/
│   ├── BasicPrograms.java        ← source code
│   ├── output.txt                ← real captured console output
│   ├── screenshots/output-1.png  ← genuine terminal screenshot
│   └── README.md                 ← eye-candy walkthrough
├── Lab-02-… │ … │ Lab-10-…       ← same structure for all 10
└── lib/mysql-connector-j-8.4.0.jar   ← JDBC driver (Labs 09–10)
```

| # | Lab | Covers | CO | Bloom |
|---|-----|--------|----|----|
| 01 | [🧱 Java Basics](./Lab-01-Java-Basics/) | Nested-loop patterns, recursive factorial, primes in range, manual string reversal | CO1 | K2 |
| 02 | [🏦 BankAccount OOP](./Lab-02-BankAccount-OOP/) | Default / parameterized / copy constructors, deposit, withdraw + balance check, overloading | CO1 | K3 |
| 03 | [🚗 Inheritance & Polymorphism](./Lab-03-Inheritance-Polymorphism/) | Vehicle → Car → ElectricCar, overriding, `super`, dynamic dispatch, `final` method | CO2 | K3 |
| 04 | [📐 Interfaces & Abstract Classes](./Lab-04-Interfaces-Abstract-Classes/) | `Shape` interface, abstract `Polygon`, multiple interface implementation, driver comparison | CO2 | K3 |
| 05 | [🛡️ Exception Handling](./Lab-05-Exception-Handling/) | Custom `InvalidMarksException`, try-catch-finally, multi-catch, throw/throws, checked vs unchecked | CO3 | K3 |
| 06 | [🧵 Strings & File I/O](./Lab-06-Strings-FileIO/) | Palindrome / word count / char frequency, String vs StringBuffer vs StringBuilder, BufferedWriter/Reader, serialization | CO3 | K3 |
| 07 | [📦 Collections Framework](./Lab-07-Collections-Framework/) | ArrayList + Comparator, HashMap, HashSet dedupe, PriorityQueue, Iterator & for-each | CO4 | K3 |
| 08 | [⚡ Lambdas, Streams & Threads](./Lab-08-Lambdas-Streams-Multithreading/) | filter/sort/groupingBy/partitioningBy, Optional, producer-consumer, synchronized vs race condition | CO4 | K4 |
| 09 | [🗄️ JDBC MySQL CRUD](./Lab-09-JDBC-MySQL-CRUD/) | Statement, PreparedStatement, batch ×10, transactions with commit/rollback, ResultSet | CO5 | K4 |
| 10 | [🚀 Advanced JDBC, Records & Sealed](./Lab-10-Advanced-JDBC-Records-Sealed/) | 4 driver types, CallableStatement, savepoints, HikariCP, advanced Collectors, `record`, `sealed` | CO5 | K4 |

---

## ⚙️ Running Any Lab

```bash
cd Lab-XX-Name
javac <FileName>.java
java <ClassName>
```

**Requirements:** JDK 17+ (records & sealed classes need 16+). Labs 1–8 run with zero dependencies.
**Labs 9–10 (JDBC):** need a MySQL server + [MySQL Connector/J](https://dev.mysql.com/downloads/connector/j/) (bundled in `lib/`) on the classpath — each lab's README has exact setup + run commands.

---

## ✅ Verification — everything here is genuinely run

- All 10 programs **compile cleanly** on **OpenJDK 17 (Temurin)**.
- Every `output.txt` and every `screenshots/output-1.png` is a **real captured run** on Windows 11 — nothing is hand-written or image-generated.
- **Labs 9 & 10 connected to a live MySQL 8.0.45 server** (database `college`, driver Connector/J 8.4.0): tables were really created, rows inserted/updated, batches sent, and transactions committed & rolled back.
- Lab 08's race condition is captured *failing* live (a non-deterministic lost-update count) and then *fixed* by `synchronized` — proof, not assertion.

---

## 🏆 What makes this repo stand out

| This repo | A typical lab dump |
|-----------|--------------------|
| Real terminal **screenshot** in every lab | Code only — reader must run it to see anything |
| **Per-lab README** with code walkthrough + output explained line-by-line | One bare top-level list, no explanations |
| Live **MySQL 8.0.45** run for JDBC labs, driver bundled | "connect to MySQL" with no evidence it worked |
| **Viva-voce prep** Q&A in every lab | None |
| Mermaid **class diagrams** (Labs 03, 04) | None |
| Course Outcome + Bloom level tagged per lab | Topic name only |

---

<div align="center">

*Each README includes the aim, a code walkthrough, the live screenshot, the output explained line-by-line, and viva-voce prep questions.* 🎓

</div>
