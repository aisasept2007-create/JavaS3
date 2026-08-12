package lab1;
public class Prime100 {
    public static void main(String[] args) {
        int i=0,j=0;
        System.out.println("First 100 prime numbers are:");
        for(i=2;i<=100;i++){
            for(j=2;j<=i;j++){
                if(i%j==0){
                    break;
                }
            }
            if(i==j){
                System.out.print(i+" ");
            }
        }
    }
}
