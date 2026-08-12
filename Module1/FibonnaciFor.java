package tut2;
import java.util.Scanner;
public class FibonnaciFor {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Enter the number of terms for Fibonacci series: ");
        int n = scanner.nextInt();
        
        int t1 = 0, t2 = 1;
        
        System.out.println("Fibonacci Series up to " + n + " terms:");
        
        for (int i = 1; i <= n; i++) {
            System.out.print(t1 + " ");
            int nextTerm = t1 + t2;
            t1 = t2;
            t2 = nextTerm;
        }
        scanner.close();
    }
}
