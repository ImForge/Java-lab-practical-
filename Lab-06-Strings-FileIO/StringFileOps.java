/**
 * Lab 06 - String Operations & File I/O
 * Demonstrates: (1) palindrome check, word count, character frequency;
 *               (2) String vs StringBuffer vs StringBuilder performance;
 *               (3) writing/reading student records with BufferedWriter/BufferedReader;
 *               (4) serializing and deserializing a Student object.
 *
 * CO3 : Perform string operations and manage file input/output in Java
 * Bloom Level: K3 - Apply
 */

import java.io.*;
import java.util.*;

// A Serializable Student for part (4)
class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    int id;
    String name;
    double marks;
    transient String sessionToken;   // transient -> NOT serialized

    Student(int id, String name, double marks, String sessionToken) {
        this.id = id; this.name = name; this.marks = marks; this.sessionToken = sessionToken;
    }

    @Override public String toString() {
        return "Student{id=" + id + ", name='" + name + "', marks=" + marks
                + ", sessionToken=" + sessionToken + "}";
    }
}

public class StringFileOps {

    // ---------- (1a) Palindrome check ----------
    static boolean isPalindrome(String s) {
        String clean = s.replaceAll("[^A-Za-z0-9]", "").toLowerCase();
        for (int i = 0, j = clean.length() - 1; i < j; i++, j--) {
            if (clean.charAt(i) != clean.charAt(j)) return false;
        }
        return true;
    }

    // ---------- (1b) Word count ----------
    static int wordCount(String s) {
        String trimmed = s.trim();
        if (trimmed.isEmpty()) return 0;
        return trimmed.split("\\s+").length;
    }

    // ---------- (1c) Character frequency ----------
    static Map<Character, Integer> charFrequency(String s) {
        Map<Character, Integer> freq = new TreeMap<>();   // sorted output
        for (char c : s.toLowerCase().toCharArray()) {
            if (Character.isLetter(c)) {
                freq.merge(c, 1, Integer::sum);
            }
        }
        return freq;
    }

    public static void main(String[] args) throws Exception {

        System.out.println("=============================================================");
        System.out.println(" 1. STRING OPERATIONS");
        System.out.println("=============================================================");
        String[] phrases = {"madam", "Java", "A man, a plan, a canal: Panama"};
        for (String p : phrases) {
            System.out.println("  \"" + p + "\" is palindrome? " + isPalindrome(p));
        }

        String sentence = "Java makes string handling simple and Java is everywhere";
        System.out.println("\n  Sentence   : \"" + sentence + "\"");
        System.out.println("  Word count : " + wordCount(sentence));
        System.out.println("  Char frequency (letters only): " + charFrequency("Programming"));

        System.out.println("\n=============================================================");
        System.out.println(" 2. String vs StringBuffer vs StringBuilder (20,000 appends)");
        System.out.println("=============================================================");
        final int N = 20_000;

        long t0 = System.nanoTime();
        String s = "";
        for (int i = 0; i < N; i++) s += "x";          // creates a NEW object every loop
        long tString = (System.nanoTime() - t0) / 1_000_000;

        t0 = System.nanoTime();
        StringBuffer sbuf = new StringBuffer();         // mutable + synchronized (thread-safe)
        for (int i = 0; i < N; i++) sbuf.append("x");
        long tBuffer = (System.nanoTime() - t0) / 1_000_000;

        t0 = System.nanoTime();
        StringBuilder sbld = new StringBuilder();       // mutable, NOT synchronized (fastest)
        for (int i = 0; i < N; i++) sbld.append("x");
        long tBuilder = (System.nanoTime() - t0) / 1_000_000;

        System.out.printf("  String        (immutable)          : %6d ms%n", tString);
        System.out.printf("  StringBuffer  (mutable, sync)      : %6d ms%n", tBuffer);
        System.out.printf("  StringBuilder (mutable, non-sync)  : %6d ms%n", tBuilder);
        System.out.println("  => String is slowest because every += builds a brand-new object.");

        System.out.println("\n=============================================================");
        System.out.println(" 3. BufferedWriter / BufferedReader - student records");
        System.out.println("=============================================================");
        File recordsFile = new File("students.txt");

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(recordsFile))) {
            bw.write("101,Aarav Sharma,88.5");  bw.newLine();
            bw.write("102,Diya Patel,92.0");    bw.newLine();
            bw.write("103,Kabir Singh,76.25");  bw.newLine();
        }
        System.out.println("  Wrote 3 records to " + recordsFile.getName());

        System.out.println("  Reading them back line by line:");
        try (BufferedReader br = new BufferedReader(new FileReader(recordsFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] f = line.split(",");
                System.out.printf("    ID %-4s | %-15s | marks %s%n", f[0], f[1], f[2]);
            }
        }

        System.out.println("\n=============================================================");
        System.out.println(" 4. SERIALIZATION / DESERIALIZATION of a Student object");
        System.out.println("=============================================================");
        File serFile = new File("student.ser");
        Student original = new Student(104, "Meera Iyer", 95.5, "TOKEN-XYZ-123");
        System.out.println("  Before serialization : " + original);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(serFile))) {
            oos.writeObject(original);
        }
        System.out.println("  Object written to " + serFile.getName()
                + " (" + serFile.length() + " bytes)");

        Student restored;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(serFile))) {
            restored = (Student) ois.readObject();
        }
        System.out.println("  After deserialization: " + restored);
        System.out.println("  Note: sessionToken is null - 'transient' fields are skipped.");

        // keep the lab folder clean: remove the generated files after the demo
        recordsFile.delete();
        serFile.delete();
        System.out.println("\n  (Demo files students.txt and student.ser cleaned up.)");
    }
}
