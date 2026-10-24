package store;

public class Product {
    private final int id;
    private final String code;
    private final String name;
    private final String category;
    private final double price;
    private final int stock;

    public Product(int id, String code, String name, String category, double price, int stock) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.category = category;
        this.price = price;
        this.stock = stock;
    }
    public int getId() { return id; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int getStock() { return stock; }
    @Override public String toString() { return name + " (" + code + ")"; }
}

