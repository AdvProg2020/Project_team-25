package Store.Model;

public class Request {
    private int type; // 5 Types
    private Product product;
    private Seller seller;
    private Offer offer;
    private boolean change;

    Request(Seller seller) {
        this.type = 1;
        this.seller = seller;
    }

    Request(Product product, boolean change) {
        this.product = product;
        this.change = change;
        this.type = 2;
        if (this.change)
            this.type++;
    }

    Request(Seller seller, Offer offer, boolean change) {
        this.offer = offer;
        this.change = change;
        this.type = 2;
        if (this.change)
            this.type++;
    }
}
