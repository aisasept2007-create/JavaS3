package lab1;
import java.util.Scanner;
public class RevFor {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter a number to print its reverse:");
        int n=sc.nextInt();
        System.out.print("Reverse of the number is ");
        for(int i=n;i>0;i/=10){
            System.out.print(i%10);
        }
        sc.close();
    }
}
