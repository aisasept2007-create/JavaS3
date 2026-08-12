package tut5;
import java.util.Vector;
public class RemoveVecElement {
    public static void main(String[] args) {
        // Create a Vector of Strings with initial elements
        Vector<String> colors = new Vector<>();
        colors.add("Red");
        colors.add("Green");
        colors.add("Blue");
        colors.add("Yellow");

        colors.remove("Green");
        System.out.println("Final contents of the Vector:");
        for (String color : colors) {
            System.out.println(color);
        }
    }
}
