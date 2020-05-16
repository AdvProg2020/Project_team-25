package Store.Model;

import Store.Model.Enums.RequestType;
import Store.Model.Enums.VerifyStatus;

import java.io.Serializable;
import java.util.ArrayList;

public class Request implements Serializable {
    private static ArrayList<Request> allRequests = new ArrayList<Request>();
    private RequestType requestType; // 5 Types
    private VerifyStatus status;
    private Product product;
    private Seller seller;
    private Offer offer;
    private Object newObject;
    private boolean change;

    public Request(Seller seller) {
        this.requestType = RequestType.REGISTER_SELLER;
        this.seller = seller;
        this.status = VerifyStatus.WAITING;
        if(!Manager.isInPendingRequests(this))
            allRequests.add(this);
    }

    public Request(Product product, boolean change, Object newObject) {
        this.seller = product.getSeller();
        this.product = product;
        this.change = change;
        this.requestType = RequestType.ADD_NEW_PRODUCT;
        if (this.change) {
            this.requestType = RequestType.CHANGE_PRODUCT;
            this.newObject = newObject;
        }
        this.status = VerifyStatus.WAITING;

        if(!Manager.isInPendingRequests(this))
            allRequests.add(this);
    }

    public Request(Seller seller, Offer offer, boolean change, Object newObject) {
        this.offer = offer;
        this.change = change;
        this.seller = seller;
        this.requestType = RequestType.ADD_NEW_OFFER;
        if (this.change) {
            this.requestType = RequestType.CHANGE_OFFER;
            this.newObject = newObject;
        }
        this.status = VerifyStatus.WAITING;

        if(!Manager.isInPendingRequests(this))
            allRequests.add(this);
    }

    public static ArrayList<Request> getAllRequests() {
        return allRequests;
    }

    public static void setAllRequests(ArrayList<Request> allRequests) {
        Request.allRequests = allRequests;
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
        return (Offer) newObject;
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

    public int getId() {
        return allRequests.indexOf(this) + 1;
    }

    @Override
    public String toString() {
        String result =  "Request{" +
                "requestType=" + requestType +
                ", id=" + this.getId() +
                ", status=" + status +
                ", product=" + product +
                ", seller=" + seller +
                ", offer=" + offer +
                ", newObject=" + newObject +
                ", change=" + change +
                '}' + "\nDetails: \n";
        if (requestType == RequestType.ADD_NEW_OFFER) {
            result = result.concat(this.offer.toString());
        }
        else if (requestType == RequestType.ADD_NEW_PRODUCT) {
            result = result.concat(this.product.toString());
        }
        else if (requestType == RequestType.CHANGE_OFFER) {
            result = result.concat(this.offer.toString() + "\n ---- TO ---- \n" + ((Offer) this.newObject).toString());
        }
        else if (requestType == RequestType.CHANGE_PRODUCT) {
            result = result.concat(this.product.toString() + "\n ---- TO ---- \n" + ((Product) this.newObject).toString());
        }
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        Request request = (Request)obj;
        if (requestType == request.getRequestType() && requestType == RequestType.ADD_NEW_OFFER) {
            if (seller.equals(request.getSeller()) && offer.equals(request.getOffer()))
                return true;
        }
        else if (requestType == request.getRequestType() && requestType == RequestType.ADD_NEW_PRODUCT) {
            if (seller.equals(request.getSeller()) && product.equals(request.getProduct()))
                return true;
        }
        else if (requestType == request.getRequestType() && requestType == RequestType.CHANGE_OFFER) {
            if (seller.equals(request.getSeller()) && offer.equals(request.getOffer()) && ((Offer)newObject).equals(request.getNewOffer()))
                return true;
        }
        else if (requestType == request.getRequestType() && requestType == RequestType.CHANGE_PRODUCT) {
            if (seller.equals(request.getSeller()) && product.equals(request.getProduct()) && ((Product)newObject).equals(request.getNewProduct()))
                return true;
        }
        else if (requestType == request.getRequestType() && requestType == RequestType.REGISTER_SELLER) {
            if (seller.equals(request.getSeller()) && User.getUserByUsername(seller.getUsername()) != null)
                return true;
        }
        return false;
    }

    public VerifyStatus getStatus() {
        return status;
    }
}
