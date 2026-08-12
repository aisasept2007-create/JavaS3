package lab1;
import java.util.Scanner;
public class FiboWhile {
    public static void main(String[] args) {
        Scanner sc=new Scanner(System.in);
        System.out.print("Enter the number of terms for Fibonacci series: ");
        int n=sc.nextInt();
        int t1=0, t2=1, nextTerm;
        while(t1<=n){
            System.out.print(t1+" ");
            nextTerm=t1+t2;
            t1=t2;
            t2=nextTerm;
        }
        sc.close();
    }
}
