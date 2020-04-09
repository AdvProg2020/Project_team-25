package Store.Model;

import java.util.ArrayList;

public class Offer {
    private int offID;
    private ArrayList<Product> products = new ArrayList<Product>();
    private int offerStatus;
    // StartingTime and EndingTime?
    private double offPercent;

    private static ArrayList<Offer> allOffers = new ArrayList<Offer>();


    public Offer(int offID, int offerStatus, double offPercent) {
        this.offID = offID;
        this.products = products;
        this.offerStatus = offerStatus;
        this.offPercent = offPercent;
        allOffers.add(this);
    }
}
