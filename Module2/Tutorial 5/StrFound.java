package tut5;
import java.util.Vector;
import java.util.Scanner;
public class StrFound {
    public static void main(String[] args) {
        Vector<String> items = new Vector<>();
        items.add("Pen");
        items.add("Notebook");
        items.add("Eraser");
        items.add("Marker");

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter item to search: ");
        String target = sc.nextLine();

        boolean found = false;
        for (String item : items) {
            if (item.equals(target)) {
                found = true;
                break; // Exit the loop as soon as the item is found
            }
        }

        if (found) {
            System.out.println("Item found");
        } else {
            System.out.println("Item not found");
        }
        sc.close();
    }
}
