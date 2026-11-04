# Lab 03 - Inheritance and Polymorphism

A three-level class hierarchy - `Vehicle` to `Car` to `ElectricCar` - showing method overriding at each level, the `super` keyword, dynamic method dispatch, and a `final` method in the base class.

Course outcome CO2 (apply inheritance and polymorphism), Bloom level K3.

## Files

- `VehicleHierarchy.java` - the three classes plus a driver
- `output.txt` - the program output
- `screenshots/output-1.png` - screenshot of the output

## Running it

```
javac VehicleHierarchy.java
java VehicleHierarchy
```

## How the code works

`super` does two separate jobs here. In each constructor, `super(...)` calls the parent constructor, so creating one `ElectricCar` runs the `Vehicle`, then `Car`, then `ElectricCar` constructors in that order - you can see all three messages in the output. Inside the overridden `describe()`, `super.describe()` calls the parent's version and then adds to it, so `ElectricCar.describe()` walks the whole chain upward.

Dynamic method dispatch is the main idea. The driver puts a plain `Vehicle`, a `Car`, and an `ElectricCar` into one `Vehicle[]` array and calls `start()` on each. The compiler only knows they're all `Vehicle`s, but at runtime the JVM looks at the real object and calls that class's override. So the same line of code produces three different outputs depending on the actual type.

`registrationInfo()` is marked `final`. Every subclass inherits it and can call it, but none can override it. The source has a comment showing the compile error you get if you try - that's the verification the lab asks for.

## Screenshot

![Lab 03 running in the terminal](screenshots/output-1.png)

## Output

```text
Creating new ElectricCar("Tata", 200, 4, 40):
  [Vehicle constructor] brand=Tata
  [Car constructor] doors=4
  [ElectricCar constructor] battery=40 kWh

-> static type: Vehicle | runtime type: Vehicle
  Vehicle.start()      -> Generic vehicle is starting...
-> static type: Vehicle | runtime type: Car
  Car.start()          -> Turning ignition key... engine started!
-> static type: Vehicle | runtime type: ElectricCar
  ElectricCar.start()  -> Silent start... battery system online!
```

One `new ElectricCar(...)` prints three constructor lines (the `super` chain). The three `start()` calls go through the same `Vehicle` reference but run different code, which is dynamic dispatch. The full output also shows `describe()` climbing the hierarchy and the `final` method running on all three objects.

## Likely viva questions

- **Multilevel vs multiple inheritance?** Multilevel is an A to B to C chain, which Java allows. Multiple inheritance (one class, two parents) is not allowed for classes - you use interfaces for that.
- **When is the overridden method chosen?** At runtime, from the object's actual class. This is called late binding.
- **Can a constructor be final?** No. Constructors aren't inherited, so `final` would mean nothing.
- **Why make a method final?** To stop subclasses from changing critical behaviour, and it lets the JVM optimize the call.
