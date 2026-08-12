package tut5;
import java.util.Vector;
public class FruitVec {
    public static void main(String[] args) {
        // Create a Vector of Strings to store fruit names
        Vector<String> fruits = new Vector<>();

        // Add 5 fruit elements to the Vector
        fruits.add("Apple");
        fruits.add("Banana");
        fruits.add("Mango");
        fruits.add("Orange");
        fruits.add("Grapes");

        // Display each fruit on a new line using an enhanced for-loop
        System.out.println("List of fruits:");
        for (String fruit : fruits) {
            System.out.println(fruit);
        }
    }
}