abstract class Shape {
    abstract double area();

    String name() {
        return getClass().getSimpleName();
    }
}

class Circle extends Shape {
    private final double radius;

    Circle(double radius) {
        this.radius = radius;
    }

    @Override
    double area() {
        return Math.PI * radius * radius;
    }
}

class Rectangle extends Shape {
    private final double length, width;

    Rectangle(double length, double width) {
        this.length = length;
        this.width = width;
    }

    @Override
    double area() {
        return length * width;
    }
}

class Triangle extends Shape {
    private final double base, height;

    Triangle(double base, double height) {
        this.base = base;
        this.height = height;
    }

    @Override
    double area() {
        return 0.5 * base * height;
    }
}

public class ShapeDemo {
    public static void main(String[] args) {
        Shape[] shapes = {
            new Circle(3),
            new Rectangle(4, 5),
            new Triangle(6, 8),
            new Circle(1.5),
            new Rectangle(2.5, 4),
            new Triangle(10, 3.5)
        };

        double total = 0;
        double largestArea = 0;
        Shape largest = null;

        for (Shape s : shapes) {
            double a = s.area();
            total += a;

            if (largest == null || a > largestArea) {
                largest = s;
                largestArea = a;
            }

            System.out.printf("%-10s area = %8.2f   running total = %8.2f%n",
                    s.name(), a, total);
        }

        System.out.printf("%nTotal area   : %.2f%n", total);
        System.out.printf("Largest shape: %s with area %.2f%n", largest.name(), largestArea);
    }
}
