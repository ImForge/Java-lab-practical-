/**
 * Lab 04 - Interfaces & Abstract Classes
 * Demonstrates: interface Shape { area(), perimeter() } implemented by
 *               Circle, Rectangle, Triangle; abstract class Polygon with a
 *               concrete describe() method; MULTIPLE interface implementation;
 *               and an interface-vs-abstract-class comparison via a driver.
 *
 * CO2 : Apply interfaces and abstract classes for modular design
 * Bloom Level: K3 - Apply
 */

// ====================== THE INTERFACE ======================
interface Shape {
    // interface fields are implicitly public static final
    double PI = 3.14159265358979;

    // abstract methods - every implementing class MUST provide these
    double area();
    double perimeter();

    // default method (Java 8+) - shared behaviour with a body
    default void printReport() {
        System.out.printf("  %-10s | area = %10.2f | perimeter = %10.2f%n",
                getClass().getSimpleName(), area(), perimeter());
    }
}

// ====================== A SECOND INTERFACE (for multiple implementation) ======================
interface Drawable {
    void draw();
}

// ====================== THE ABSTRACT CLASS ======================
abstract class Polygon implements Shape {
    protected int sides;

    Polygon(int sides) {
        this.sides = sides;
    }

    // CONCRETE method - abstract classes may mix implemented and abstract members.
    // An interface (pre-Java-8) could not hold state like 'sides'; this can.
    void describe() {
        System.out.println("  I am a " + getClass().getSimpleName()
                + ", a polygon with " + sides + " sides.");
    }
}

// ====================== IMPLEMENTATIONS ======================

// Circle implements TWO interfaces -> multiple interface implementation
class Circle implements Shape, Drawable {
    private final double radius;

    Circle(double radius) { this.radius = radius; }

    @Override public double area()      { return PI * radius * radius; }
    @Override public double perimeter() { return 2 * PI * radius; }

    @Override public void draw() {
        System.out.println("  Drawing Circle    : ( O )  radius=" + radius);
    }
}

// Rectangle extends the abstract class (which itself implements Shape)
class Rectangle extends Polygon implements Drawable {
    private final double length, width;

    Rectangle(double length, double width) {
        super(4);
        this.length = length;
        this.width = width;
    }

    @Override public double area()      { return length * width; }
    @Override public double perimeter() { return 2 * (length + width); }

    @Override public void draw() {
        System.out.println("  Drawing Rectangle : [=====]  " + length + " x " + width);
    }
}

class Triangle extends Polygon {
    private final double a, b, c;

    Triangle(double a, double b, double c) {
        super(3);
        this.a = a; this.b = b; this.c = c;
    }

    @Override public double area() {                 // Heron's formula
        double s = perimeter() / 2;
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
    @Override public double perimeter() { return a + b + c; }
}

// ====================== DRIVER CLASS ======================
public class ShapeDemo {
    public static void main(String[] args) {
        Circle    circle    = new Circle(5);
        Rectangle rectangle = new Rectangle(8, 3);
        Triangle  triangle  = new Triangle(3, 4, 5);

        System.out.println("===========================================================");
        System.out.println(" 1. INTERFACE CONTRACT - area() & perimeter() for each shape");
        System.out.println("===========================================================");
        Shape[] shapes = { circle, rectangle, triangle };
        for (Shape s : shapes) {
            s.printReport();          // default method from the interface
        }

        System.out.println("\n===========================================================");
        System.out.println(" 2. ABSTRACT CLASS - concrete describe() inherited by subclasses");
        System.out.println("===========================================================");
        rectangle.describe();
        triangle.describe();
        System.out.println("  (Circle has no describe(): it is not a Polygon - by design.)");

        System.out.println("\n===========================================================");
        System.out.println(" 3. MULTIPLE INTERFACE IMPLEMENTATION - Shape + Drawable");
        System.out.println("===========================================================");
        Drawable[] drawables = { circle, rectangle };
        for (Drawable d : drawables) {
            d.draw();
        }
        System.out.println("  Circle implements Shape AND Drawable directly;");
        System.out.println("  Rectangle gets Shape via Polygon and adds Drawable - both legal.");

        System.out.println("\n===========================================================");
        System.out.println(" 4. INTERFACE vs ABSTRACT CLASS - observed comparison");
        System.out.println("===========================================================");
        System.out.println("  Interface Shape   : only a contract (area/perimeter) + default method;");
        System.out.println("                      a class may implement MANY interfaces.");
        System.out.println("  Abstract Polygon  : holds STATE (sides) + concrete describe();");
        System.out.println("                      a class may extend ONLY ONE abstract class.");
        System.out.println("  Polymorphism works through both: Shape[] held all three shapes above.");
    }
}
