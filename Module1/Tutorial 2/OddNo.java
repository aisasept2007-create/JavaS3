package tut2;
import java.util.Scanner;
public class OddNo {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the value of n: ");
        int n = scanner.nextInt();
        
        System.out.println("Odd numbers from 1 to " + n + ":");
        for (int i = 1; i <= n; i++) {
            if (i % 2 == 0) {
                continue; 
            }
            
            // This line only executes if the number is odd
            System.out.print(i + " ");
        }
        scanner.close();
    }
}
