package Store.Model;

import Store.Model.Product;
import Store.Model.Enums.CheckingStatus;

import java.util.ArrayList;
import java.util.Date;

public class Offer {
    private int offID;
    private ArrayList<Product> products = new ArrayList<Product>();
    private CheckingStatus offerStatus;
    private Date startingTime, endingTime;
    // StartingTime and EndingTime?
    private double offPercent;

    private static ArrayList<Offer> allOffers = new ArrayList<Offer>();


    public Offer(int offID, CheckingStatus offerStatus, double offPercent) {
        this.offID = offID;
        this.products = products;
        this.offerStatus = offerStatus;
        this.offPercent = offPercent;
        allOffers.add(this);
    }

    public double getOffPercent() {
        return offPercent;
    }

    public boolean canBeUsedInDate(Date now) {
        return (this.startingTime.before(now) && this.endingTime.after(now));
    }

    public CheckingStatus getOfferStatus() {
        return offerStatus;
    }

    public void addProductToOffer(Product product) {
        this.products.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
    }

    public static void removeProductFromOffer(Product product) {
        for (Offer offer : allOffers) {
            if (offer.isProductInOffer(product)) {
                offer.removeProduct(product);
            }
        }
    }

    public boolean isProductInOffer(Product product) {
        for (Product currentProduct : this.products) {
            if (currentProduct == product) {
                return true;
            }
        }
        return false;
    }

    public static Offer getOfferOfProduct(Product product) {
        for (Offer offer : allOffers) {
            if (offer.isProductInOffer(product)) {
                return offer;
            }
        }
        return null;
    }

}
