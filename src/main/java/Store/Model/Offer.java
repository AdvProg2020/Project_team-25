package main.java.Store.Model;

import main.java.Store.Model.Enums.CheckingStatus;

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
}
