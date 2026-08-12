import java.util.Scanner;
class Marks{
    int mark1, mark2, mark3;

    public Marks(int mark1, int mark2, int mark3) {
        this.mark1 = mark1;
        this.mark2 = mark2;
        this.mark3 = mark3;
    }
public void display() {
    int total = mark1 + mark2 + mark3;
    double average = total / 3;
    System.out.println("Total marks: " + total);
    System.out.println("Average marks: " + average);
}
public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter marks for subject 1: ");
    int m1 = sc.nextInt();
    
    System.out.print("Enter marks for subject 2: ");
    int m2 = sc.nextInt();
    
    System.out.print("Enter marks for subject 3: ");
    int m3 = sc.nextInt();
    
    Marks m = new Marks(m1, m2, m3);
    System.out.println("--- Marks Details ---");
    m.display();
    
    sc.close();
}
}
