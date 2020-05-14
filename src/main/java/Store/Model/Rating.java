package Store.Model;

import Store.Model.Product;

import java.io.Serializable;

public class Rating implements Serializable {
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
