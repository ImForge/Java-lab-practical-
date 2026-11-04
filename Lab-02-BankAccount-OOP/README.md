<div align="center">

# 🏦 Lab 02 — BankAccount (OOP Fundamentals)

### *Default, parameterized & copy constructors · deposit / withdraw / display · constructor overloading*

![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![CO1](https://img.shields.io/badge/Course_Outcome-CO1-blue)
![Bloom](https://img.shields.io/badge/Bloom-K3_Apply-green)
![Status](https://img.shields.io/badge/Output-Verified_✓-success)

</div>

---

## 🎯 Aim

Create a class **`BankAccount`** with attributes *(account number, holder name, balance)* and demonstrate:

- ✅ **Default**, **parameterized** and **copy** constructors
- ✅ `deposit()`, `withdraw()` **with balance check**, `display()`
- ✅ **Multiple objects** living independently
- ✅ **Constructor overloading** (4 constructors, same name, different signatures)

---

## 📁 Files

| File | Purpose |
|------|---------|
| `BankAccountDemo.java` | `BankAccount` class + driver class `BankAccountDemo` |
| `output.txt` | Actual captured program output |
| `README.md` | You are here 👋 |

---

## ⚙️ How to Run

```bash
javac BankAccountDemo.java
java BankAccountDemo
```

---

## 🔍 How the Code Works

### The four constructors (overloading in action)

```java
BankAccount()                                          // 1. default  → safe placeholder values
BankAccount(String acc, String name, double opening)   // 2. parameterized → full control
BankAccount(String acc, String name)                   // 3. overloaded → chains to #2 via this(...)
BankAccount(BankAccount source)                        // 4. copy → clones another object's state
```

> 💡 Constructor #3 shows **constructor chaining**: `this(accountNumber, holderName, 0.0)` reuses #2 instead of duplicating code. The copy constructor appends `-COPY` to the account number so the clone is distinguishable.

### Defensive `withdraw()` — the balance check

```java
if (amount > balance) {      // ← the balance check the syllabus asks for
    System.out.printf("✗ Withdrawal DENIED — insufficient balance%n");
    return;                  // state is never corrupted
}
balance -= amount;
```

Negative deposits and withdrawals are rejected too — the object **protects its own invariants**, which is the whole point of encapsulation (`private` fields + public behaviour).

---

## 📸 Live Terminal Screenshot

> Genuine screenshot of `BankAccountDemo` compiled with `javac` and run in Windows Terminal on Windows 11 (JDK 17) — not a mock-up.

![Lab 02 terminal output](screenshots/output-1.png)

---

## 📟 Output (actual run)

```text
[Default constructor] called -> empty account created
[Parameterized constructor] called -> account SBI-1001 created for Aarav Sharma
[Parameterized constructor] called -> account SBI-1002 created for Diya Patel
   (chained: opening balance defaulted to 0.0)
[Copy constructor] called -> cloned account of Aarav Sharma

Operating on Aarav's account (SBI-1001):
  ✓ Deposited  ₹2,500.00  | New balance: ₹7,500.00
  ✓ Withdrew   ₹1,000.00  | New balance: ₹6,500.00
  ✗ Withdrawal of ₹999,999.00 DENIED — insufficient balance (₹6,500.00 available)
  ✗ Deposit failed: amount must be positive
```

<details>
<summary>📜 <b>Click for the FULL captured output</b> (also in <code>output.txt</code>)</summary>

See [`output.txt`](./output.txt) — all four constructor messages, the boxed `display()` of every object before and after transactions, and the copy-independence proof.

</details>

### 🧾 What the output proves

| Observation | Concept verified |
|-------------|------------------|
| Four different "constructor called" messages | All 4 overloaded constructors resolve correctly by signature |
| `(chained: opening balance defaulted to 0.0)` prints **after** the parameterized message | `this(...)` runs the target constructor **first** |
| ₹999,999 withdrawal **DENIED** | Balance check guards the account |
| Copy still shows ₹5,000 while the original grew to ₹6,500 | The copy got its **own** state — objects are independent |

---

## 🎤 Viva-Voce Quick Prep

| Question | Answer |
|----------|--------|
| Does Java have a built-in copy constructor like C++? | No — you write one yourself taking the same type as a parameter. |
| What makes constructors "overloaded"? | Same name, **different parameter lists**; the compiler picks by signature. |
| Why are the fields `private`? | Encapsulation — balance can only change through `deposit`/`withdraw`, which enforce the rules. |
| What if no constructor is written at all? | The compiler inserts a no-arg default constructor; writing *any* constructor removes that freebie. |
