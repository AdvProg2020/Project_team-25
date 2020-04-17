package main.java.Store.Model;

import main.java.Store.Model.Enums.RequestType;
import main.java.Store.Model.Enums.VerifyStatus;

public class Request {
    private RequestType type; // 5 Types
    private VerifyStatus status;
    private Product product;
    private Seller seller;
    private Offer offer;
    private Object firstObject;
    private Object secondObject;
    private boolean change;

    Request(Seller seller) {
        this.type = RequestType.REGISTER_SELLER;
        this.seller = seller;
        this.status = VerifyStatus.WAITING;
    }

    Request(Product product, boolean change, Object firstObject, Object secondObject) {
        this.product = product;
        this.change = change;
        this.type = RequestType.ADD_NEW_PRODUCT;
        if (this.change) {
            this.type = RequestType.CHANGE_PRODUCT;
            this.firstObject = firstObject;
            this.secondObject = secondObject;
        }
        this.status = VerifyStatus.WAITING;
    }

    Request(Seller seller, Offer offer, boolean change, Object firstObject, Object secondObject) {
        this.offer = offer;
        this.change = change;
        this.type = RequestType.ADD_NEW_OFFER;
        if (this.change) {
            this.type = RequestType.CHANGE_OFFER;
            this.firstObject = firstObject;
            this.secondObject = secondObject;
        }
        this.status = VerifyStatus.WAITING;
        //Deboo: seller must be defined
    }
}
