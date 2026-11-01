abstract class Employee {
    private final String name;
    private final int id;

    Employee(String name, int id) {
        this.name = name;
        this.id = id;
    }

    String getName() {
        return name;
    }

    int getId() {
        return id;
    }

    abstract double monthlySalary();
}

class FullTime extends Employee {
    private final double fixedSalary;

    FullTime(String name, int id, double fixedSalary) {
        super(name, id);
        this.fixedSalary = fixedSalary;
    }

    @Override
    double monthlySalary() {
        return fixedSalary;
    }
}

class PartTime extends Employee {
    private final double hours, rate;

    PartTime(String name, int id, double hours, double rate) {
        super(name, id);
        this.hours = hours;
        this.rate = rate;
    }

    @Override
    double monthlySalary() {
        return hours * rate;
    }
}

class Intern extends Employee {
    private final double stipend;

    Intern(String name, int id, double stipend) {
        super(name, id);
        this.stipend = stipend;
    }

    @Override
    double monthlySalary() {
        return stipend;
    }
}

public class PayrollDemo {
    public static void main(String[] args) {
        Employee[] staff = {
            new FullTime("Aarav", 101, 60000),
            new PartTime("Diya", 102, 80, 450),
            new Intern("Kabir", 103, 15000),
            new FullTime("Meera", 104, 72000),
            new PartTime("Rohan", 105, 60, 500)
        };

        double total = 0;

        for (Employee e : staff) {
            double salary = e.monthlySalary();
            total += salary;

            String note = (e instanceof Intern) ? "   [Intern: stipend only]" : "";
            System.out.printf("%-8s ID: %-4d salary = %10.2f%s%n",
                    e.getName(), e.getId(), salary, note);
        }

        System.out.printf("%nTotal payroll: %.2f%n", total);
    }
}
