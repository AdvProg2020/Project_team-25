package Store.Model;

import Store.Model.Product;
import Store.Model.Enums.RequestType;
import Store.Model.Enums.VerifyStatus;

public class Request {
    private RequestType requestType; // 5 Types
    private VerifyStatus status;
    private Product product;
    private Seller seller;
    private Offer offer;
    private Object newObject;
    private boolean change;

    Request(Seller seller) {
        this.requestType = RequestType.REGISTER_SELLER;
        this.seller = seller;
        this.status = VerifyStatus.WAITING;
    }

    Request(Product product, boolean change, Object newObject) {
        this.product = product;
        this.change = change;
        this.requestType = RequestType.ADD_NEW_PRODUCT;
        if (this.change) {
            this.requestType = RequestType.CHANGE_PRODUCT;
            this.newObject = newObject;
        }
        this.status = VerifyStatus.WAITING;
    }

    Request(Seller seller, Offer offer, boolean change, Object newObject) {
        this.offer = offer;
        this.change = change;
        this.seller = seller;
        this.requestType = RequestType.ADD_NEW_OFFER;
        if (this.change) {
            this.requestType = RequestType.CHANGE_OFFER;
            this.newObject = newObject;
        }
        this.status = VerifyStatus.WAITING;
    }

    public Seller getSeller() {
        return seller;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public Offer getOffer() {
        return offer;
    }

    public Offer getNewOffer() {
        return (Offer)newObject;
    }

    public Product getProduct() {
        return product;
    }

    public Product getNewProduct() {
        return (Product) newObject;
    }

    public void setStatus(VerifyStatus status) {
        this.status = status;
    }
}
