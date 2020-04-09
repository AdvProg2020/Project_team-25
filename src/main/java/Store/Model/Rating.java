package Store.Model;

public class Rating {
    private User user;
    private double rating;
    private Product product;

    public Rating(User user, double rating, Product product) {
        this.user = user;
        this.rating = rating;
        this.product = product;
    }
}
