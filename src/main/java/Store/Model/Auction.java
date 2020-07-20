package Store.Model;

import Store.Model.Enums.CheckingStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Queue;

public class Auction implements Serializable {
    int auctionId;
    private Seller seller;
    private Product product;
    private Customer currentBuyer;
    private double basePrice;
    private double highestPrice = 0.0;
    private LocalDateTime endingTime;
    private Queue<String> messages;
    private int messagePort;

    private static ArrayList<Auction> allAuctions = new ArrayList<>();
    private static ArrayList<Product> allAuctionsProducts = new ArrayList<>();


    private static int idCounter = 0;

    public Auction(Seller seller, Product product, LocalDateTime endingTime) {
        this.auctionId = idCounter++;
        this.seller = seller;
        this.product = product;
        this.endingTime = endingTime;
        this.basePrice = product.getPrice();
        allAuctions.add(this);
        allAuctionsProducts.add(product);
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setMessagePort(int messagePort) {
        this.messagePort = messagePort;
    }

    public Queue<String> getMessages() {
        return messages;
    }

    public int getMessegePort() {
        return messagePort;
    }

    public static ArrayList<Product> getAllAuctionsProducts() {
        return allAuctionsProducts;
    }

    public static int getIdCounter() {
        return idCounter;
    }

    public static Auction getAuctionOfProduct(Map<String, Object> product) {
        Product product1 = Product.getProductByID((Integer)product.get("id"));
        for (Auction auction: allAuctions)
        {
            if (auction.getProduct().equals(product1) && auction.getSeller().equals(product1.getSeller()))
                return auction;
        }
        return null;
    }

    public Customer getCurrentBuyer() {
        return currentBuyer;
    }

    public double getHighestPrice() {
        return highestPrice;
    }

    public User getSeller() {
        return seller;
    }

    public Product getProduct() {
        return product;
    }

    public static Auction getAuctionByID(int id) {
        for (Auction auction : allAuctions) {
            if (auction.getAuctionID() == id) {
                return auction;
            }
        }
        return null;
    }

    public int getAuctionID() {
        return auctionId;
    }

    public void setEndingTime(LocalDateTime endingTime) {
        this.endingTime = endingTime;
    }

    public LocalDateTime getEndingTime() {
        return this.endingTime;
    }

    public static ArrayList<Auction> getAllAuctions() {
        return allAuctions;
    }

    public void finish(){
        currentBuyer.setMoney(currentBuyer.getMoney() - highestPrice);
        seller.setMoney(seller.getMoney() + highestPrice);
        allAuctions.remove(this);
        boolean flag = false;
        for (Auction auction: allAuctions)
            if (auction.getProduct().equals(product))
                flag = true;
        if (!flag)
            allAuctionsProducts.remove(product);
    }

    @Override
    public String toString() {
        return "{" +
                "auctionID=" + auctionId +
                ", endingTime=" + endingTime +
                ", Product=" + this.product +
                '}';
    }

    @Override
    public boolean equals(Object object)
    {
        Auction auction = (Auction) object;
        if(seller.equals(auction.getSeller()) && product.equals(auction.getProduct()))
            return true;
        return false;
    }

    public void changeBet(Customer customer, double newPrice) {
        this.currentBuyer = customer;
        this.highestPrice = newPrice;
    }
}