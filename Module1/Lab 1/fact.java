package lab1;
import java.util.Scanner;
public class fact {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        int i=1;
        int fact=1;
        System.out.println("Enter a number to find its factorial:");
        int n=sc.nextInt();
        while(i<=n){
            fact=fact*i;
            i++;
        }
        System.out.println("factorial of number "+n+" is "+fact);
        sc.close();
    }
}
