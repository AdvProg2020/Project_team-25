package main.java.Store.Model;

public class Comment {
    private User commentingUser;
    private Product product;
    private String commentText;
    private boolean hasBought;
    private int acceptanceStatus;

    public Comment(User commentingUser, Product product, String commentText, boolean hasBought, int acceptanceStatus) {
        this.commentingUser = commentingUser;
        this.product = product;
        this.commentText = commentText;
        this.hasBought = hasBought;
        this.acceptanceStatus = acceptanceStatus;
    }
}
