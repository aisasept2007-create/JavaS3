import java.util.Scanner;
class Employee {
    int empID;
    String name;
    double salary;    

public Employee(int empId, String name, double salary) {
    this.empID = empId;
    this.name = name;
    this.salary = salary;
}
public void displayEmployeeDetails() {
    System.out.println("Employee ID: " + empID);
    System.out.println("Employee Name: " + name);
    System.out.println("Employee Salary: " + salary);
}
}

public class EmployeeDemo {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        for (int i = 0; i < 2; i++) {
            System.out.println("Enter details for Employee " + (i + 1) + ":");
            System.out.print("Enter Employee ID: ");
            int empId = sc.nextInt();
            sc.nextLine(); // Consume the newline character
            
            System.out.print("Enter Employee Name: ");
            String name = sc.nextLine();
            
            System.out.print("Enter Employee Salary: ");
            double salary = sc.nextDouble();
            
            Employee emp = new Employee(empId, name, salary);
            System.out.println("--- Employee Details ---");
            emp.displayEmployeeDetails();
        }
        sc.close();
    }

}
