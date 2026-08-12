package lab1;
import java.util.Scanner;
public class GCDwhile {
    public static void main(String[] args) {
        int a=0,b=0;
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter two numbers to find their GCD:");
        a=sc.nextInt();
        b=sc.nextInt();
        while(a!=b){
            if(a>b){
                a=a-b;
            }
            else{
                b=b-a;
            }
        }
        System.out.println("GCD of the two numbers is "+a);
        sc.close();
    }
}
