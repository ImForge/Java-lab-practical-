/**
 * Lab 01 - Java Basics
 * Demonstrates: (1) Pattern printing using nested loops
 *               (2) Factorial using recursion
 *               (3) Prime numbers in a range
 *               (4) Reversing a string WITHOUT built-in methods
 *
 * CO1 : Understand Java environment and basic programming constructs
 * Bloom Level: K2 - Understand
 */
public class BasicPrograms {

    // ---------- (1) Pattern printing using nested loops ----------
    static void printStarPyramid(int rows) {
        for (int i = 1; i <= rows; i++) {           // outer loop -> rows
            for (int s = rows - i; s > 0; s--)      // inner loop -> leading spaces
                System.out.print(" ");
            for (int j = 1; j <= 2 * i - 1; j++)    // inner loop -> stars
                System.out.print("*");
            System.out.println();
        }
    }

    static void printNumberTriangle(int rows) {
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= i; j++)
                System.out.print(j + " ");
            System.out.println();
        }
    }

    static void printInvertedTriangle(int rows) {
        for (int i = rows; i >= 1; i--) {
            for (int j = 1; j <= i; j++)
                System.out.print("# ");
            System.out.println();
        }
    }

    // ---------- (2) Factorial using recursion ----------
    static long factorial(int n) {
        if (n < 0)
            throw new IllegalArgumentException("Factorial is not defined for negative numbers");
        if (n <= 1)                 // base case
            return 1;
        return n * factorial(n - 1); // recursive case: n! = n * (n-1)!
    }

    // ---------- (3) Prime numbers in a range ----------
    static boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++)   // trial division up to sqrt(n)
            if (n % i == 0) return false;
        return true;
    }

    static void printPrimesInRange(int low, int high) {
        System.out.print("Primes between " + low + " and " + high + " : ");
        int count = 0;
        for (int n = low; n <= high; n++) {
            if (isPrime(n)) {
                System.out.print(n + " ");
                count++;
            }
        }
        System.out.println("\nTotal primes found : " + count);
    }

    // ---------- (4) Reverse a string WITHOUT built-in methods ----------
    static String reverse(String input) {
        char[] chars = new char[input.length()];
        // manually copy characters from the end to the beginning
        for (int i = input.length() - 1, j = 0; i >= 0; i--, j++) {
            chars[j] = input.charAt(i);
        }
        return new String(chars);
    }

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println(" (1) PATTERN PRINTING USING NESTED LOOPS");
        System.out.println("==============================================");
        System.out.println("\n--- Star Pyramid (5 rows) ---");
        printStarPyramid(5);
        System.out.println("\n--- Number Triangle (5 rows) ---");
        printNumberTriangle(5);
        System.out.println("\n--- Inverted Triangle (5 rows) ---");
        printInvertedTriangle(5);

        System.out.println("\n==============================================");
        System.out.println(" (2) FACTORIAL USING RECURSION");
        System.out.println("==============================================");
        for (int n : new int[]{0, 1, 5, 10, 15}) {
            System.out.println(n + "! = " + factorial(n));
        }

        System.out.println("\n==============================================");
        System.out.println(" (3) PRIME NUMBERS IN A RANGE");
        System.out.println("==============================================");
        printPrimesInRange(1, 50);
        printPrimesInRange(90, 120);

        System.out.println("\n==============================================");
        System.out.println(" (4) STRING REVERSAL (NO BUILT-IN METHODS)");
        System.out.println("==============================================");
        String[] tests = {"Java", "Hello World", "racecar", "A"};
        for (String s : tests) {
            System.out.println("Original : \"" + s + "\"  ->  Reversed : \"" + reverse(s) + "\"");
        }
    }
}
