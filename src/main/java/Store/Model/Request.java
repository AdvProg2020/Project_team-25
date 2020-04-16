package Store.Model;

import Store.Model.Enums.RequestType;

public class Request {
    private RequestType type; // 5 Types
    private Product product;
    private Seller seller;
    private Offer offer;
    private boolean change;

    Request(Seller seller) {
        this.type = RequestType.REGISTER_SELLER;
        this.seller = seller;
    }

    Request(Product product, boolean change) {
        this.product = product;
        this.change = change;
        this.type = RequestType.ADD_NEW_PRODUCT;
        if (this.change)
            this.type = RequestType.CHANGE_PRODUCT;
    }

    Request(Seller seller, Offer offer, boolean change) {
        this.offer = offer;
        this.change = change;
        this.type = RequestType.ADD_NEW_OFFER;
        if (this.change)
            this.type = RequestType.CHANGE_OFFER;
    }
}
