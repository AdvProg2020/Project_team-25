package main.java.Store.Model;

import main.java.Store.Model.Enums.CheckingStatus;

import java.util.ArrayList;

public class Product {

    //Deboo: it needs buyer
    private int productID;
    private CheckingStatus productStatus;
    private Category category;
    private String name;
    private Seller seller;
    private String brand;
    private double price;
    // private ArrayList<Seller> sellers = new ArrayList<Seller>();
    private boolean availablity;
    private String attributes;
    private String description;
    private float averageRating;
    private ArrayList<Rating> ratings = new ArrayList<Rating>();
    private ArrayList<Comment> comments = new ArrayList<Comment>();

    private static ArrayList<Product> allProducts = new ArrayList<Product>();

    public Product(int productID, CheckingStatus productStatus, Category category, String name, Seller seller, String brand, double price, boolean availablity, String attributes, String description, float averageRating) {
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
        allProducts.add(product);
    }

    public void addComment(User user, String title, String content) {

    }

    public void rate(Customer customer, double rating) {
        this.ratings.add(new Rating(customer, rating, this));
    }

    public double getAverageRating() {
        double sum = 0;
        for (Rating rating : this.ratings) {
            sum += rating.getRating();
        }
        return sum / this.ratings.size();
    }

    public static void deleteProduct(Product product) {
        allProducts.remove(product);
    }

    public static Product getProductByID(int id) {
        for (Product product : allProducts) {
            if (product.getProductID() == id) {
                return product;
            }
        }
        return null;
    }

    public static boolean hasProductWithID(int id) {
        for (Product product : allProducts) {
            if (product.getProductID() == id) {
                return true;
            }
        }
        return false;
    }



    public int getProductID() {
        return this.productID;
    }

    public Category getCategory() {
        return this.category;
    }

    public String getAttributes() {
        return this.attributes;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public double getPrice() {
        return this.price;
    }

    public Seller getSeller() {
        return this.seller;
    }

    public CheckingStatus getProductStatus() {
        return this.productStatus;
    }



    public void setAvailablity(boolean availablity) {
        this.availablity = availablity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductStatus(CheckingStatus productStatus) {
        this.productStatus = productStatus;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
