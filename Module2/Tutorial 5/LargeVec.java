package tut5;
import java.util.Vector;
public class LargeVec {
    public static void main(String[] args) {
        // Create a Vector of Integers with the specified values
        Vector<Integer> numbers = new Vector<>();
        numbers.add(25);
        numbers.add(60);
        numbers.add(15);
        numbers.add(80);
        numbers.add(45);

        // Find the maximum value using a basic for loop
        int max = numbers.get(0);
        for (int i = 1; i < numbers.size(); i++) {
            if (numbers.get(i) > max) {
                max = numbers.get(i);
            }
        }

        // Print the largest number
        System.out.println("The largest number is: " + max);
    }
}
