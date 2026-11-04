<div align="center">

# 🧱 Lab 01 — Java Basics

### *Nested-loop patterns · Recursive factorial · Prime hunting · Manual string reversal*

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![CO1](https://img.shields.io/badge/Course_Outcome-CO1-blue)
![Bloom](https://img.shields.io/badge/Bloom-K2_Understand-green)
![Status](https://img.shields.io/badge/Output-Verified_✓-success)

</div>

---

## 🎯 Aim

Write Java programs to demonstrate:

| # | Task | Core idea |
|---|------|-----------|
| 1 | Printing patterns | **Nested loops** — outer loop = rows, inner loops = spaces/symbols |
| 2 | Factorial | **Recursion** — `n! = n × (n−1)!` with a base case |
| 3 | Prime numbers in a range | **Trial division** up to `√n` |
| 4 | Reverse a string | **No built-in methods** — manual `char[]` copy |

---

## 📁 Files

| File | Purpose |
|------|---------|
| `BasicPrograms.java` | All four programs in one class with a sectioned `main()` |
| `output.txt` | Actual captured program output |
| `README.md` | You are here 👋 |

---

## ⚙️ How to Run

```bash
javac BasicPrograms.java
java BasicPrograms
```

---

## 🔍 How the Code Works

### 1️⃣ Patterns — nested loops
Three patterns are drawn (star pyramid, number triangle, inverted triangle). The trick in every pattern is the same:

```java
for (int i = 1; i <= rows; i++) {        // OUTER loop → one pass per row
    for (int s = rows - i; s > 0; s--)   // INNER loop #1 → leading spaces
        System.out.print(" ");
    for (int j = 1; j <= 2 * i - 1; j++) // INNER loop #2 → 1, 3, 5, 7… stars
        System.out.print("*");
    System.out.println();                // end the row
}
```

> 💡 Row `i` of a centered pyramid has `2i−1` stars and `rows−i` leading spaces — that formula is the entire program.

### 2️⃣ Factorial — recursion
```java
static long factorial(int n) {
    if (n <= 1) return 1;            // BASE case — stops the recursion
    return n * factorial(n - 1);     // RECURSIVE case — smaller subproblem
}
```
The call `factorial(5)` unwinds as `5 × 4 × 3 × 2 × 1 = 120`. `long` is used because `15!` (1,307,674,368,000) overflows `int`.

### 3️⃣ Primes in a range — trial division
```java
for (int i = 2; i * i <= n; i++)     // only test divisors up to √n
    if (n % i == 0) return false;
```
> 💡 If `n` had a factor bigger than `√n`, its partner factor would be smaller than `√n` — so checking up to `√n` is enough. That turns an O(n) check into O(√n).

### 4️⃣ String reversal — zero built-ins
No `StringBuilder.reverse()`, no `charAt` tricks hidden in library calls — just a backwards copy into a fresh `char[]`:

```java
for (int i = input.length() - 1, j = 0; i >= 0; i--, j++)
    chars[j] = input.charAt(i);      // last char → first slot, and so on
```

---

## 📸 Live Terminal Screenshot

> Genuine screenshot of `BasicPrograms` compiled with `javac` and run in Windows Terminal on Windows 11 (JDK 17) — not a mock-up.

![Lab 01 terminal output](screenshots/output-1.png)

---

## 📟 Output (actual run)

```text
--- Star Pyramid (5 rows) ---
    *
   ***
  *****
 *******
*********

0! = 1
5! = 120
15! = 1307674368000

Primes between 1 and 50 : 2 3 5 7 11 13 17 19 23 29 31 37 41 43 47
Total primes found : 15

Original : "Hello World"  ->  Reversed : "dlroW olleH"
Original : "racecar"  ->  Reversed : "racecar"
```

<details>
<summary>📜 <b>Click for the FULL captured output</b> (also in <code>output.txt</code>)</summary>

See [`output.txt`](./output.txt) — every section banner, all three patterns, five factorials, two prime ranges and four reversal test cases.

</details>

### 🧾 What the output proves
- **Patterns** — spacing math lines the pyramid up perfectly; each pattern uses a different nested-loop shape.
- **Factorial** — edge cases `0!` and `1!` both return `1` (the base case), and `15!` shows why `long` was needed.
- **Primes** — 15 primes below 50 is the classic known answer ✓; the 90–120 range shows it works for arbitrary windows.
- **Reversal** — `"racecar"` reversing to itself is a free palindrome sanity-check.

---

## 🎤 Viva-Voce Quick Prep

| Question | Answer |
|----------|--------|
| Why `long` for factorial? | `13!` already exceeds `int`'s max (≈2.1 billion); `long` holds up to `20!`. |
| What happens without a base case? | Infinite recursion → `StackOverflowError`. |
| Why loop only to `√n` for primes? | Factors come in pairs; one of each pair is always ≤ `√n`. |
| Is `charAt()` a "built-in reversal method"? | No — it reads one character. The *reversal logic* is entirely manual. |
