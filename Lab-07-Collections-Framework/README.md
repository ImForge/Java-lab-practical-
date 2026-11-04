# Lab 07 - Collections Framework

A small inventory system built around four collections: an `ArrayList` sorted with a `Comparator`, a `HashMap` for product lookups, a `HashSet` to drop duplicates, and a `PriorityQueue` to process orders by priority. Iterators and enhanced for-loops are used throughout.

Course outcome CO4 (use the Collections Framework for data management), Bloom level K3.

## Files

- `InventorySystem.java` - `Product` and `Order` classes plus the driver
- `output.txt` - the program output
- `screenshots/output-1.png` - screenshot of the output

## Running it

```
javac InventorySystem.java
java InventorySystem
```

## How the code works

**ArrayList with Comparator.** Products are sorted by price using `Comparator.comparingDouble(...)`, then again with `.reversed()` for descending order. Keeping the ordering in a comparator instead of inside `Product` means you can sort by price today and by name tomorrow without touching the class, which is the difference from `Comparable`.

**HashMap.** A map of product id to name handles lookup (`get`), a safe lookup for a missing key (`getOrDefault`), update (putting a value on an existing key overwrites it), and delete (`remove`). All of these are effectively constant time.

**HashSet.** A shipment list with duplicate products goes into a `HashSet` and comes out with the duplicates gone. This only works because `Product` overrides `equals()` and `hashCode()` based on the id - without that, the set would think every object is unique. It's a common exam trap. The set is then walked with an explicit iterator.

**PriorityQueue.** Orders are inserted in the order 3, 1, 4, 2 but `poll()` always returns the lowest priority number first. A priority queue is a heap, not a FIFO queue, so insertion order doesn't decide what comes out.

## Screenshot

![Lab 07 running in the terminal](screenshots/output-1.png)

## Output

```text
  Sorted by price ASCENDING (Comparator.comparingDouble):
    [#3 Mouse          Rs.    599.99]
    [#1 Keyboard       Rs.  1,499.00]
    [#2 Monitor        Rs. 12,999.50]

  LOOKUP  id 3     : Mouse
  UPDATE  id 3     -> Gaming Mouse
  DELETE  id 5     -> contains? false

  Incoming shipment list (5 entries, with duplicates):
  After new HashSet<>(shipment) -> 3 unique products

  Insertion order was: 3, 1, 4, 2 - but poll() always gives the smallest:
    PROCESSING -> Ship to customer (priority 1)
    PROCESSING -> Repair monitor (priority 2)
    PROCESSING -> Restock SSDs (priority 3)
    PROCESSING -> Update price tags (priority 4)
```

Five shipment entries become three unique products, and the orders come out 1, 2, 3, 4 even though they went in as 3, 1, 4, 2.
