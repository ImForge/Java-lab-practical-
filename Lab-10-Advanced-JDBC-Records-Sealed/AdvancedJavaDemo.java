/**
 * Lab 10 - Advanced JDBC, Advanced Collectors, Records & Sealed Classes
 * Part A : JDBC - 4 driver types (conceptual comparison), CRUD with Statement /
 *          PreparedStatement / CallableStatement, batch insert of 10 records,
 *          transactions with commit, rollback AND savepoint, HikariCP pooling.
 * Part B : Collectors.groupingBy, partitioningBy, joining, flatMap, reduce,
 *          and chained Optional methods.
 * Part C : Record class Student (immutability) and a sealed Shape hierarchy
 *          with permitted subclasses Circle, Rectangle, Triangle.
 *
 * CO5 : Develop database-connected Java applications using JDBC and apply
 *       advanced Java features including Records and Sealed classes
 * Bloom Level: K4 - Analyze
 *
 * NOTE: Parts B and C run on plain Java 17. Part A needs MySQL + Connector/J
 *       (and HikariCP's jar for the pooling demo) on the classpath - if the
 *       database is unreachable the program says so and continues.
 */

import java.sql.*;
import java.util.*;
import java.util.stream.*;

// ======================= PART C : RECORD =======================
// One line gives us: private final fields, constructor, accessors,
// equals(), hashCode() and toString(). Records are IMMUTABLE.
record StudentR(int id, String name, double marks) {
    // compact constructor -> validation runs before fields are assigned
    StudentR {
        if (marks < 0 || marks > 100)
            throw new IllegalArgumentException("marks must be 0-100");
    }
}

// ======================= PART C : SEALED HIERARCHY =======================
// 'sealed' = ONLY the permitted classes may extend/implement it.
sealed interface ShapeS permits CircleS, RectangleS, TriangleS {
    double area();
}
record CircleS(double r)              implements ShapeS { public double area() { return Math.PI * r * r; } }
record RectangleS(double l, double w) implements ShapeS { public double area() { return l * w; } }
record TriangleS(double b, double h)  implements ShapeS { public double area() { return 0.5 * b * h; } }
// class Pentagon implements ShapeS {}  // <-- COMPILE ERROR: not in the permits list

public class AdvancedJavaDemo {

    // ======================= PART A : JDBC =======================
    static void jdbcDriverTypesComparison() {
        System.out.println("  Conceptual comparison of the 4 JDBC driver types:");
        System.out.println("  +------+---------------------------+----------------------------------------+");
        System.out.println("  | Type | Name                      | How it works / verdict                 |");
        System.out.println("  +------+---------------------------+----------------------------------------+");
        System.out.println("  |  1   | JDBC-ODBC bridge          | Java->ODBC->DB. Removed in Java 8. Dead|");
        System.out.println("  |  2   | Native-API driver         | Java->C client lib->DB. Platform-bound |");
        System.out.println("  |  3   | Network-protocol driver   | Java->middleware server->DB. Flexible  |");
        System.out.println("  |  4   | Thin (pure Java) driver   | Java->DB wire protocol directly.       |");
        System.out.println("  |      |                           | Portable & fastest -> USED TODAY       |");
        System.out.println("  +------+---------------------------+----------------------------------------+");
        System.out.println("  MySQL Connector/J is a Type-4 thin driver.");
    }

    static void jdbcLiveDemo() {
        final String URL = "jdbc:mysql://localhost:3306/college?createDatabaseIfNotExist=true";
        try (Connection con = DriverManager.getConnection(URL, "labuser", "lab123")) {
            System.out.println("  Connected: " + con.getMetaData().getDatabaseProductVersion());

            // ---- CRUD with Statement + PreparedStatement ----
            try (Statement st = con.createStatement()) {
                st.executeUpdate("DROP TABLE IF EXISTS student10");
                st.executeUpdate("CREATE TABLE student10 (id INT PRIMARY KEY, "
                        + "name VARCHAR(50), marks DOUBLE)");
                st.executeUpdate("INSERT INTO student10 VALUES (1,'Aarav',88.5)"); // CREATE
            }
            try (PreparedStatement ps = con.prepareStatement(
                    "UPDATE student10 SET marks=? WHERE id=?")) {                  // UPDATE
                ps.setDouble(1, 91.0); ps.setInt(2, 1); ps.executeUpdate();
            }

            // ---- CallableStatement (stored procedure) ----
            try (Statement st = con.createStatement()) {
                st.executeUpdate("DROP PROCEDURE IF EXISTS topper_count");
                st.executeUpdate("CREATE PROCEDURE topper_count(IN cutoff DOUBLE, OUT cnt INT) "
                        + "BEGIN SELECT COUNT(*) INTO cnt FROM student10 WHERE marks >= cutoff; END");
            }
            try (CallableStatement cs = con.prepareCall("{call topper_count(?, ?)}")) {
                cs.setDouble(1, 80.0);
                cs.registerOutParameter(2, Types.INTEGER);
                cs.execute();
                System.out.println("  CallableStatement topper_count(80) -> " + cs.getInt(2));
            }

            // ---- batch insert of 10 records ----
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO student10 VALUES (?,?,?)")) {
                for (int i = 2; i <= 11; i++) {
                    ps.setInt(1, i); ps.setString(2, "Student" + i); ps.setDouble(3, 50 + i);
                    ps.addBatch();
                }
                System.out.println("  Batch insert -> " + ps.executeBatch().length + " rows in one shot.");
            }

            // ---- transaction with SAVEPOINT ----
            con.setAutoCommit(false);
            try (Statement st = con.createStatement()) {
                st.executeUpdate("UPDATE student10 SET marks = marks + 5 WHERE id = 1");
                Savepoint sp = con.setSavepoint("afterBonus");
                st.executeUpdate("UPDATE student10 SET marks = 0 WHERE id = 2"); // oops
                con.rollback(sp);        // undo only the second update
                con.commit();            // keep the +5 bonus
                System.out.println("  Savepoint demo: bonus kept, bad update rolled back.");
            } finally {
                con.setAutoCommit(true);
            }

            // ---- display with ResultSet ----
            try (Statement st = con.createStatement();
                 ResultSet rs = st.executeQuery(
                         "SELECT * FROM student10 ORDER BY id LIMIT 5")) {
                System.out.println("  First 5 rows:");
                while (rs.next())
                    System.out.printf("    %d | %s | %.1f%n",
                            rs.getInt(1), rs.getString(2), rs.getDouble(3));
            }

            /* ---- HikariCP connection pool (needs HikariCP + SLF4J jars) ----
             * HikariConfig cfg = new HikariConfig();
             * cfg.setJdbcUrl("jdbc:mysql://localhost:3306/college");
             * cfg.setUsername("labuser");  cfg.setPassword("lab123");
             * cfg.setMaximumPoolSize(10);           // max simultaneous connections
             * cfg.setMinimumIdle(2);                // connections kept warm
             * cfg.setConnectionTimeout(30_000);     // ms to wait for a free one
             * try (HikariDataSource ds = new HikariDataSource(cfg);
             *      Connection pooled = ds.getConnection()) {
             *     // borrowing from the pool is ~100x faster than a new connection
             * }
             */
            System.out.println("  HikariCP pool configuration shown in source (external jar needed).");

        } catch (SQLException e) {
            System.out.println("  MySQL not reachable here (" + e.getMessage().split("\n")[0] + ")");
            System.out.println("  -> Code above is complete; see README for setup + expected output.");
        }
    }

    public static void main(String[] args) {

        System.out.println("=================================================================");
        System.out.println(" PART A - JDBC: driver types, CRUD, Callable, batch, savepoint");
        System.out.println("=================================================================");
        jdbcDriverTypesComparison();
        System.out.println();
        jdbcLiveDemo();

        // ======================= PART B : advanced Collectors =======================
        System.out.println("\n=================================================================");
        System.out.println(" PART B - Collectors, flatMap, reduce, Optional chaining");
        System.out.println("=================================================================");
        record Pupil(String name, int marks, String grade, String dept, List<String> courses) {}
        List<Pupil> pupils = List.of(
                new Pupil("Aarav", 92, "A", "CSE", List.of("DSA", "DBMS")),
                new Pupil("Diya",  81, "A", "ECE", List.of("VLSI", "DSP")),
                new Pupil("Kabir", 35, "F", "CSE", List.of("DSA")),
                new Pupil("Meera", 74, "B", "ME",  List.of("Thermo", "CAD")),
                new Pupil("Ishan", 58, "C", "CSE", List.of("DBMS", "OS")));

        Map<String, List<String>> byGrade = pupils.stream()
                .collect(Collectors.groupingBy(Pupil::grade, TreeMap::new,
                         Collectors.mapping(Pupil::name, Collectors.toList())));
        System.out.println("  groupingBy(grade)     : " + byGrade);

        Map<Boolean, Long> passFailCount = pupils.stream()
                .collect(Collectors.partitioningBy(p -> p.marks() >= 40, Collectors.counting()));
        System.out.println("  partitioningBy(pass)  : pass=" + passFailCount.get(true)
                + ", fail=" + passFailCount.get(false));

        String roster = pupils.stream().map(Pupil::name)
                .collect(Collectors.joining(", ", "[", "]"));
        System.out.println("  joining(names)        : " + roster);

        List<String> allCourses = pupils.stream()
                .flatMap(p -> p.courses().stream())      // List<List<String>> -> flat stream
                .distinct().sorted().toList();
        System.out.println("  flatMap(courses)      : " + allCourses);

        int totalMarks = pupils.stream().map(Pupil::marks).reduce(0, Integer::sum);
        System.out.println("  reduce(sum of marks)  : " + totalMarks);

        // chained Optional - safely retrieve a student's department
        Optional<String> dept = pupils.stream()
                .filter(p -> p.name().equals("Meera"))
                .findFirst()                              // Optional<Pupil>
                .map(Pupil::dept)                         // Optional<String>
                .filter(d -> !d.isBlank());
        System.out.println("  Optional chain (Meera): " + dept.orElse("<unknown>"));
        System.out.println("  Optional chain (Nil)  : " + pupils.stream()
                .filter(p -> p.name().equals("Nil")).findFirst()
                .map(Pupil::dept).orElse("<unknown>"));

        // ======================= PART C : Records & Sealed =======================
        System.out.println("\n=================================================================");
        System.out.println(" PART C - Record immutability + sealed Shape hierarchy");
        System.out.println("=================================================================");
        StudentR s1 = new StudentR(1, "Aarav", 92);
        StudentR s2 = new StudentR(1, "Aarav", 92);
        System.out.println("  record toString()     : " + s1);
        System.out.println("  auto equals()         : s1.equals(s2) = " + s1.equals(s2));
        System.out.println("  accessor              : s1.marks() = " + s1.marks());
        System.out.println("  IMMUTABLE             : no setters exist; s1.marks = 50 won't compile.");
        System.out.println("  'withMarks' pattern   : " + new StudentR(s1.id(), s1.name(), 99.0)
                + "  (a NEW object - original untouched)");
        try {
            new StudentR(2, "Broken", 150);
        } catch (IllegalArgumentException e) {
            System.out.println("  compact-ctor guard    : rejected marks=150 -> " + e.getMessage());
        }

        System.out.println();
        List<ShapeS> shapes = List.of(new CircleS(5), new RectangleS(8, 3), new TriangleS(6, 4));
        for (ShapeS sh : shapes) {
            // exhaustive handling is possible BECAUSE the hierarchy is sealed
            String kind = (sh instanceof CircleS c)    ? "Circle r=" + c.r()
                        : (sh instanceof RectangleS r) ? "Rectangle " + r.l() + "x" + r.w()
                        :                                "Triangle b=" + ((TriangleS) sh).b();
            System.out.printf("  sealed ShapeS -> %-18s area = %8.2f%n", kind, sh.area());
        }
        System.out.println("  Only Circle/Rectangle/Triangle may implement ShapeS - the compiler");
        System.out.println("  rejects any other subclass (see commented Pentagon in source).");
    }
}
