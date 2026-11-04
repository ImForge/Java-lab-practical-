/**
 * Lab 03 - Multilevel Inheritance & Polymorphism
 * Hierarchy : Vehicle -> Car -> ElectricCar
 * Demonstrates: method overriding at each level, use of the super keyword,
 *               dynamic method dispatch, and a final method in the base class.
 *
 * CO2 : Apply inheritance and polymorphism in Java programs
 * Bloom Level: K3 - Apply
 */

// ====================== LEVEL 1 : BASE CLASS ======================
class Vehicle {
    protected String brand;
    protected int maxSpeed;   // km/h

    Vehicle(String brand, int maxSpeed) {
        this.brand = brand;
        this.maxSpeed = maxSpeed;
        System.out.println("  [Vehicle constructor] brand=" + brand);
    }

    void start() {
        System.out.println("  Vehicle.start()      -> Generic vehicle is starting...");
    }

    void describe() {
        System.out.println("  Vehicle.describe()   -> " + brand + " | top speed " + maxSpeed + " km/h");
    }

    /*
     * FINAL METHOD - cannot be overridden by ANY subclass.
     * Un-commenting an override of registrationInfo() in Car or ElectricCar
     * produces the compile-time error:
     *   "registrationInfo() in Car cannot override registrationInfo() in Vehicle;
     *    overridden method is final"
     * which VERIFIES the behaviour of the final keyword.
     */
    final void registrationInfo() {
        System.out.println("  Vehicle.registrationInfo() [FINAL] -> RTO registration applies to every "
                + getClass().getSimpleName());
    }
}

// ====================== LEVEL 2 : INTERMEDIATE CLASS ======================
class Car extends Vehicle {
    protected int doors;

    Car(String brand, int maxSpeed, int doors) {
        super(brand, maxSpeed);               // super -> parent constructor
        this.doors = doors;
        System.out.println("  [Car constructor] doors=" + doors);
    }

    @Override
    void start() {                            // overriding level 1
        System.out.println("  Car.start()          -> Turning ignition key... engine started!");
    }

    @Override
    void describe() {
        super.describe();                     // super -> reuse parent behaviour
        System.out.println("  Car.describe()       -> a " + doors + "-door car");
    }

    // final void registrationInfo() {}       // <-- would NOT compile (final in Vehicle)
}

// ====================== LEVEL 3 : MOST DERIVED CLASS ======================
class ElectricCar extends Car {
    private int batteryKWh;

    ElectricCar(String brand, int maxSpeed, int doors, int batteryKWh) {
        super(brand, maxSpeed, doors);        // super chains up through Car -> Vehicle
        this.batteryKWh = batteryKWh;
        System.out.println("  [ElectricCar constructor] battery=" + batteryKWh + " kWh");
    }

    @Override
    void start() {                            // overriding level 2
        System.out.println("  ElectricCar.start()  -> Silent start... battery system online!");
    }

    @Override
    void describe() {
        super.describe();                     // calls Car.describe() which calls Vehicle.describe()
        System.out.println("  ElectricCar.describe() -> " + batteryKWh + " kWh battery, zero emissions");
    }
}

// ====================== DRIVER ======================
public class VehicleHierarchy {
    public static void main(String[] args) {
        System.out.println("=========================================================");
        System.out.println(" 1. CONSTRUCTOR CHAINING VIA super (observe call order)");
        System.out.println("=========================================================");
        System.out.println("Creating new ElectricCar(\"Tata\", 200, 4, 40):");
        ElectricCar nexonEV = new ElectricCar("Tata", 200, 4, 40);

        System.out.println("\n=========================================================");
        System.out.println(" 2. METHOD OVERRIDING AT EACH LEVEL");
        System.out.println("=========================================================");
        Vehicle plainVehicle = new Vehicle("Generic", 80);
        Car swift           = new Car("Maruti Swift", 180, 4);

        System.out.println("plainVehicle.start():");
        plainVehicle.start();
        System.out.println("swift.start():");
        swift.start();
        System.out.println("nexonEV.start():");
        nexonEV.start();

        System.out.println("\n=========================================================");
        System.out.println(" 3. super KEYWORD - describe() reuses parent versions");
        System.out.println("=========================================================");
        System.out.println("nexonEV.describe() walks UP the chain using super:");
        nexonEV.describe();

        System.out.println("\n=========================================================");
        System.out.println(" 4. DYNAMIC METHOD DISPATCH (runtime polymorphism)");
        System.out.println("=========================================================");
        System.out.println("One Vehicle[] reference array, three different runtime types:");
        Vehicle[] garage = { plainVehicle, swift, nexonEV };
        for (Vehicle v : garage) {
            System.out.println("-> static type: Vehicle | runtime type: " + v.getClass().getSimpleName());
            v.start();      // JVM picks the override of the RUNTIME type
        }

        System.out.println("\n=========================================================");
        System.out.println(" 5. FINAL METHOD - inherited everywhere, overridable nowhere");
        System.out.println("=========================================================");
        for (Vehicle v : garage) {
            v.registrationInfo();
        }
        System.out.println("  (See source comment: overriding registrationInfo() fails to compile.)");
    }
}
