package Store.Model;

import Store.Model.Product;
import Store.Model.Enums.CheckingStatus;

import java.util.ArrayList;
import java.util.Date;

public class Offer {
    private int offID;
    private User user;
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<String> filters = new ArrayList<String>();
    private CheckingStatus offerStatus;
    private Date startingTime, endingTime;
    // StartingTime and EndingTime?
    private double offPercent;

    private static ArrayList<Offer> allOffers = new ArrayList<Offer>();
    private static ArrayList<Product> allOffProducts = new ArrayList<>();

    private static int idCounter = 0;

    public Offer(User user, CheckingStatus offerStatus, double offPercent) {
        this.offID = idCounter++;
        this.user = user;
        this.products = products;
        this.offerStatus = offerStatus;
        this.offPercent = offPercent;
        allOffProducts.addAll(products);
    }

    public static void setIdCounter(int idCounter) {
        Offer.idCounter = idCounter;
    }

    public User getUser() {
        return user;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void addFilter(String filter) {
        this.filters.add(filter);
    }

    public void removeFilter(String filter) {
        this.filters.remove(filter);
    }

    public boolean hasFilter(String filter) {
        return this.filters.contains(filter);
    }

    public ArrayList<String> getFilters() {
        return filters;
    }

    public boolean containsProduct(Product product) {
        return this.products.contains(product);
    }

    public static ArrayList<Offer> getAllOffers() {
        return allOffers;
    }

    public static Offer getOfferByID(int id) {
        for (Offer offer : allOffers) {
            if (offer.getOffID() == id) {
                return offer;
            }
        }
        return null;
    }

    public static boolean hasOfferByID(int id) {
        for (Offer offer : allOffers) {
            if (offer.getOffID() == id) {
                return true;
            }
        }
        return false;
    }

    public static void addOfferToAllOffers(Offer offer) {
        allOffers.add(offer);
    }

    public static void deleteOfferFromAllOffers(Offer offer) {
        allOffers.remove(offer);
    }

    public double getOffPercent() {
        return offPercent;
    }

    public void setOffPercent(double offPercent) {
        this.offPercent = offPercent;
    }

    public boolean canBeUsedInDate(Date now) {
        return (this.startingTime.before(now) && this.endingTime.after(now));
    }

    public CheckingStatus getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(CheckingStatus offerStatus) {
        this.offerStatus = offerStatus;
    }

    public void addProduct(Product product) {
        this.products.add(product);
        allOffProducts.add(product);
    }

    public void removeProduct(Product product) {
        this.products.remove(product);
        allOffProducts.remove(product);
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

    public void setStartingTime(Date startingTime) {
        this.startingTime = startingTime;
    }

    public void setEndingTime(Date endingTime) {
        this.endingTime = endingTime;
    }

    public Date getStartingTime() {
        return this.startingTime;
    }

    public Date getEndingTime() {
        return this.endingTime;
    }

    public int getOffID() {
        return offID;
    }

    public static ArrayList<Product> getAllOffProducts() {
        return allOffProducts;
    }

    @Override
    public String toString() {
        return "{" +
                "offID=" + offID +
                ", startingTime=" + startingTime +
                ", endingTime=" + endingTime +
                ", offPercent=" + offPercent +
                '}';
    }
}