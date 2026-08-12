package tut5;
import java.util.Vector;
public class SumVec {
    public static void main(String[] args) {
        Vector<Integer> numbers = new Vector<>();
        numbers.add(10);
        numbers.add(20);
        numbers.add(30);
        numbers.add(40);
        numbers.add(50);

        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        System.out.println("Sum of the elements in the Vector: " + sum);
    }
}
