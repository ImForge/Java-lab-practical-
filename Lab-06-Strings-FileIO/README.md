<div align="center">

# 🧵 Lab 06 — Strings & File I/O

### *Palindrome / word count / char frequency · String vs StringBuffer vs StringBuilder · BufferedWriter/Reader · Serialization*

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![CO3](https://img.shields.io/badge/Course_Outcome-CO3-blue)
![Bloom](https://img.shields.io/badge/Bloom-K3_Apply-green)
![Status](https://img.shields.io/badge/Output-Verified_✓-success)

</div>

---

## 🎯 Aim

Write programs to:

| # | Task |
|---|------|
| 1 | String operations — **palindrome check**, **word count**, **character frequency** |
| 2 | Compare **String vs StringBuffer vs StringBuilder** performance |
| 3 | Write student records to a text file with **BufferedWriter**, read back with **BufferedReader** |
| 4 | **Serialize** a `Student` object to a file and **deserialize** it back |

---

## 📁 Files

| File | Purpose |
|------|---------|
| `StringFileOps.java` | All four parts + a `Serializable` `Student` class |
| `output.txt` | Actual captured program output |
| `README.md` | You are here 👋 |

> ℹ️ The program creates `students.txt` and `student.ser` while running and **deletes them at the end**, so the folder stays clean.

---

## ⚙️ How to Run

```bash
javac StringFileOps.java
java StringFileOps
```

---

## 🔍 How the Code Works

### 1️⃣ String operations
```java
// palindrome: strip non-alphanumerics, lowercase, two-pointer scan
String clean = s.replaceAll("[^A-Za-z0-9]", "").toLowerCase();

// word count: split on runs of whitespace
trimmed.split("\\s+").length;

// char frequency: TreeMap + merge() → sorted counts in 1 line per char
freq.merge(c, 1, Integer::sum);
```
The two-pointer palindrome handles `"A man, a plan, a canal: Panama"` correctly because punctuation is stripped first.

### 2️⃣ The performance shoot-out (20,000 appends)

| Class | Mutability | Thread-safe | Measured time |
|-------|-----------|-------------|---------------|
| `String` | ❌ immutable | — | **60 ms** 🐌 |
| `StringBuffer` | ✅ mutable | ✅ synchronized | **1 ms** |
| `StringBuilder` | ✅ mutable | ❌ (faster) | **1 ms** ⚡ |

> 💡 `s += "x"` copies the **entire** string every iteration → O(n²) total work. The builders append into an internal array → O(n). That's a ~60× gap at just 20k appends, and it grows quadratically. *(Timings vary slightly per run/machine.)*

### 3️⃣ Buffered file I/O — try-with-resources
```java
try (BufferedWriter bw = new BufferedWriter(new FileWriter(recordsFile))) {
    bw.write("101,Aarav Sharma,88.5");
    bw.newLine();
}   // auto-closed here, even if an exception occurs
```
Buffering batches many small writes into few disk operations; `try-with-resources` guarantees the stream closes.

### 4️⃣ Serialization — objects to bytes and back
```java
class Student implements Serializable {
    transient String sessionToken;   // ← excluded from the byte stream
}
oos.writeObject(original);           // object → 94 bytes on disk
Student restored = (Student) ois.readObject();   // bytes → object
```

---

## 📸 Live Terminal Screenshot

> Genuine screenshot of `StringFileOps` compiled with `javac` and run in Windows Terminal on Windows 11 (JDK 17) — not a mock-up. (The String-vs-Builder timings vary slightly per run/machine.)

![Lab 06 terminal output](screenshots/output-1.png)

---

## 📟 Output (actual run)

```text
  "madam" is palindrome? true
  "Java" is palindrome? false
  "A man, a plan, a canal: Panama" is palindrome? true

  Word count : 9
  Char frequency (letters only): {a=1, g=2, i=1, m=2, n=1, o=1, p=1, r=2}

  String        (immutable)          :     60 ms
  StringBuffer  (mutable, sync)      :      1 ms
  StringBuilder (mutable, non-sync)  :      1 ms

  Reading them back line by line:
    ID 101  | Aarav Sharma    | marks 88.5
    ID 102  | Diya Patel      | marks 92.0
    ID 103  | Kabir Singh     | marks 76.25

  Before serialization : Student{id=104, name='Meera Iyer', marks=95.5, sessionToken=TOKEN-XYZ-123}
  Object written to student.ser (94 bytes)
  After deserialization: Student{id=104, name='Meera Iyer', marks=95.5, sessionToken=null}
```

<details>
<summary>📜 <b>Click for the FULL captured output</b> (also in <code>output.txt</code>)</summary>

See [`output.txt`](./output.txt) — all four sections including the cleanup confirmation line.

</details>

### 🧾 What the output proves

| Observation | Concept verified |
|-------------|------------------|
| Panama sentence → `true` | Normalization before the two-pointer check works |
| 60 ms vs 1 ms | Immutability has a real, measurable cost in loops |
| 3 records written then read back identical | Buffered writer/reader round-trip is lossless |
| `sessionToken=null` after deserialization | `transient` fields are **skipped** by serialization — exactly as designed |

---

## 🎤 Viva-Voce Quick Prep

| Question | Answer |
|----------|--------|
| Why is `String` immutable in Java? | Security, string-pool sharing, safe hashing/threading. |
| StringBuffer vs StringBuilder? | Same API; Buffer is synchronized (thread-safe, slower), Builder is not (faster). |
| What is `serialVersionUID`? | A version stamp checked at deserialization to detect incompatible class changes. |
| What does `transient` do? | Excludes a field from serialization; it comes back as its default value (`null`/0). |
