/**
 * Lab 09 - JDBC : MySQL CRUD on a student table
 * Demonstrates: (1) Statement for simple queries;
 *               (2) PreparedStatement for parameterized INSERT & UPDATE;
 *               (3) Batch processing - insert 10 records at once;
 *               (4) Transaction management - transfer marks between two
 *                   records with commit, and rollback on failure;
 *               ResultSet used throughout to display rows.
 *
 * CO5 : Develop database-connected Java applications using JDBC
 * Bloom Level: K4 - Analyze
 *
 * ------------------------------------------------------------------
 * SETUP (one time):
 *   1. Install MySQL Server and create the database:
 *        CREATE DATABASE college;
 *   2. Download MySQL Connector/J (mysql-connector-j-8.x.jar).
 *   3. Compile and run WITH the driver on the classpath:
 *        javac StudentJDBC.java
 *        java -cp .;mysql-connector-j-8.4.0.jar StudentJDBC      (Windows)
 *        java -cp .:mysql-connector-j-8.4.0.jar StudentJDBC      (Linux/Mac)
 * ------------------------------------------------------------------
 */

import java.sql.*;

public class StudentJDBC {

    static final String URL  = "jdbc:mysql://localhost:3306/college?createDatabaseIfNotExist=true";
    static final String USER = "labuser";
    static final String PASS = "lab123";   // change to your MySQL credentials

    // ---------- helper: print rows of the student table (optionally filtered) ----------
    static void displayAll(Connection con, String title) throws SQLException {
        displayRows(con, title, "");           // "" -> every row
    }

    static void displayRows(Connection con, String title, String whereClause) throws SQLException {
        System.out.println("\n  --- " + title + " ---");
        String sql = "SELECT id, name, marks FROM student " + whereClause + " ORDER BY id";
        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            System.out.printf("  %-5s %-20s %-6s%n", "ID", "NAME", "MARKS");
            while (rs.next()) {                       // ResultSet cursor iteration
                System.out.printf("  %-5d %-20s %-6.1f%n",
                        rs.getInt("id"), rs.getString("name"), rs.getDouble("marks"));
            }
        }
    }

    public static void main(String[] args) {
        try (Connection con = DriverManager.getConnection(URL, USER, PASS)) {
            System.out.println("Connected to MySQL: " + con.getMetaData().getDatabaseProductVersion());

            // ================= (1) Statement - simple queries =================
            System.out.println("\n==============================================================");
            System.out.println(" 1. Statement - CREATE TABLE + simple INSERT/SELECT");
            System.out.println("==============================================================");
            try (Statement st = con.createStatement()) {
                st.executeUpdate("DROP TABLE IF EXISTS student");
                st.executeUpdate("""
                        CREATE TABLE student (
                            id    INT PRIMARY KEY,
                            name  VARCHAR(50) NOT NULL,
                            marks DOUBLE      NOT NULL
                        )""");
                System.out.println("  Table 'student' created.");

                int rows = st.executeUpdate(
                        "INSERT INTO student VALUES (1, 'Aarav Sharma', 88.5)");
                System.out.println("  Statement INSERT -> " + rows + " row affected.");
            }
            displayAll(con, "After Statement insert");

            // ================= (2) PreparedStatement =================
            System.out.println("\n==============================================================");
            System.out.println(" 2. PreparedStatement - parameterized INSERT and UPDATE");
            System.out.println("==============================================================");
            String insertSql = "INSERT INTO student (id, name, marks) VALUES (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                ps.setInt(1, 2);  ps.setString(2, "Diya Patel");  ps.setDouble(3, 92.0);
                ps.executeUpdate();
                ps.setInt(1, 3);  ps.setString(2, "Kabir Singh"); ps.setDouble(3, 45.0);
                ps.executeUpdate();
                System.out.println("  2 rows inserted via placeholders (?) - SQL-injection safe.");
            }

            String updateSql = "UPDATE student SET marks = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(updateSql)) {
                ps.setDouble(1, 50.5);
                ps.setInt(2, 3);
                int rows = ps.executeUpdate();
                System.out.println("  PreparedStatement UPDATE -> " + rows + " row (Kabir 45.0 -> 50.5).");
            }
            displayAll(con, "After PreparedStatement insert/update");

            // ================= (3) Batch processing =================
            System.out.println("\n==============================================================");
            System.out.println(" 3. BATCH - 10 records inserted in ONE round trip");
            System.out.println("==============================================================");
            try (PreparedStatement ps = con.prepareStatement(insertSql)) {
                String[] names = {"Meera", "Ishan", "Zara", "Vivaan", "Anaya",
                                  "Reyansh", "Kiara", "Arjun", "Sara", "Dev"};
                for (int i = 0; i < 10; i++) {
                    ps.setInt(1, 100 + i);
                    ps.setString(2, names[i]);
                    ps.setDouble(3, 60 + i * 3.5);
                    ps.addBatch();                       // queue it - nothing sent yet
                }
                int[] counts = ps.executeBatch();        // all 10 sent together
                System.out.println("  executeBatch() returned " + counts.length
                        + " results -> total rows inserted: " + counts.length);
            }
            displayAll(con, "After batch insert (13 rows total)");

            // ================= (4) Transactions =================
            System.out.println("\n==============================================================");
            System.out.println(" 4. TRANSACTION - transfer 10 marks, commit; then rollback demo");
            System.out.println("==============================================================");
            con.setAutoCommit(false);                    // start manual transaction mode

            // --- successful transfer: id 2 gives 10 marks to id 3 ---
            try (PreparedStatement take = con.prepareStatement(
                         "UPDATE student SET marks = marks - ? WHERE id = ?");
                 PreparedStatement give = con.prepareStatement(
                         "UPDATE student SET marks = marks + ? WHERE id = ?")) {
                take.setDouble(1, 10); take.setInt(2, 2); take.executeUpdate();
                give.setDouble(1, 10); give.setInt(2, 3); give.executeUpdate();
                con.commit();                            // both updates become permanent
                System.out.println("  Transfer of 10 marks (id 2 -> id 3) COMMITTED.");
            }

            // --- failing transfer: second UPDATE hits an invalid id -> rollback ---
            try (PreparedStatement take = con.prepareStatement(
                         "UPDATE student SET marks = marks - ? WHERE id = ?")) {
                take.setDouble(1, 20); take.setInt(2, 1); take.executeUpdate();
                System.out.println("  Deducted 20 from id 1 (not yet committed)...");
                // simulate a failure mid-transaction:
                throw new SQLException("Simulated failure before crediting the receiver!");
            } catch (SQLException e) {
                con.rollback();                          // undo the uncommitted deduction
                System.out.println("  ERROR: " + e.getMessage());
                System.out.println("  ROLLBACK executed - id 1's marks are unchanged.");
            } finally {
                con.setAutoCommit(true);
            }
            displayRows(con, "Final state of ids 1-3 (committed transfer + rollback proof)", "WHERE id <= 3");
            System.out.println("  (the 10 batched rows 100-109 are untouched - see batch section above)");

            System.out.println("\n  All four JDBC techniques demonstrated successfully.");

        } catch (SQLException e) {
            System.out.println("==============================================================");
            System.out.println(" Could not connect to MySQL: " + e.getMessage());
            System.out.println(" -> Start MySQL, create database 'college', and run with");
            System.out.println("    MySQL Connector/J on the classpath (see header comment).");
            System.out.println("==============================================================");
        }
    }
}
