package main.java.Store.Model;

public class Rating {
    private User user;
    private double rating;
    private Product product;

    public Rating(User user, double rating, Product product) {
        this.user = user;
        this.rating = rating;
        this.product = product;
    }

    public double getRating() {
        return this.rating;
    }

    public User getUser() {
        return user;
    }
}
