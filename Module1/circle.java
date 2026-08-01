import java.util.Scanner;
class Circle{
    int radius;

public Circle(int radius) {
    this.radius = radius;
}
public void displayCircleDetails() {
   double area = 3.14 * radius * radius;
   double circumference = 2 * 3.14 * radius;
    System.out.println("Circle Radius: " + radius);
    System.out.println("Circle Area: " + area);
    System.out.println("Circle Circumference: " + circumference);
}

public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter Circle Radius: ");
    int radius = sc.nextInt();
    
    Circle c = new Circle(radius);
    System.out.println("--- Circle Details ---");
    c.displayCircleDetails();
    
    sc.close();
}
}
