package tut6;

public class Animal {
    public String species;

    public Animal(String species) {
        this.species = species;
    }

    public static void main(String[] args) {
        Animal myAnimal = new Animal("Elephant");

        System.out.println("Direct Access: " + myAnimal.species);
    }
}
