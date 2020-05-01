package Store.Model;

import Store.Model.Enums.CheckingStatus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Product implements Serializable {

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
    private ArrayList<String> filters = new ArrayList<>();
    private static HashSet<String> allFilters = new HashSet<>();
    private int visited;

    private static int idCounter = 0;

    private static ArrayList<Product> allProducts = new ArrayList<Product>();

    public Product(CheckingStatus productStatus, Category category, String name, Seller seller, String brand, double price, boolean availablity, String attributes, String description) {
        this.productID = idCounter++;
        this.productStatus = productStatus;
        this.category = category;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.availablity = availablity;
        this.attributes = attributes;
        this.description = description;
        this.averageRating = 0;
        this.seller = seller;
        this.visited = 0;
    }

    public static void setAllProducts(ArrayList<Product> allProducts) {
        Product.allProducts = allProducts;
    }

    public static void setIdCounter(int idCounter) {
        Product.idCounter = idCounter;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public boolean equals(Product other) {
        if (productStatus != other.getProductStatus()) {
            return false;
        }
        else if (category != other.getCategory()) {
            return false;
        }
        else if (!name.equals(other.getName())) {
            return false;
        }
        else if (!brand.equals(other.getBrand())) {
            return false;
        }
        else if (availablity != other.getAvailablity()) {
            return false;
        }
        else if (!attributes.equals(other.getAttributes())) {
            return false;
        }
        else if (!description.equals(other.getDescription())) {
            return false;
        }
        else if (!comments.equals(other.comments)) {
            return false;
        }
        else if (!ratings.equals(other.ratings)) {
            return false;
        }
        else if (!filters.equals(other.filters)) {
            return false;
        }
        else if (visited != other.getVisited()) {
            return false;
        }
        return true;
    }

    public static ArrayList<Product> getAllProducts() {
        return allProducts;
    }

    public static void addProduct(Product product) {
        allProducts.add(product);
    }

    public void addComment(User user, String commentTitle, String content) {
        this.comments.add(new Comment(user, this, commentTitle, content));
    }

    public ArrayList<Comment> getComments() {
        return comments;
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

    public static Product getProductByName(String name) {
        for (Product product : allProducts) {
            if (product.getName().equalsIgnoreCase(name)) {
                return product;
            }
        }
        return null;
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

    public ArrayList<String> getFilters() {
        return filters;
    }

    public static HashSet<String> getAllFilters() {
        return allFilters;
    }

    public void addFilter(String filter) {
       this.filters.add(filter);
       allFilters.add(filter);
    }

    public void deleteFilter(String filter) {
        this.filters.remove(filter);
    }

    public boolean hasFilter(String filter) {
        return this.filters.contains(filter);
    }

    public Date getStartingDate() {  //just call when the product is in a offer
        if (Offer.getOfferOfProduct(this) == null) {
            return null;
        }
        return Offer.getOfferOfProduct(this).getStartingTime();
    }

    public Date getEndingDate() {  // just call when the product is in a offer
        if (Offer.getOfferOfProduct(this) == null) {
            return null;
        }
        return Offer.getOfferOfProduct(this).getEndingTime();
    }

    public int getVisited() {
        return visited;
    }

    public boolean getAvailablity() {
        return this.availablity;
    }

    @Override
    public String toString() {
        return "(" + this.name + " " + this.productID + ")";
    }
}
