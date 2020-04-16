package main.java.Store.Model;

import main.java.Store.Model.Enums.RequestType;

public class Request {
    private RequestType type; // 5 Types
    private Product product;
    private Seller seller;
    private Offer offer;
    private boolean change;

    Request(Seller seller) {
        this.type = RequestType.REGISTERSELLER;
        this.seller = seller;
    }

    Request(Product product, boolean change) {
        this.product = product;
        this.change = change;
        this.type = RequestType.ADDNEWPRODUCT;
        if (this.change)
            this.type = RequestType.CHANGEPRODUCT;
    }

    Request(Seller seller, Offer offer, boolean change) {
        this.offer = offer;
        this.change = change;
        this.type = RequestType.ADDNEWOFFER;
        if (this.change)
            this.type = RequestType.CHANGEOFFER;
    }
}
