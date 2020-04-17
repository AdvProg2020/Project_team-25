package main.java.Store.Model;

import main.java.Store.Model.Enums.VerifyStatus;

public class Comment {
    private User commentingUser;
    private Product product;
    private String commentText;
    private boolean hasBought;
    private VerifyStatus acceptanceStatus;

    public Comment(User commentingUser, Product product, String commentText, boolean hasBought, VerifyStatus acceptanceStatus) {
        this.commentingUser = commentingUser;
        this.product = product;
        this.commentText = commentText;
        this.hasBought = hasBought;
        this.acceptanceStatus = acceptanceStatus;
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
