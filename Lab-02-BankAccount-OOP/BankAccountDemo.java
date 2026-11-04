/**
 * Lab 02 - OOP Fundamentals : BankAccount
 * Demonstrates: default, parameterized and copy constructors,
 *               deposit / withdraw (with balance check) / display,
 *               multiple objects, and constructor overloading.
 *
 * CO1 : Implement OOP fundamentals using classes, objects, and constructors
 * Bloom Level: K3 - Apply
 */

class BankAccount {
    // ---------- Attributes (encapsulated) ----------
    private String accountNumber;
    private String holderName;
    private double balance;

    // ---------- (a) Default constructor ----------
    BankAccount() {
        this.accountNumber = "NOT-ASSIGNED";
        this.holderName    = "Unknown";
        this.balance       = 0.0;
        System.out.println("[Default constructor] called -> empty account created");
    }

    // ---------- (b) Parameterized constructor (overloading #1) ----------
    BankAccount(String accountNumber, String holderName, double openingBalance) {
        this.accountNumber = accountNumber;
        this.holderName    = holderName;
        this.balance       = openingBalance;
        System.out.println("[Parameterized constructor] called -> account "
                + accountNumber + " created for " + holderName);
    }

    // ---------- Overloaded constructor without opening balance (overloading #2) ----------
    BankAccount(String accountNumber, String holderName) {
        this(accountNumber, holderName, 0.0);   // constructor chaining with this()
        System.out.println("   (chained: opening balance defaulted to 0.0)");
    }

    // ---------- (c) Copy constructor ----------
    BankAccount(BankAccount source) {
        this.accountNumber = source.accountNumber + "-COPY";
        this.holderName    = source.holderName;
        this.balance       = source.balance;
        System.out.println("[Copy constructor] called -> cloned account of " + source.holderName);
    }

    // ---------- Behaviour ----------
    void deposit(double amount) {
        if (amount <= 0) {
            System.out.println("  [X] Deposit failed: amount must be positive");
            return;
        }
        balance += amount;
        System.out.printf("  [OK] Deposited  Rs.%,.2f  | New balance: Rs.%,.2f%n", amount, balance);
    }

    void withdraw(double amount) {
        if (amount <= 0) {
            System.out.println("  [X] Withdrawal failed: amount must be positive");
            return;
        }
        if (amount > balance) {   // balance check
            System.out.printf("  [X] Withdrawal of Rs.%,.2f DENIED - insufficient balance (Rs.%,.2f available)%n",
                    amount, balance);
            return;
        }
        balance -= amount;
        System.out.printf("  [OK] Withdrew   Rs.%,.2f  | New balance: Rs.%,.2f%n", amount, balance);
    }

    void display() {
        System.out.println("  +--------------------------------------+");
        System.out.printf ("  | A/C No : %-27s |%n", accountNumber);
        System.out.printf ("  | Holder : %-27s |%n", holderName);
        System.out.printf ("  | Balance: Rs.%,-24.2f |%n", balance);
        System.out.println("  +--------------------------------------+");
    }
}

public class BankAccountDemo {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" 1. CREATING OBJECTS WITH DIFFERENT CONSTRUCTORS");
        System.out.println("=================================================");
        BankAccount a1 = new BankAccount();                                  // default
        BankAccount a2 = new BankAccount("SBI-1001", "Aarav Sharma", 5000);  // parameterized
        BankAccount a3 = new BankAccount("SBI-1002", "Diya Patel");          // overloaded
        BankAccount a4 = new BankAccount(a2);                                // copy

        System.out.println("\n=================================================");
        System.out.println(" 2. INITIAL STATE OF ALL FOUR OBJECTS");
        System.out.println("=================================================");
        a1.display();
        a2.display();
        a3.display();
        a4.display();

        System.out.println("\n=================================================");
        System.out.println(" 3. DEPOSITS AND WITHDRAWALS (WITH BALANCE CHECK)");
        System.out.println("=================================================");
        System.out.println("Operating on Aarav's account (SBI-1001):");
        a2.deposit(2500);
        a2.withdraw(1000);
        a2.withdraw(999999);      // should be denied
        a2.deposit(-50);          // invalid amount

        System.out.println("\nOperating on Diya's account (SBI-1002):");
        a3.deposit(750);
        a3.withdraw(800);         // should be denied (only 750 available)
        a3.withdraw(300);

        System.out.println("\n=================================================");
        System.out.println(" 4. FINAL STATE - COPY IS INDEPENDENT OF ORIGINAL");
        System.out.println("=================================================");
        System.out.println("Original (modified after copying):");
        a2.display();
        System.out.println("Copy (still holds the balance at copy time):");
        a4.display();
    }
}
