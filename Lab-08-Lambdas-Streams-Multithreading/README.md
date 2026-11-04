<div align="center">

# ⚡ Lab 08 — Java 8 Features & Multithreading

### *Lambdas & Streams · Optional · Producer-Consumer · synchronized vs race condition*

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![CO4](https://img.shields.io/badge/Course_Outcome-CO4-blue)
![Bloom](https://img.shields.io/badge/Bloom-K4_Analyze-purple)
![Status](https://img.shields.io/badge/Output-Verified_✓-success)

</div>

---

## 🎯 Aim

| # | Task |
|---|------|
| 1 | **Lambda expressions + Stream API** — `filter`, `sorted`, `groupingBy`, `partitioningBy` on student objects |
| 2 | **`Optional`** — handle absent values with zero `NullPointerException` risk |
| 3 | **Producer–Consumer** — `Thread` class + `Runnable` interface with `wait()/notifyAll()` |
| 4 | **`synchronized` block** — first *show* a race condition on a shared bank balance, then *fix* it |

---

## 📁 Files

| File | Purpose |
|------|---------|
| `ModernJavaDemo.java` | All four parts (shared buffer + shared account as nested classes) |
| `output.txt` | Actual captured program output |
| `README.md` | You are here 👋 |

---

## ⚙️ How to Run

```bash
javac ModernJavaDemo.java
java ModernJavaDemo
```

---

## 🔍 How the Code Works

### 1️⃣ Streams — declarative data crunching

```java
students.stream()
        .filter(st -> st.marks() >= 60)                          // lambda predicate
        .sorted(Comparator.comparingInt(StudentRec::marks).reversed())
        .forEach(st -> System.out.printf(...));

Collectors.groupingBy(StudentRec::dept)        // Map<dept, students>
Collectors.partitioningBy(st -> st.marks() >= 40)   // Map<Boolean, …>  pass/fail
```

> 💡 `groupingBy` makes **N buckets** (one per key); `partitioningBy` always makes **exactly 2** (true/false). That contrast is the point of running both.

### 2️⃣ `Optional` — absence as a type

```java
students.stream().filter(st -> st.dept().equals("CIVIL"))
        .findFirst()                     // Optional<StudentRec> — maybe empty!
        .map(StudentRec::name)           // runs ONLY if present
        .orElse("<no CIVIL student found>");   // safe fallback
```
No null checks, no NPE — the empty case flows through `map` harmlessly and lands on `orElse`.

### 3️⃣ Producer–Consumer — the classic coordination problem

```java
synchronized void produce(int item) throws InterruptedException {
    while (buffer.size() == CAPACITY) wait();   // full → sleep until notified
    buffer.add(item);
    notifyAll();                                // wake the consumer
}
```
A capacity-3 buffer, a fast producer (30 ms) and a slow consumer (70 ms): the output shows the buffer filling to 3 and the producer **blocking** until space frees up. `while` (not `if`) around `wait()` guards against spurious wakeups.

### 4️⃣ Race condition → `synchronized` fix

```java
void depositUnsafe() { balance = balance + 1; }          // read-modify-write ≠ atomic!

void depositSafe() {
    synchronized (lock) { balance = balance + 1; }        // one thread at a time
}
```
Two threads × 100,000 deposits each should give 200,000.

---

## 📸 Live Terminal Screenshot

> Genuine screenshot of `ModernJavaDemo` compiled with `javac` and run in Windows Terminal on Windows 11 (JDK 17) — not a mock-up. In this capture the unsynchronized total came out **120165** (it differs every run — that nondeterminism *is* the race condition); the synchronized total is always exactly 200000.

![Lab 08 terminal output](screenshots/output-1.png)

---

## 📟 Output (actual run)

```text
  filter(marks >= 60) + sorted by marks desc:
    Aarav   92  CSE
    Meera   81  ME
    Zara    74  CSE
    Diya    67  ECE

  groupingBy(dept)        : {CSE=[Aarav, Kabir, Zara], ECE=[Diya, Ishan], ME=[Meera]}
  partitioningBy(>= 40)   : PASS=[Aarav, Diya, Meera, Ishan, Zara]  FAIL=[Kabir]

  CIVIL student present?  : false
  map().orElse() fallback : <no CIVIL student found>

    [Producer] produced item-1  (buffer size: 1)
    [Consumer] consumed item-1 (buffer size: 0)
    [Producer] produced item-2  (buffer size: 1)
    [Producer] produced item-3  (buffer size: 2)
    ...

  WITHOUT synchronized : expected 200000, got 121230  <-- updates were LOST
  WITH synchronized    : expected 200000, got 200000  <-- every update counted
```

<details>
<summary>📜 <b>Click for the FULL captured output</b> (also in <code>output.txt</code>)</summary>

See [`output.txt`](./output.txt) — the full stream results, both Optional cases, the complete producer/consumer interleaving and both race-condition rounds.

</details>

### 🧾 What the output proves

| Observation | Concept verified |
|-------------|------------------|
| Kabir (38) missing from the filtered list | `filter` + lambda predicates work |
| 3 dept buckets vs exactly 2 pass/fail buckets | `groupingBy` vs `partitioningBy` |
| `<no CIVIL student found>` instead of a crash | `Optional` chains make absence safe |
| Producer/consumer lines interleave, buffer never exceeds 3 | `wait()/notifyAll()` coordination works |
| **121,230 ≠ 200,000** unsafe, **200,000 = 200,000** safe | The race condition is *real*, and `synchronized` fixes it |

> ⚠️ The unsafe number (121,230 here) **changes every run** — that nondeterminism is itself evidence of a race condition. The synchronized result is always exactly 200,000.

---

## 🎤 Viva-Voce Quick Prep

| Question | Answer |
|----------|--------|
| What is a functional interface? | An interface with exactly one abstract method (`Runnable`, `Comparator`) — the target of a lambda. |
| Why `while` around `wait()`? | Spurious wakeups + multiple waiters: the condition must be re-checked after waking. |
| Why does `balance + 1` race? | It's three steps (read, add, write); threads can interleave between them and overwrite each other. |
| `start()` vs `run()`? | `start()` spawns a new thread which calls `run()`; calling `run()` directly executes on the *current* thread. |
