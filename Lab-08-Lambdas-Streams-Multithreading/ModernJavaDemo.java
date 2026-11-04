/**
 * Lab 08 - Java 8 Features & Multithreading
 * Demonstrates: (1) Lambdas + Stream API: filter, sort, groupingBy, partitioningBy;
 *               (2) Optional for null-safety;
 *               (3) Producer-Consumer using Thread class + Runnable interface;
 *               (4) synchronized block preventing a race condition on a
 *                   shared bank-account balance.
 *
 * CO4 : Apply Java 8 features and implement multithreaded programs
 * Bloom Level: K4 - Analyze
 */

import java.util.*;
import java.util.stream.*;

public class ModernJavaDemo {

    record StudentRec(String name, int marks, String dept) {}

    // ======================= Part 3 : Producer-Consumer =======================
    static class SharedBuffer {
        private final Queue<Integer> buffer = new LinkedList<>();
        private final int CAPACITY = 3;

        synchronized void produce(int item) throws InterruptedException {
            while (buffer.size() == CAPACITY) {
                wait();                       // buffer full -> producer waits
            }
            buffer.add(item);
            System.out.println("    [Producer] produced item-" + item
                    + "  (buffer size: " + buffer.size() + ")");
            notifyAll();                      // wake the consumer
        }

        synchronized int consume() throws InterruptedException {
            while (buffer.isEmpty()) {
                wait();                       // buffer empty -> consumer waits
            }
            int item = buffer.remove();
            System.out.println("    [Consumer] consumed item-" + item
                    + " (buffer size: " + buffer.size() + ")");
            notifyAll();                      // wake the producer
            return item;
        }
    }

    // ======================= Part 4 : shared balance =======================
    static class SharedAccount {
        private int balance = 0;
        private final Object lock = new Object();

        // UNSAFE: read-modify-write with no synchronization -> race condition
        void depositUnsafe() { balance = balance + 1; }

        // SAFE: the synchronized block makes the operation atomic
        void depositSafe() {
            synchronized (lock) {
                balance = balance + 1;
            }
        }

        int getBalance() { return balance; }
        void reset()     { balance = 0; }
    }

    public static void main(String[] args) throws Exception {

        // ================= (1) Lambdas + Streams =================
        System.out.println("=================================================================");
        System.out.println(" 1. LAMBDAS + STREAM API (filter / sort / group / partition)");
        System.out.println("=================================================================");
        List<StudentRec> students = List.of(
                new StudentRec("Aarav", 92, "CSE"),
                new StudentRec("Diya",  67, "ECE"),
                new StudentRec("Kabir", 38, "CSE"),
                new StudentRec("Meera", 81, "ME"),
                new StudentRec("Ishan", 55, "ECE"),
                new StudentRec("Zara",  74, "CSE"));

        System.out.println("  filter(marks >= 60) + sorted by marks desc:");
        students.stream()
                .filter(st -> st.marks() >= 60)                         // lambda predicate
                .sorted(Comparator.comparingInt(StudentRec::marks).reversed())
                .forEach(st -> System.out.printf("    %-6s %3d  %s%n",
                        st.name(), st.marks(), st.dept()));

        Map<String, List<String>> byDept = students.stream()
                .collect(Collectors.groupingBy(StudentRec::dept,
                         TreeMap::new,
                         Collectors.mapping(StudentRec::name, Collectors.toList())));
        System.out.println("\n  groupingBy(dept)        : " + byDept);

        Map<Boolean, List<String>> passFail = students.stream()
                .collect(Collectors.partitioningBy(st -> st.marks() >= 40,
                         Collectors.mapping(StudentRec::name, Collectors.toList())));
        System.out.println("  partitioningBy(>= 40)   : PASS=" + passFail.get(true)
                + "  FAIL=" + passFail.get(false));

        double avg = students.stream().mapToInt(StudentRec::marks).average().orElse(0);
        System.out.printf("  average marks (mapToInt): %.2f%n", avg);

        // ================= (2) Optional =================
        System.out.println("\n=================================================================");
        System.out.println(" 2. OPTIONAL - handling null safely");
        System.out.println("=================================================================");
        Optional<StudentRec> topper = students.stream()
                .max(Comparator.comparingInt(StudentRec::marks));
        topper.ifPresent(t -> System.out.println("  Topper (ifPresent)      : "
                + t.name() + " with " + t.marks()));

        Optional<StudentRec> fromCivil = students.stream()
                .filter(st -> st.dept().equals("CIVIL"))
                .findFirst();
        System.out.println("  CIVIL student present?  : " + fromCivil.isPresent());
        String name = fromCivil.map(StudentRec::name).orElse("<no CIVIL student found>");
        System.out.println("  map().orElse() fallback : " + name);
        // Without Optional this would have been a NullPointerException waiting to happen.

        // ================= (3) Producer-Consumer =================
        System.out.println("\n=================================================================");
        System.out.println(" 3. PRODUCER-CONSUMER (Thread class + Runnable, wait/notify)");
        System.out.println("=================================================================");
        SharedBuffer shared = new SharedBuffer();
        final int ITEMS = 6;

        Runnable producerTask = () -> {          // Runnable as a lambda
            try {
                for (int i = 1; i <= ITEMS; i++) {
                    shared.produce(i);
                    Thread.sleep(30);
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };
        Runnable consumerTask = () -> {
            try {
                for (int i = 1; i <= ITEMS; i++) {
                    shared.consume();
                    Thread.sleep(70);            // slower consumer -> buffer fills up
                }
            } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        };

        Thread producer = new Thread(producerTask, "producer-thread");
        Thread consumer = new Thread(consumerTask, "consumer-thread");
        producer.start();
        consumer.start();
        producer.join();                          // main waits for both to finish
        consumer.join();
        System.out.println("  Buffer capacity was 3 - producer blocked whenever it was full.");

        // ================= (4) Race condition vs synchronized =================
        System.out.println("\n=================================================================");
        System.out.println(" 4. RACE CONDITION on shared balance - then fixed with synchronized");
        System.out.println("=================================================================");
        SharedAccount account = new SharedAccount();
        final int DEPOSITS_PER_THREAD = 100_000;

        // --- round 1: UNSAFE ---
        Thread u1 = new Thread(() -> { for (int i = 0; i < DEPOSITS_PER_THREAD; i++) account.depositUnsafe(); });
        Thread u2 = new Thread(() -> { for (int i = 0; i < DEPOSITS_PER_THREAD; i++) account.depositUnsafe(); });
        u1.start(); u2.start(); u1.join(); u2.join();
        System.out.println("  WITHOUT synchronized : expected 200000, got " + account.getBalance()
                + "  <-- updates were LOST");

        // --- round 2: SAFE ---
        account.reset();
        Thread s1 = new Thread(() -> { for (int i = 0; i < DEPOSITS_PER_THREAD; i++) account.depositSafe(); });
        Thread s2 = new Thread(() -> { for (int i = 0; i < DEPOSITS_PER_THREAD; i++) account.depositSafe(); });
        s1.start(); s2.start(); s1.join(); s2.join();
        System.out.println("  WITH synchronized    : expected 200000, got " + account.getBalance()
                + "  <-- every update counted");
    }
}
