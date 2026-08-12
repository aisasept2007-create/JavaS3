package tut6;

class CorporatePackage {
    
    
    public static class Employee {
        protected int salary;

        public Employee(int salary) {
            this.salary = salary;
        }

        protected void showSalary() {
            System.out.println("Salary: " + salary);
        }
    }
}


class ManagementPackage {
    
    
    public static class Manager extends CorporatePackage.Employee {
        private String department;

        public Manager(int salary, String department) {
            super(salary); 
            this.department = department;
        }

        public void testAccess() {
            System.out.println("--- Subclass Access Testing ---");
            
            
            this.salary = 95000; 
            System.out.println("Manager successfully changed salary directly via inheritance.");
            
            this.showSalary();
            System.out.println("Department: " + department);
        }
    }
}


class ExternalPackage {
    
    public static class Stranger {
        public void testAccess() {
            System.out.println("\n--- Non-Subclass in Different Package Access Testing ---");
            
            CorporatePackage.Employee emp = new CorporatePackage.Employee(50000);
            System.out.println("Employee object created: " + emp);
        }
    }
}
