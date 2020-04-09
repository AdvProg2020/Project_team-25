package Store.Model;

import java.util.ArrayList;

public class Product {

    private int productID;
    private int productStatus;
    private Category category;
    private String name;
    private Seller seller;
    private String brand;
    private double price;
    private ArrayList<Seller> sellers = new ArrayList<Seller>();
    private boolean availablity;
    private String attributes;
    private String description;
    private float averageRating;
    private ArrayList<Rating> Ratings = new ArrayList<Rating>();
    private ArrayList<Comment> comments = new ArrayList<Comment>();

    private static ArrayList<Product> allProducts = new ArrayList<Product>();

    public Product(int productID, int productStatus, Category category, String name, Seller seller, String brand, double price, boolean availablity, String attributes, String description, float averageRating) {
        this.productID = productID;
        this.productStatus = productStatus;
        this.category = category;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.availablity = availablity;
        this.attributes = attributes;
        this.description = description;
        this.averageRating = averageRating;
        this.seller = seller;
    }

    public static void addProduct(Product product) {

    }

    public static void deleteProduct(Product product) {

    }
}
