/**
 * Lab 07 - Java Collections Framework : Inventory Management System
 * Demonstrates: (1) ArrayList + Comparator (sort products by price);
 *               (2) HashMap for product ID-name pairs (lookup/update/delete);
 *               (3) HashSet to remove duplicates;
 *               (4) PriorityQueue to process orders by priority;
 *               Iterator and enhanced for-loop across ALL collections.
 *
 * CO4 : Utilize Java Collections Framework for data management
 * Bloom Level: K3 - Apply
 */

import java.util.*;

class Product {
    int id;
    String name;
    double price;

    Product(int id, String name, double price) {
        this.id = id; this.name = name; this.price = price;
    }

    @Override public String toString() {
        return String.format("[#%d %-14s Rs.%,10.2f]", id, name, price);
    }

    // equals/hashCode on id -> lets HashSet detect duplicate products
    @Override public boolean equals(Object o) {
        return (o instanceof Product p) && p.id == this.id;
    }
    @Override public int hashCode() { return Integer.hashCode(id); }
}

class Order {
    String item;
    int priority;   // 1 = highest

    Order(String item, int priority) { this.item = item; this.priority = priority; }

    @Override public String toString() { return item + " (priority " + priority + ")"; }
}

public class InventorySystem {
    public static void main(String[] args) {

        // ================= (1) ArrayList + Comparator =================
        System.out.println("==============================================================");
        System.out.println(" 1. ArrayList - store products, sort by price with Comparator");
        System.out.println("==============================================================");
        List<Product> products = new ArrayList<>();
        products.add(new Product(1, "Keyboard",      1499.00));
        products.add(new Product(2, "Monitor",      12999.50));
        products.add(new Product(3, "Mouse",          599.99));
        products.add(new Product(4, "SSD 1TB",       6499.00));
        products.add(new Product(5, "Webcam",        2299.00));

        System.out.println("  Insertion order (enhanced for-loop):");
        for (Product p : products) System.out.println("    " + p);

        // Comparator as a lambda - ascending by price
        products.sort(Comparator.comparingDouble(p -> p.price));
        System.out.println("\n  Sorted by price ASCENDING (Comparator.comparingDouble):");
        for (Product p : products) System.out.println("    " + p);

        products.sort(Comparator.comparingDouble((Product p) -> p.price).reversed());
        System.out.println("\n  Sorted by price DESCENDING (.reversed()):");
        for (Product p : products) System.out.println("    " + p);

        // ================= (2) HashMap =================
        System.out.println("\n==============================================================");
        System.out.println(" 2. HashMap - product ID -> name (lookup / update / delete)");
        System.out.println("==============================================================");
        Map<Integer, String> catalog = new HashMap<>();
        for (Product p : products) catalog.put(p.id, p.name);
        System.out.println("  Catalog          : " + catalog);

        System.out.println("  LOOKUP  id 3     : " + catalog.get(3));
        System.out.println("  LOOKUP  id 99    : " + catalog.getOrDefault(99, "<not found>"));

        catalog.put(3, "Gaming Mouse");                      // update = put with existing key
        System.out.println("  UPDATE  id 3     -> " + catalog.get(3));

        catalog.remove(5);                                   // delete
        System.out.println("  DELETE  id 5     -> contains? " + catalog.containsKey(5));
        System.out.println("  Final catalog    : " + catalog);

        // ================= (3) HashSet =================
        System.out.println("\n==============================================================");
        System.out.println(" 3. HashSet - duplicate entries removed automatically");
        System.out.println("==============================================================");
        List<Product> shipment = new ArrayList<>(List.of(
                new Product(1, "Keyboard", 1499.00),
                new Product(2, "Monitor", 12999.50),
                new Product(1, "Keyboard", 1499.00),    // duplicate id 1
                new Product(3, "Mouse",     599.99),
                new Product(2, "Monitor", 12999.50)     // duplicate id 2
        ));
        System.out.println("  Incoming shipment list (" + shipment.size() + " entries, with duplicates):");
        for (Product p : shipment) System.out.println("    " + p);

        Set<Product> unique = new HashSet<>(shipment);
        System.out.println("\n  After new HashSet<>(shipment) -> " + unique.size() + " unique products:");
        // Iterator (explicit) over the Set
        Iterator<Product> it = unique.iterator();
        while (it.hasNext()) {
            System.out.println("    (via Iterator) " + it.next());
        }

        // ================= (4) PriorityQueue =================
        System.out.println("\n==============================================================");
        System.out.println(" 4. PriorityQueue - orders processed by priority (1 = urgent)");
        System.out.println("==============================================================");
        Queue<Order> orderQueue = new PriorityQueue<>(Comparator.comparingInt(o -> o.priority));
        orderQueue.add(new Order("Restock SSDs",      3));
        orderQueue.add(new Order("Ship to customer",  1));
        orderQueue.add(new Order("Update price tags", 4));
        orderQueue.add(new Order("Repair monitor",    2));

        System.out.println("  Insertion order was: 3, 1, 4, 2 - but poll() always gives the smallest:");
        while (!orderQueue.isEmpty()) {
            System.out.println("    PROCESSING -> " + orderQueue.poll());
        }

        System.out.println("\n  Iterator + enhanced-for were demonstrated across List, Map, Set & Queue.");
    }
}
