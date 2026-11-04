/**
 * Lab 05 - Exception Handling : Student Grade System
 * Demonstrates: custom exception InvalidMarksException, try-catch-finally,
 *               multi-catch, throw & throws, a CHECKED exception (IOException)
 *               and UNCHECKED exceptions (ArithmeticException,
 *               ArrayIndexOutOfBoundsException) in separate scenarios.
 *
 * CO3 : Implement robust exception handling in Java applications
 * Bloom Level: K3 - Apply
 */

import java.io.FileReader;
import java.io.IOException;

// ====================== CUSTOM CHECKED EXCEPTION ======================
class InvalidMarksException extends Exception {
    InvalidMarksException(String message) {
        super(message);
    }
}

public class GradeSystem {

    // 'throws' declares that the caller must handle this checked exception.
    // 'throw' actually raises it when marks are out of the 0-100 range.
    static String computeGrade(String student, int marks) throws InvalidMarksException {
        if (marks < 0 || marks > 100) {
            throw new InvalidMarksException(
                "Marks " + marks + " for " + student + " are out of range (0-100)");
        }
        if (marks >= 90) return "A+";
        if (marks >= 75) return "A";
        if (marks >= 60) return "B";
        if (marks >= 40) return "C";
        return "F";
    }

    public static void main(String[] args) {

        // ---------------- Scenario 1 : custom exception + try-catch-finally ----------------
        System.out.println("==========================================================");
        System.out.println(" 1. CUSTOM InvalidMarksException + try-catch-finally");
        System.out.println("==========================================================");
        String[] students = {"Aarav", "Diya", "Kabir"};
        int[]    marks    = {  92   ,  -5  ,  104  };
        for (int i = 0; i < students.length; i++) {
            try {
                String grade = computeGrade(students[i], marks[i]);
                System.out.println("  " + students[i] + " scored " + marks[i] + " -> Grade " + grade);
            } catch (InvalidMarksException e) {
                System.out.println("  [CAUGHT InvalidMarksException] " + e.getMessage());
            } finally {
                // finally ALWAYS runs - success or failure
                System.out.println("    (finally) record for " + students[i] + " processed.\n");
            }
        }

        // ---------------- Scenario 2 : unchecked ArithmeticException ----------------
        System.out.println("==========================================================");
        System.out.println(" 2. UNCHECKED ArithmeticException (divide by zero)");
        System.out.println("==========================================================");
        int totalMarks = 285, subjects = 0;
        try {
            System.out.println("  Average = " + (totalMarks / subjects));
        } catch (ArithmeticException e) {
            System.out.println("  [CAUGHT ArithmeticException] " + e.getMessage()
                    + " -> subject count cannot be zero");
        }

        // ---------------- Scenario 3 : unchecked ArrayIndexOutOfBoundsException ----------------
        System.out.println("\n==========================================================");
        System.out.println(" 3. UNCHECKED ArrayIndexOutOfBoundsException");
        System.out.println("==========================================================");
        int[] scores = {88, 76, 91};
        try {
            System.out.println("  Accessing scores[5] in an array of length " + scores.length + "...");
            System.out.println(scores[5]);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("  [CAUGHT ArrayIndexOutOfBoundsException] " + e.getMessage());
        }

        // ---------------- Scenario 4 : CHECKED IOException ----------------
        System.out.println("\n==========================================================");
        System.out.println(" 4. CHECKED IOException (file that does not exist)");
        System.out.println("==========================================================");
        try {
            // The compiler FORCES us to handle IOException -> that is what 'checked' means
            FileReader fr = new FileReader("marks_database_missing.txt");
            fr.close();
        } catch (IOException e) {
            System.out.println("  [CAUGHT IOException] " + e.getMessage());
        }

        // ---------------- Scenario 5 : MULTI-CATCH block ----------------
        System.out.println("\n==========================================================");
        System.out.println(" 5. MULTI-CATCH - one handler, several exception types");
        System.out.println("==========================================================");
        for (int scenario = 1; scenario <= 3; scenario++) {
            try {
                switch (scenario) {
                    case 1 -> { int x = 10 / 0; }                       // ArithmeticException
                    case 2 -> { int[] a = new int[2]; int y = a[9]; }   // ArrayIndexOutOfBounds
                    case 3 -> { String s = null; s.length(); }          // NullPointerException
                }
            } catch (ArithmeticException | ArrayIndexOutOfBoundsException e) {
                System.out.println("  [MULTI-CATCH] handled " + e.getClass().getSimpleName()
                        + " : " + e.getMessage());
            } catch (NullPointerException e) {
                System.out.println("  [SEPARATE CATCH] handled NullPointerException");
            }
        }

        System.out.println("\n  All scenarios handled - program terminated NORMALLY (exit code 0).");
    }
}
