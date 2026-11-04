<div align="center">

# 🛡️ Lab 05 — Exception Handling (Student Grade System)

### *Custom `InvalidMarksException` · try-catch-finally · multi-catch · throw/throws · checked vs unchecked*

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![CO3](https://img.shields.io/badge/Course_Outcome-CO3-blue)
![Bloom](https://img.shields.io/badge/Bloom-K3_Apply-green)
![Status](https://img.shields.io/badge/Output-Verified_✓-success)

</div>

---

## 🎯 Aim

Simulate a student grade system demonstrating:

- ✅ **Custom exception** `InvalidMarksException` thrown when marks ∉ (0–100)
- ✅ **try-catch-finally** blocks
- ✅ **Multi-catch** blocks (`catch (A | B e)`)
- ✅ **`throw`** and **`throws`**
- ✅ **Checked** (`IOException`) and **unchecked** (`ArithmeticException`, `ArrayIndexOutOfBoundsException`) exceptions in separate scenarios

---

## 📁 Files

| File | Purpose |
|------|---------|
| `GradeSystem.java` | Custom exception + 5 handling scenarios |
| `output.txt` | Actual captured program output |
| `README.md` | You are here 👋 |

---

## ⚙️ How to Run

```bash
javac GradeSystem.java
java GradeSystem
```

---

## 🔍 How the Code Works

### The custom exception — `throw` + `throws`

```java
class InvalidMarksException extends Exception { ... }   // extends Exception → CHECKED

static String computeGrade(String student, int marks)
        throws InvalidMarksException {                  // 'throws' → warns the caller
    if (marks < 0 || marks > 100)
        throw new InvalidMarksException(...);           // 'throw'  → raises it
    ...
}
```

> 💡 **`throw`** fires one exception object; **`throws`** is a method-signature *declaration* that forces callers to handle it. Extending `Exception` (not `RuntimeException`) makes it **checked** — the compiler refuses to let you ignore it.

### The five scenarios

| # | Scenario | Exception | Type |
|---|----------|-----------|------|
| 1 | Marks −5 and 104 | `InvalidMarksException` + `finally` | custom, checked |
| 2 | Average with 0 subjects | `ArithmeticException` (`/ by zero`) | unchecked |
| 3 | `scores[5]` on a length-3 array | `ArrayIndexOutOfBoundsException` | unchecked |
| 4 | Opening a missing file | `IOException` | **checked** |
| 5 | One `try`, three failure modes | multi-catch `A \| B` + separate `NullPointerException` catch | mixed |

### `finally` always runs

```java
} finally {
    System.out.println("(finally) record for " + students[i] + " processed.");
}
```
The output shows this line for Aarav (success) **and** for Diya/Kabir (failures) — `finally` is the cleanup slot that runs no matter what.

---

## 📸 Live Terminal Screenshot

> Genuine screenshot of `GradeSystem` compiled with `javac` and run in Windows Terminal on Windows 11 (JDK 17) — not a mock-up.

![Lab 05 terminal output](screenshots/output-1.png)

---

## 📟 Output (actual run)

```text
  Aarav scored 92 -> Grade A+
    (finally) record for Aarav processed.

  [CAUGHT InvalidMarksException] Marks -5 for Diya are out of range (0-100)
    (finally) record for Diya processed.

  [CAUGHT ArithmeticException] / by zero -> subject count cannot be zero
  [CAUGHT ArrayIndexOutOfBoundsException] Index 5 out of bounds for length 3
  [CAUGHT IOException] marks_database_missing.txt (The system cannot find the file specified)

  [MULTI-CATCH] handled ArithmeticException : / by zero
  [MULTI-CATCH] handled ArrayIndexOutOfBoundsException : Index 9 out of bounds for length 2
  [SEPARATE CATCH] handled NullPointerException

  All scenarios handled — program terminated NORMALLY (exit code 0).
```

<details>
<summary>📜 <b>Click for the FULL captured output</b> (also in <code>output.txt</code>)</summary>

See [`output.txt`](./output.txt) — all five scenarios with section banners and every `finally` line.

</details>

### 🧾 What the output proves

| Observation | Concept verified |
|-------------|------------------|
| −5 and 104 both rejected with our own message | Custom exception + `throw` + `throws` work end-to-end |
| `(finally)` prints after success **and** failure | `finally` is unconditional |
| One handler caught two different exception types | Multi-catch collapses duplicate handlers |
| The compiler *forced* the `IOException` catch | That's exactly what "checked" means |
| Final line + exit code 0 | Handled exceptions don't crash the program |

---

## 🧭 Checked vs Unchecked — one-glance table

| | Checked | Unchecked |
|---|---|---|
| Parent | `Exception` (minus `RuntimeException`) | `RuntimeException` |
| Compiler forces handling? | ✅ yes | ❌ no |
| Represents | recoverable external failures (files, network) | programming bugs (÷0, bad index, null) |
| This lab | `InvalidMarksException`, `IOException` | `ArithmeticException`, `ArrayIndexOutOfBoundsException`, `NullPointerException` |

---

## 🎤 Viva-Voce Quick Prep

| Question | Answer |
|----------|--------|
| `throw` vs `throws`? | `throw` raises an exception object; `throws` declares what a method may propagate. |
| When does `finally` NOT run? | Practically only on `System.exit()` or JVM death. |
| Can one `try` have multiple `catch` blocks? | Yes — ordered subclass → superclass; plus multi-catch with `\|`. |
| Why make `InvalidMarksException` checked? | Invalid marks are a *foreseeable* data problem the caller should be forced to handle. |
