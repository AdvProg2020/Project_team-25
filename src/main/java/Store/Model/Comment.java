package Store.Model;

import Store.Model.Product;
import Store.Model.Enums.VerifyStatus;

import java.io.Serializable;

public class Comment implements Serializable {
    private User commentingUser;
    private Product product;
    private String commentTitle;
    private String commentText;
    private boolean hasBought;
    private VerifyStatus acceptanceStatus;

    public Comment(User commentingUser, Product product, String commentTitle, String commentText) {
        this.commentingUser = commentingUser;
        this.product = product;
        this.commentTitle = commentTitle;
        this.commentText = commentText;
        if (commentingUser instanceof Customer) {
            this.hasBought = ((Customer) commentingUser).hasBoughtProduct(product); // Bound to change
        }
        else {
            this.hasBought = false;
        }
        this.acceptanceStatus = VerifyStatus.WAITING;
    }

    public String getCommentTitle() {
        return commentTitle;
    }

    public String getCommentText() {
        return this.commentText;
    }

    public VerifyStatus getAcceptanceStatus() {
        return this.acceptanceStatus;
    }

    public boolean getHasBought() {
        return this.hasBought;
    }

    public User getCommentingUser() {
        return this.commentingUser;
    }

    public void setAcceptanceStatus(VerifyStatus verifyCommentStatus) {
        this.acceptanceStatus = verifyCommentStatus;
    }

    public boolean isAccepted() {
        return this.acceptanceStatus.equals(VerifyStatus.ACCEPTED);
    }
}
